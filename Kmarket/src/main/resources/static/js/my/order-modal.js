// order-modal.js

document.addEventListener("DOMContentLoaded", function () {
    // 🚨 1. 변수 선언을 블록 최상단에 모읍니다. (ReferenceError 해결)
    const orderLinks = document.querySelectorAll('.order-detail-link');
    const orderModalElement = document.getElementById('orderModal'); // 모달 요소 참조
    const body = document.body;

    orderLinks.forEach(link => {
        link.addEventListener('click', function (e) {
            e.preventDefault();

            const orderNumber = this.dataset.orderNumber;
            // 🚨 2. 컨텍스트 루트 포함 및 API 경로 호출
            const url = `/kmarket/my/orderDetail?orderNumber=${orderNumber}`;

            fetch(url)
                .then(response => {
                    if (!response.ok) throw new Error(`서버 응답 오류 (상태: ${response.status})`);
                    return response.json();
                })
                .then(data => {
                    // 3. 받은 JSON 데이터를 모달의 요소에 채워 넣습니다.
                    if (data) {
                        document.getElementById('modal-oDate').textContent = data.dateString;
                        document.getElementById('modal-orderNumber').textContent = data.orderNumber;
                        document.getElementById('modal-manufacture').textContent = data.manufacture;
                        document.getElementById('modal-prodName').textContent = data.prod_name;
                        document.getElementById('modal-piece').textContent = data.piece + '개';

                        document.getElementById('modal-salePrice').textContent = data.salePriceString || '0원';
                        document.getElementById('modal-discount').textContent = data.discountString || '0원';
                        document.getElementById('modal-price').textContent = data.priceString || '0원';

                        const imgPath = data.encodedImg1 ? data.encodedImg1 : '';
                        document.getElementById('modal-prod-img').setAttribute('src', imgPath);

                        document.getElementById('modal-stat').textContent = getOrderStatus(data.stat);

                        // 4. 모달 띄우기 (Bootstrap)
                        const orderModal = new bootstrap.Modal(orderModalElement);
                        orderModal.show();
                    }
                })
                .catch(err => {
                    console.error('주문 상세 정보를 불러오는 데 실패했습니다.', err);
                    alert("주문 상세 정보를 불러오는 데 실패했습니다. 서버 로그를 확인하세요.");
                });
        });
    });

    // 🚨 5. [X버튼 오류 해결] 모달이 닫힐 때 발생하는 이벤트에 리스너를 추가합니다.
    orderModalElement.addEventListener('hidden.bs.modal', function () {

        // body에 남아있는 모달 관련 클래스와 스타일을 강제로 제거합니다.
        body.classList.remove('modal-open');
        body.style.removeProperty('overflow');
        body.style.removeProperty('padding-right');

        // 남아있을 수 있는 회색 배경 (backdrop) 요소를 강제로 제거합니다.
        const backdrops = document.querySelectorAll('.modal-backdrop');
        backdrops.forEach(backdrop => {
            backdrop.remove();
        });

        // 모달 데이터도 초기화합니다. (선택 사항이나 권장)
        document.getElementById('modal-oDate').textContent = '';
        // ... (필요하다면 다른 id 요소들도 초기화) ...
    });
});

// 주문 상태 코드 변환 함수
function getOrderStatus(statCode) {
    switch (statCode) {
        case 0: return '결제대기';
        case 1: return '결제완료';
        case 2: return '배송준비';
        case 3: return '배송중';
        case 4: return '배송완료';
        case 5: return '주문취소';
        case 6: return '반품요청';
        case 7: return '교환요청';
        case 8: return '구매확정';
        default: return '알 수 없음';
    }
}