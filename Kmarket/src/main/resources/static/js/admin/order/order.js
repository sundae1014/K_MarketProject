// admin/order.js (수정)
document.addEventListener('DOMContentLoaded', function (){
    $(document).ready(function() {

        const CONTEXT_PATH = '/kmarket';
        const priceFormatter = new Intl.NumberFormat('ko-KR');

        // =========================================================
        // 유틸리티 함수
        // =========================================================

        function getAdminStatusText(statCode) {
            switch (parseInt(statCode)) {
                case 0: return '결제대기'; case 1: return '결제완료'; case 2: return '배송준비';
                case 3: return '배송중'; case 4: return '배송완료'; case 5: return '주문취소';
                case 6: return '반품요청'; case 7: return '교환요청'; case 8: return '구매확정';
                default: return '확인불가';
            }
        }

        /**
         * 결제 수단 코드(payment)에 따라 텍스트를 반환합니다.
         */
        function getPaymentText(paymentCode) {
            switch (parseInt(paymentCode)) {
                case 1: return '신용카드'; case 2: return '체크카드';
                case 3: return '계좌이체'; case 4: return '무통장입금';
                case 5: return '휴대폰결제'; case 6: return '카카오페이'; default: return '확인불가';
            }
        }

        // =========================================================
        // 1. 주문 상세 모달 처리 (`#orderModal`)
        // =========================================================

        const orderModal = $('#orderModal');

        orderModal.on('show.bs.modal', function (event) {

            var button = $(event.relatedTarget);
            // 주문 목록에서 '주문번호' 링크의 data-order-number 속성 사용
            var orderNumber = button.data('order-number');

            var modalOrderNumberTitle = $('#modalOrderNumber');

            // 데이터 로딩 시작 시, 초기화 및 로딩 메시지 설정
            modalOrderNumberTitle.text(orderNumber + ' 로딩 중...');
            $('#modalProductList').html('<tr><td colspan="9" class="text-center"><i class="fas fa-spinner fa-spin"></i> 상품 정보 로딩 중...</td></tr>');

            if (!orderNumber) {
                modalOrderNumberTitle.text('N/A');
                $('#modalProductList').html('<tr><td colspan="9" class="text-center alert alert-danger">주문번호 정보가 누락되었습니다.</td></tr>');
                return;
            }

            $.ajax({
                url: CONTEXT_PATH + '/admin/order/detail/' + orderNumber,
                type: 'GET',
                dataType: 'json',
                success: function(response) {

                    if (response.success && response.order) {
                        const order = response.order;       // ⭐️ OrderDTO (부모 DTO)
                        const details = order.details || [];// ⭐️ OrderDTO 내부의 List<AdminOrderDetailDTO> (자식 리스트)

                        let totalOriginalPrice = 0;
                        let totalDiscountAmount = 0;
                        let totalDeliveryFee = 0;

                        // 상품 목록 HTML 생성 및 총액 계산
                        let productHtml = '';
                        if (details.length > 0) {
                            details.forEach(detail => {
                                // ⭐️ AdminOrderDetailDTO의 필드명 사용: itemPiece, price, salePrice
                                const itemTotalPrice = detail.salePrice * detail.itemPiece;
                                const itemOriginalPrice = detail.price * detail.itemPiece;
                                const itemDiscountAmount = itemOriginalPrice - itemTotalPrice;

                                totalOriginalPrice += itemOriginalPrice;
                                totalDiscountAmount += itemDiscountAmount;
                                totalDeliveryFee += detail.delivery_fee || 0;

                                productHtml += `
                                <tr>
                                    <td>
                                        <img src="${CONTEXT_PATH}${detail.img1}" alt="상품사진">
                                    </td>
                                    <td>${detail.prod_number || 'N/A'}</td>
                                    <td>${detail.prod_name || 'N/A'}</td>
                                    <td>${detail.manufacture || 'N/A'}</td>
                                    <td>${priceFormatter.format(detail.price || 0)}</td>
                                    <td>${detail.discount || 0}</td>
                                    <td>${detail.itemPiece || 0}</td>
                                    <td>${priceFormatter.format(detail.delivery_fee || 0)}</td>
                                    <td>${priceFormatter.format(itemTotalPrice || 0)}</td>
                                </tr>
                            `;
                            });
                        } else {
                            productHtml += `<tr><td colspan="9" class="text-center">주문된 상품이 없습니다.</td></tr>`;
                        }

                        // =================================================================
                        // ⭐️ HTML ID에 데이터 채우기 ⭐️
                        // =================================================================

                        // 1. 모달 헤더
                        modalOrderNumberTitle.text(order.order_number || 'N/A');

                        // 2. 상품 목록 테이블
                        $('#modalProductList').html(productHtml);

                        // 3. 총액 정보 영역 (OrderDTO.price는 최종 결제 금액)
                        $('#modalTotalOriginalPrice').text(priceFormatter.format(totalOriginalPrice) + ' 원');
                        $('#modalTotalDiscountAmount').text(priceFormatter.format(totalDiscountAmount) + ' 원');
                        $('#modalTotalDeliveryFee').text(priceFormatter.format(totalDeliveryFee) + ' 원');
                        $('#modalFinalPrice').text(priceFormatter.format(order.price || 0) + ' 원');

                        // 4. 결제정보 영역
                        $('#payOrderNumber').text(order.order_number || 'N/A'); // 주문번호도 다시 채움
                        $('#payPaymentMethod').text(getPaymentText(order.payment));
                        $('#payOrdName').text(order.ordName || 'N/A');      // ⭐️ OrderDTO.ordName 사용
                        $('#payOrdHp').text(order.ordHp || 'N/A');          // ⭐️ OrderDTO.ordHp 사용
                        $('#payOrderStatus').text(getAdminStatusText(order.stat));
                        $('#payFinalPriceFooter').text(priceFormatter.format(order.price || 0) + '원');

                        // 5. 배송정보 영역 (수취인 정보는 OrderDTO의 name, hp, zip, addr1, addr2 사용)
                        $('#deliRecipName').text(order.name || 'N/A');
                        $('#deliRecipHp').text(order.hp || 'N/A');
                        $('#deliRecipZip').text(order.zip || 'N/A');
                        $('#deliRecipAddr1').text(order.addr || 'N/A');
                        $('#deliRecipAddr2').text(order.addr2 || '');
                        $('#deliReq').text(order.req || '없음');

                    } else {
                        modalOrderNumberTitle.text('N/A');
                        $('#modalProductList').html('<tr><td colspan="9" class="text-center alert alert-warning">주문 상세 정보를 불러오는데 실패했습니다. (데이터 없음)</td></tr>');
                    }
                },
                error: function(xhr, status, error) {
                    modalOrderNumberTitle.text('N/A');
                    console.error("주문 상세 정보 로드 실패:", error);
                    $('#modalProductList').html('<tr><td colspan="9" class="text-center alert alert-danger">시스템 오류가 발생했습니다.</td></tr>');
                }
            });
        });

        // 2. 배송 모달 처리 (`#addressModal`) - 참고용
        $('#addressModal').on('show.bs.modal', function (event) {
            console.log("배송하기 모달 열림 (주문 상태 변경/송장 입력)");
        });
    });
});