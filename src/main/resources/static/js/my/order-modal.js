// order-modal.js (최종)

document.addEventListener("DOMContentLoaded", function () {
    const orderLinks = document.querySelectorAll('.order-detail-link');
    const orderModalElement = document.getElementById('orderModal'); // 모달 요소 참조
    const body = document.body;

    orderLinks.forEach(link => {
        link.addEventListener('click', function (e) {
            e.preventDefault();

            const orderNumber = this.dataset.orderNumber;
            // 컨텍스트 루트 포함 및 API 경로 호출
            const url = `/kmarket/my/orderDetail?orderNumber=${orderNumber}`;

            fetch(url)
                .then(response => {
                    if (!response.ok) throw new Error(`서버 응답 오류 (상태: ${response.status})`);
                    return response.json();
                })
                .then(data => {
                    // 3. 받은 JSON 데이터를 모달의 요소에 채워 넣습니다.
                    if (data) {
                        // 🚨 날짜 정보 업데이트
                        document.getElementById('modal-oDate').textContent = data.dateString;
                        document.getElementById('modal-date').textContent = data.dateString; // ⬅️ 테이블 내부 날짜

                        document.getElementById('modal-orderNumber').textContent = data.orderNumber;
                        document.getElementById('modal-manufacture').textContent = data.manufacture;
                        document.getElementById('modal-prodName').textContent = data.prod_name;
                        document.getElementById('modal-piece').textContent = data.piece + '개';

                        document.getElementById('modal-salePrice').textContent = data.salePriceString || '0원';
                        document.getElementById('modal-discount').textContent = data.discountString || '0원';
                        document.getElementById('modal-price').textContent = data.priceString || '0원';
                        document.getElementById('modal-realPrice').textContent = data.priceString || '0원';

                        const imgPath = data.encodedImg1 ? data.encodedImg1 : '';
                        document.getElementById('modal-prod-img').setAttribute('src', imgPath);

                        const statElement = document.getElementById('modal-stat');
                        updateOrderStatusDisplay(statElement, data.stat);

                        // 4. 모달 띄우기 (Bootstrap)
                        const orderModal = new bootstrap.Modal(orderModalElement);
                        orderModal.show();
                    }
                })
                .catch(err => {
                    console.error('주문 상세 정보를 불러오는 데 실패했습니다.', err);
                });
        });
    });

    // X 버튼/ESC 오류 해결
    orderModalElement.addEventListener('hidden.bs.modal', function () {

        body.classList.remove('modal-open');
        body.style.removeProperty('overflow');
        body.style.removeProperty('padding-right');

        const backdrops = document.querySelectorAll('.modal-backdrop');
        backdrops.forEach(backdrop => {
            backdrop.remove();
        });

        // 초기화
        document.getElementById('modal-oDate').textContent = '';
        document.getElementById('modal-date').textContent = '';
    });
});

// 주문 상태 코드 변환 함수
function updateOrderStatusDisplay(element, statCode) {
    // 이전 클래스 초기화
    element.className = '';

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

    element.textContent = statusText;
    element.classList.add(statusClass);
}