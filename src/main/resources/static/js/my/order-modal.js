document.addEventListener("DOMContentLoaded", function () {
    const orderLinks = document.querySelectorAll('.order-detail-link');

    orderLinks.forEach(link => {
        link.addEventListener('click', function (e) {
            e.preventDefault();

            const orderNumber = this.dataset.orderNumber;

            fetch(`/my/orderDetail?orderNumber=${encodeURIComponent(orderNumber)}`)
                .then(response => {
                    if (!response.ok) throw new Error('서버 오류');
                    return response.text();
                })
                .then(html => {
                    const modalBody = document.querySelector('#orderModalBody');
                    modalBody.innerHTML = html;
                })
                .catch(err => {
                    console.error(err);
                    alert("주문 상세 정보를 불러오는 데 실패했습니다.");
                });
        });
    });
});
