document.addEventListener('DOMContentLoaded', function() {

    const returnModal = document.getElementById('returnModal');
    const returnForm = document.getElementById('returnForm');
    const CONTEXT_PATH = '/kmarket'; // 형의 프로젝트 컨텍스트 경로에 맞게 수정해주세요.

    // 주문 상태 코드에 따라 텍스트와 클래스를 반환하는 함수
    function getStatusInfo(statCode) {
        let statusText;
        let statusClass;

        switch (statCode) {
            case 0:
                statusText = '결제대기';
                statusClass = 'status-payment-pending';
                break;
            case 1:
                statusText = '결제완료';
                statusClass = 'status-payment-complete';
                break;
            case 2:
                statusText = '배송준비';
                statusClass = 'status-preparing-shipment';
                break;
            case 3:
                statusText = '배송중';
                statusClass = 'status-shipping';
                break;
            case 4:
                statusText = '배송완료';
                statusClass = 'status-delivered';
                break;
            case 5:
                statusText = '주문취소';
                statusClass = 'status-order-cancelled';
                break;
            case 6:
                statusText = '반품요청';
                statusClass = 'status-return-request';
                break;
            case 7:
                statusText = '교환요청';
                statusClass = 'status-exchange-request';
                break;
            case 8:
                statusText = '구매확정';
                statusClass = 'status-purchase-confirmed';
                break;
            case 9:
                statusText = '취소완료';
                statusClass = 'status-cancellation-complete';
                break;
            default:
                statusText = '확인불가';
                statusClass = 'status-unknown';
        }

        return { text: statusText, class: statusClass };
    }

    // 통화 포맷 함수
    const priceFormatter = new Intl.NumberFormat('ko-KR');

    // =========================================================
    // 모달이 열릴 때 이벤트 처리 (데이터 불러오기)
    // =========================================================
    returnModal.addEventListener('show.bs.modal', function(e) {
        const button = e.relatedTarget; // 모달을 트리거한 버튼
        const orderNumber = button.getAttribute('data-order-number');
        const piece = button.getAttribute('data-piece');

        if (!orderNumber) {
            alert('주문번호가 누락되었습니다.');
            return;
        }

        // 1. 서버에 주문 상세 정보 요청
        fetch(`${CONTEXT_PATH}/my/orderDetail?orderNumber=${orderNumber}`)
            .then(response => response.json())
            .then(data => {
                if (!data) {
                    throw new Error('주문 상세 정보가 없습니다.');
                }

                // 2. HTML 요소에 데이터 바인딩

                // Hidden Input에 주문 번호 설정 (폼 전송용)
                document.getElementById('returnFormOrderNumber').value = orderNumber;

                // 상품 정보 영역

                // 🚨 [날짜 수정] Java에서 포맷팅한 값을 사용
                document.getElementById('return-modal-date').textContent = data.dateString;

                document.getElementById('return-modal-orderNumber').textContent = data.order_number;
                document.getElementById('return-modal-manufacture').textContent = data.manufacture || '';
                document.getElementById('return-modal-prodName').textContent = data.prod_name || '';
                document.getElementById('return-modal-piece').textContent = `수량: ${data.piece || 0}개`;

                // 가격 정보 영역 (결제금액)
                // data.realPrice가 없으므로 price, discount, salePrice를 사용하여 계산
                document.getElementById('return-modal-realPrice').textContent = priceFormatter.format(data.salePrice) + '원'; // 단일 상품 판매가
                document.getElementById('return-modal-salePrice').textContent = priceFormatter.format(data.salePrice * data.piece) + '원'; // 총 판매가
                document.getElementById('return-modal-discount').textContent = data.discountString || '0원';
                document.getElementById('return-modal-finalPrice').textContent = data.priceString || '0원';

                // 이미지 설정
                const imgElement = document.getElementById('return-modal-prod-img');
                // 🚨 [이미지 경로 수정] Java에서 만든 전체 경로를 그대로 사용
                const fullImgPath = data.encodedImg1;

                if (imgElement && fullImgPath) {
                    imgElement.src = fullImgPath;
                    imgElement.alt = data.prod_name;
                }

                // 주문 상태 설정
                const statusInfo = getStatusInfo(data.stat);
                document.getElementById('return-modal-stat').textContent = statusInfo.text;
                document.getElementById('return-modal-stat').className = statusInfo.class; // 클래스도 설정

            })
            .catch(error => {
                console.error('반품 모달 데이터 로드 중 에러 발생:', error);
                alert('주문 상세 정보를 가져오는 데 실패했습니다.');
            });
    });

    // =========================================================
    // 반품 요청 폼 제출 처리
    // =========================================================
    returnForm.addEventListener('submit', function(e) {
        e.preventDefault();

        const formData = new FormData(returnForm);
        const jsonData = {};

        // 폼 데이터를 JSON으로 변환 (orderNumber, exchange, exchange_reason만 전송)
        formData.forEach((value, key) => {
            if (key === 'orderNumber' || key === 'exchange' || key === 'exchange_reason') {
                jsonData[key] = value;
            }
        });

        if (!jsonData.orderNumber) {
            alert('주문 정보가 누락되었습니다.');
            return;
        }

        // 폼 데이터를 서버의 RestController로 전송
        fetch(`${CONTEXT_PATH}/my/return`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(jsonData)
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert(data.message);
                    // 모달 닫기 및 페이지 새로고침
                    const modalInstance = bootstrap.Modal.getInstance(returnModal);
                    if (modalInstance) {
                        modalInstance.hide();
                    }
                    window.location.reload();
                } else {
                    alert(`반품 요청 실패: ${data.message}`);
                }
            })
            .catch(error => {
                console.error('반품 요청 중 에러 발생:', error);
                alert('반품 요청 중 시스템 오류가 발생했습니다.');
            });
    });

    // 이미지 파일 핸들링 함수 (HTML onchange에서 사용)
    window.handleFileUpload = function(event, type) {
        const fileInput = event.target;
        if (fileInput.files.length > 0) {
            console.log(`선택된 파일: ${fileInput.files[0].name}`);
        }
    }
});