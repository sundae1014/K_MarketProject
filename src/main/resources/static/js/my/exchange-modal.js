document.addEventListener('DOMContentLoaded', function() {

    const exchangeModal = document.getElementById('exchangeModal');
    const exchangeForm = document.getElementById('exchangeForm');
    const CONTEXT_PATH = '/kmarket';

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
            default:
                statusText = '알 수 없음';
                statusClass = 'status-default';
                break;
        }
        return { text: statusText, class: statusClass };
    }


    /* =======================================================
     * 1. 모달 열릴 때 (show.bs.modal) 주문 상세 정보 Fetch 및 표시
     * ======================================================= */
    exchangeModal.addEventListener('show.bs.modal', function (event) {
        const button = event.relatedTarget;
        const orderNumber = button.getAttribute('data-order-number');

        exchangeForm.reset();

        if (orderNumber) {
            document.getElementById('exchangeFormOrderNumber').value = orderNumber;

            const url = `${CONTEXT_PATH}/my/orderDetail?orderNumber=${orderNumber}`;

            fetch(url)
                .then(response => {
                    if (!response.ok) throw new Error(`서버 응답 오류: ${response.status}`);
                    return response.json();
                })
                .then(data => {
                    if (data && data.orderNumber) {
                        const statElement = document.getElementById('exchange-modal-stat');
                        const statusInfo = getStatusInfo(data.stat);

                        // 🚨 상태 텍스트 및 클래스 업데이트
                        statElement.textContent = statusInfo.text;
                        // 기존 클래스 초기화 후 새로운 클래스 추가 (색상 적용)
                        statElement.className = '';
                        statElement.classList.add(statusInfo.class);


                        document.getElementById('exchange-modal-date').textContent = data.dateString || '';
                        document.getElementById('exchange-modal-orderNumber').textContent = data.orderNumber;
                        document.getElementById('exchange-modal-manufacture').textContent = data.manufacture || '';
                        document.getElementById('exchange-modal-prodName').textContent = data.prod_name || '상품명 없음';
                        document.getElementById('exchange-modal-piece').textContent = data.piece ? `${data.piece}개` : '1개';
                        document.getElementById('exchange-modal-realPrice').textContent = data.priceString || '0원';
                        document.getElementById('exchange-modal-salePrice').textContent = data.salePriceString || '0원';
                        document.getElementById('exchange-modal-discount').textContent = data.discountString || '0원';
                        document.getElementById('exchange-modal-finalPrice').textContent = data.priceString || '0원';

                        const imgElement = document.getElementById('exchange-modal-prod-img');
                        const fullImgPath = data.encodedImg1 ? `${data.encodedImg1}` : '/images/default.png';
                        imgElement.src = `${fullImgPath}`;
                        imgElement.alt = data.prod_name;

                    } else {
                        alert('주문 정보를 찾을 수 없습니다.');
                    }
                })
                .catch(error => {
                    console.error('주문 상세 정보 Fetch Error:', error);
                    alert('주문 정보를 불러오는 중 오류가 발생했습니다. (경로 및 API 확인)');
                });
        }
    });


    /* =======================================================
     * 3. 교환 폼 제출 이벤트 리스너 (AJAX POST /my/exchange)
     * ======================================================= */
    exchangeForm.addEventListener('submit', function(e) {
        e.preventDefault();

        const formData = new FormData(exchangeForm);
        const jsonData = {};

        formData.forEach((value, key) => {
            if (key === 'orderNumber' || key === 'exchange' || key === 'exchange_reason') {
                jsonData[key] = value;
            }
        });

        // console.log("전송할 JSON 데이터:", JSON.stringify(jsonData));

        if (!jsonData.orderNumber) {
            alert('주문 정보가 누락되었습니다.');
            return;
        }

        fetch(`${CONTEXT_PATH}/my/exchange`, {
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
                    const modalInstance = bootstrap.Modal.getInstance(exchangeModal);
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
});