document.addEventListener('DOMContentLoaded', function() {

    // 전역 변수로 현재 구매확정할 주문번호를 저장할 변수
    let currentOrderNumber = 0;

    // 모달 DOM 요소를 변수로 선언 (이제는 DOM이 준비된 상태이므로 안전하게 찾을 수 있습니다.)
    const confirmModalEl = document.getElementById('confirmModal');

    // 모달 인스턴스 초기화 (요소가 존재할 경우에만)
    let confirmModalInstance = null;
    if (confirmModalEl) {
        // Bootstrap Modal 초기화
        confirmModalInstance = new bootstrap.Modal(confirmModalEl);

        // 1. 모달이 열릴 때(show) 이벤트 처리: 주문 번호 저장
        confirmModalEl.addEventListener('show.bs.modal', function (event) {
            const button = event.relatedTarget;
            const orderNumber = button.getAttribute('data-order-number');
            currentOrderNumber = orderNumber;
        });

        // 2. 모달 내 '확정' 버튼 클릭 이벤트 처리: 서버 요청
        const finalConfirmBtn = document.getElementById('finalConfirmBtn');
        if (finalConfirmBtn) {
            finalConfirmBtn.addEventListener('click', function() {

                // 모달 숨기기
                if (confirmModalInstance) {
                    confirmModalInstance.hide();
                }

                // 서버로 구매 확정 요청 전송
                if (currentOrderNumber && currentOrderNumber > 0) {
                    requestPurchaseConfirm(currentOrderNumber);
                } else {
                    alert("처리할 주문 정보가 유효하지 않습니다.");
                }
            });
        }
    }


    /**
     * 서버에 구매 확정 요청을 전송하는 AJAX 함수
     * @param {number} orderNumber - 확정할 주문 번호
     */
    function requestPurchaseConfirm(orderNumber) {
        // 🚨 jQuery를 사용하여 AJAX 요청을 보낸다고 가정합니다.
        $.ajax({
            url: '/kmarket/my/confirmPurchase',
            type: 'POST',
            data: { orderNumber: orderNumber },
            success: function(response) {
                if(response.success) {
                    alert("구매가 확정되었습니다.");
                    location.reload();
                } else {
                    alert("구매 확정 실패: " + (response.message || "처리 중 오류가 발생했습니다."));
                }
            },
            error: function(jqXHR, textStatus, errorThrown) {
                alert("구매 확정 중 통신 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.");
                console.error("AJAX Error:", textStatus, errorThrown);
            }
        });
    }

    document.querySelectorAll('.order-cancel-btn').forEach(button => {
        button.addEventListener('click', function() {

            if (confirm('상품이 이미 택배사로 전달되었을 경우 주문 취소가 불가하여 상품 수령 후 반품 또는 교환 요청 부탁드립니다.')) {
                const orderNumber = this.getAttribute('data-order-number');
                requestOrderCancel(orderNumber);
            }
        });
    });


    /**
     * 서버에 주문 취소 요청을 전송하는 AJAX 함수
     * @param {string} orderNumber - 취소할 주문 번호
     */
    function requestOrderCancel(orderNumber) {
        // 🚨 Context Path '/kmarket'를 포함하여 요청 URL 설정
        $.ajax({
            url: '/kmarket/my/cancelOrder', // 새로운 취소 API 경로
            type: 'POST',
            data: { orderNumber: orderNumber },
            success: function(response) {
                if (response.success) {
                    alert("주문이 취소되었습니다.");
                    location.reload(); // 페이지 새로고침
                } else {
                    alert("주문 취소에 실패하였습니다. 고객센터로 문의 부탁드립니다.");
                }
            },
            error: function() {
                alert("주문 취소 중 통신 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.");
            }
        });
    }

});