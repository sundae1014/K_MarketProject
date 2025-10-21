// admin/delivery.js (최종 수정 버전)

document.addEventListener('DOMContentLoaded', function () {
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

        // 1. 주문 상태 코드 -> 텍스트 변환 적용 (목록 테이블)
        $('.order-stat').each(function() {
            const statCode = $(this).data('stat');
            $(this).text(getAdminStatusText(statCode));
        });

        // =========================================================
        // 2. 송장번호 클릭 시 모달 처리 (상세 정보 조회) - #deliveryModal
        // =========================================================

        $('.delivery-detail-link').on('click', function(event) {
            event.preventDefault();

            var orderNumber = $(this).data('order-number');
            // ⭐️ 추가: HTML의 data-tracking-number 속성 값 가져오기
            var trackingNumber = $(this).data('tracking-number');

            const modalOrderNumTitle = $('#deliveryModal .modal-sm-title');
            const modalProductList = $('#modalProductList');

            // delivery-modal.html의 상세 정보 ID
            const modalDeliOrderNumber = $('#modalDeliOrderNumber');
            const modalDeliRecipName = $('#modalDeliRecipName');
            const modalDeliRecipHp = $('#modalDeliRecipHp');
            const modalDeliRecipAddr = $('#modalDeliRecipAddr');
            const modalDeliCompany = $('#modalDeliCompany');
            const modalDeliTracking = $('#modalDeliTracking');
            const modalDeliReq = $('#modalDeliReq');

            modalOrderNumTitle.text('주문번호: ' + orderNumber);
            modalProductList.html('<tr><td colspan="9" class="text-center"><i class="fas fa-spinner fa-spin"></i> 상품 정보 로딩 중...</td></tr>');

            // ⭐️ 수정: trackingNumber 유무에 따라 URL 결정 ⭐️
            let url = CONTEXT_PATH + '/admin/order/delivery-detail/' + orderNumber;
            // trackingNumber가 유효한 값(null, 0이 아닐 경우)일 때만 쿼리 파라미터로 추가
            if (trackingNumber && trackingNumber > 0) {
                url += '?trackingNumber=' + trackingNumber;
            }

            $.ajax({
                url: url, // 수정된 URL 사용
                type: 'GET',
                dataType: 'json',
                success: function(response) {
                    if (response.success && response.order) {
                        const order = response.order;
                        // ⭐️ 수정: DTO의 필드명 'products' 사용 ⭐️
                        const products = order.products || [];

                        // 배송 및 결제 정보 채우기
                        modalOrderNumTitle.text(order.order_number);
                        modalDeliOrderNumber.text(order.order_number);

                        // 수령인 정보 (OrderDTO의 name, hp 필드 사용)
                        modalDeliRecipName.text(order.name || '-');
                        modalDeliRecipHp.text(order.hp || '-');
                        // 주소 통합하여 표시
                        modalDeliRecipAddr.text(`(${order.zip || ''}) ${order.addr || ''} ${order.addr2 || ''}`);

                        // 배송 정보
                        modalDeliCompany.text(order.deliveryCompany || '-');
                        modalDeliTracking.text(order.trackingNumber || '-');

                        // 기타/요청사항
                        modalDeliReq.text(order.req || '없음');

                        // 상품 목록 생성
                        let productHtml = '';
                        if (products.length > 0) {
                            products.forEach(p => {
                                // ⭐️ 수정: 모든 숫자 필드에 || 0 방어 로직 추가 ⭐️
                                const unitPrice = p.price || 0;             // 상품 원가 (OD_PRICE)
                                const unitSalePrice = p.salePrice || 0;     // 주문 시 최종 판매가 (OD_SALEPRICE)
                                const discountRate = p.discount || 0;       // 할인율
                                const quantity = p.itemPiece || 0;          // 수량 (OD_PIECE)
                                const deliveryFee = p.deliveryFee || 0;

                                // 최종 결제 금액 계산
                                const finalPrice = (unitSalePrice * quantity);

                                const imgPath = p.img1;

                                productHtml += `
                                    <tr>
                                        <td><img src="${CONTEXT_PATH}${imgPath.startsWith('/') ? imgPath : '/' + imgPath}" alt="상품사진" style="width: 50px;"></td>
                                        <td>${p.prod_number}</td>
                                        <td>${p.prod_name}</td>
                                        <td>${p.manufacture}</td>
                                        
                                        <td>${priceFormatter.format(unitPrice)} 원</td>
                                        
                                        <td>${discountRate}%</td>
                                        
                                        <td>${quantity} 건</td>
                                        
                                        <td>${priceFormatter.format(deliveryFee)} 원</td>
                                        
                                        <td>${priceFormatter.format(finalPrice)} 원</td>
                                    </tr>
                                `;
                            });
                        } else {
                            productHtml = `<tr><td colspan="9" class="text-center">주문 상품 정보가 없습니다.</td></tr>`;
                        }
                        modalProductList.html(productHtml);

                    } else {
                        modalProductList.html('<tr><td colspan="9" class="text-center">주문 상세 정보를 불러오는데 실패했습니다. (데이터 없음)</td></tr>');
                        modalOrderNumTitle.text('주문번호: ' + orderNumber);
                    }
                },
                error: function(xhr, status, error) {
                    console.error("배송 상세 정보 로드 실패:", error);
                    modalProductList.html('<tr><td colspan="9" class="text-center">시스템 오류가 발생했습니다. (API 호출 실패)</td></tr>');
                    modalOrderNumTitle.text('주문번호: ' + orderNumber);
                }
            });
        });
    });
});