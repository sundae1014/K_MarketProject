// admin/order.js (최종 수정 버전)

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
        // 1. 주문 상세 모달 (list.html: #orderModal) 처리 로직
        // =========================================================
        $('.order-detail-link').on('click', function(event) {
            event.preventDefault();

            var orderNumber = $(this).data('order-number');
            const modalTitle = $('#orderModal .modal-sm-title');
            const modalProductList = $('#modalProductList');

            // order-modal.html의 결제/배송 정보 ID
            const payOrderStatus = $('#payOrderStatus');
            const payFinalPriceFooter = $('#payFinalPriceFooter');
            const deliRecipName = $('#deliRecipName');
            const deliRecipHp = $('#deliRecipHp');
            const deliRecipAddr1 = $('#deliRecipAddr1');
            const deliRecipAddr2 = $('#deliRecipAddr2');
            const payMethod = $('#payMethod'); // 결제수단 표시 ID가 있다고 가정
            const payOrderNumber = $('#payOrderNumber');
            const payPaymentMethod = $('#payPaymentMethod'); // HTML ID에 맞춤
            const payOrdName = $('#payOrdName');
            const payOrdHp = $('#payOrdHp');


            modalTitle.text('주문번호: ' + orderNumber + ' 로딩 중...');
            modalProductList.html('<tr><td colspan="9" class="text-center"><i class="fas fa-spinner fa-spin"></i> 상품 정보 로딩 중...</td></tr>');

            $.ajax({
                url: CONTEXT_PATH + '/admin/order/delivery-detail/' + orderNumber,
                type: 'GET',
                dataType: 'json',
                success: function(response) {
                    if (response.success && response.order) {
                        const order = response.order;

                        // ⭐️⭐️ 수정된 부분: order.details -> order.products로 변경 ⭐️⭐️
                        // 형의 DTO에 상품 리스트 필드가 'products'인지 'details'인지 확인 후 수정
                        const products = order.products || [];

                        let totalOriginalPrice = 0; // 총 상품금액 (정가 합)
                        let totalDiscountAmount = 0; // 총 할인금액 (할인액 합)
                        let totalDeliveryFee = 0; // 총 배송비 (배송비 합)
                        let totalFinalPrice = 0; // 총 결제금액 (최종 합)

                        products.forEach(p => {
                            // OrderDTO 또는 AdminOrderDetailDTO 필드 사용
                            const originalPrice = p.price * p.itemPiece; // 원가 * 수량
                            const discountPricePerItem = p.price * (p.discount / 100); // 아이템당 할인 가격
                            const finalSalePrice = p.salePrice * p.itemPiece; // 최종 판매가 * 수량
                            const deliveryFee = p.deliveryFee || 0;

                            // 1. 총 상품금액 (원가 합) 누적
                            totalOriginalPrice += originalPrice;

                            // 2. 총 할인금액 누적: (원가 - 최종판매가) * 수량
                            totalDiscountAmount += (originalPrice - finalSalePrice);

                            // 3. 총 배송비 누적 (중복 합산 방지를 위해 로직 조정 필요)
                            // 일반적으로 주문 내 여러 상품의 배송비는 한 번만 부과되거나, 가장 큰 금액을 따릅니다.
                            // 여기서는 일단 모든 상품의 배송비를 합산하며, 실제 비즈니스 로직에 맞게 조정해야 합니다.
                            totalDeliveryFee += deliveryFee;

                            // 4. 총 결제 금액 누적
                            totalFinalPrice += finalSalePrice + deliveryFee;
                        });

                        const finalPaymentPrice = order.price || totalFinalPrice;

                        $('#modalTotalOriginalPrice').text(priceFormatter.format(totalOriginalPrice) + ' 원');
                        $('#modalTotalDiscountAmount').text(priceFormatter.format(totalDiscountAmount) + ' 원');
                        $('#modalTotalDeliveryFee').text(priceFormatter.format(totalDeliveryFee) + ' 원');
                        $('#modalFinalPrice').text(priceFormatter.format(finalPaymentPrice) + ' 원');

                        // 모달 상세 정보 채우기
                        modalTitle.text(order.order_number);
                        payOrderNumber.text(order.order_number); // 주문번호 재사용
                        payPaymentMethod.text(getPaymentText(order.payment)); // HTML ID에 맞춤
                        payOrdName.text(order.ordName); // DTO 필드명 사용
                        payOrdHp.text(order.ordHp); // DTO 필드명 사용

                        payOrderStatus.text(getAdminStatusText(order.stat));
                        payFinalPriceFooter.text(priceFormatter.format(order.price) + '원');
                        deliRecipName.text(order.name);
                        deliRecipHp.text(order.hp);
                        deliRecipAddr1.text('(' + order.zip + ') ' + order.addr);
                        deliRecipAddr2.text(order.addr2);
                        payMethod.text(getPaymentText(order.payment)); // 주석 해제 (payMethod ID가 있다면)

                        // 상품 목록 테이블 생성
                        let productHtml = '';
                        if (products.length > 0) {
                            products.forEach(p => {
                                // OrderDTO 또는 AdminOrderDetailDTO 필드 사용
                                // DTO 필드명(p.salePrice, p.itemPiece 등)이 일치하는지 확인!
                                const finalPrice = (p.salePrice * p.itemPiece);
                                const imgPath = p.img1;
                                const deliveryFee = p.deliveryFee || 0;

                                productHtml += `
                                    <tr>
                                        <td><img src="${CONTEXT_PATH}${imgPath.startsWith('/') ? imgPath : '/' + imgPath}" alt="상품사진" style="width: 50px;"></td>
                                        <td>${p.prod_number}</td>
                                        <td>${p.prod_name}</td>
                                        <td>${p.manufacture}</td>
                                        <td>${priceFormatter.format(p.price)} 원</td>
                                        <td>${p.discount}%</td>
                                        <td>${p.itemPiece}</td>
                                        <td>${priceFormatter.format(deliveryFee)} 원</td>
                                        <td>${priceFormatter.format(finalPrice)} 원</td>
                                    </tr>
                                `;
                            });
                        } else {
                            productHtml = `<tr><td colspan="9" class="text-center">주문 상품 정보가 없습니다. (products 필드 미포함 또는 필드명 불일치)</td></tr>`;
                        }
                        modalProductList.html(productHtml);

                    } else {
                        modalProductList.html('<tr><td colspan="9" class="text-center">주문 상세 정보를 불러오는데 실패했습니다. (데이터 없음)</td></tr>');
                    }
                },
                error: function(xhr, status, error) {
                    console.error("주문 상세 정보 로드 실패:", error);
                    modalProductList.html('<tr><td colspan="9" class="text-center">시스템 오류가 발생했습니다. (API 호출 실패)</td></tr>');
                    modalTitle.text('주문번호: ' + orderNumber + ' (오류)');
                }
            });
        });

        // =========================================================
        // 2. 배송 등록 모달 (addressModal) 처리 로직
        // =========================================================

        /**
         * 배송 정보 입력 버튼 클릭 시 모달 데이터 채우기 (list.html의 "배송입력" 버튼)
         */
        $('.delivery-input-btn').on('click', function() {
            // ⭐️ HTML에서 정의된 data-* 속성 이름에 맞춰 변수명을 수정 및 추가합니다. ⭐️
            var orderNumber = $(this).data('order-number');
            var recipName = $(this).data('order-name');      // th:data-order-name
            var recipReq = $(this).data('order-req');
            var recipZip = $(this).data('order-zip');        // th:data-order-zip
            var recipAddr1 = $(this).data('order-addr');     // th:data-order-addr
            var recipAddr2 = $(this).data('order-addr2');    // th:data-order-addr2

            // 모달의 주문번호 필드에 값 채우기 (address-modal.html)
            $('#deliveryOrderNumberDisplay').text(orderNumber);
            $('#deliveryOrderNumberInput').val(orderNumber);

            $('#recipNameInput').val(recipName);
            $('#zipInput').val(recipZip);
            $('#addr1Input').val(recipAddr1);
            $('#addr2Input').val(recipAddr2);
            $('#text-area').val(recipReq);
        });
        /**
         * 배송 정보 등록 폼 제출 처리 (address-modal.html의 #deliveryInputForm)
         */
        $('#deliveryInputForm').on('submit', function(e) {
            e.preventDefault();

            var postData = {};
            $(this).serializeArray().forEach(function(item) {
                postData[item.name] = item.value;
            });

            // 유효성 검사
            if (!postData.deliveryCompany || postData.deliveryCompany === '') {
                alert('택배사를 선택해 주세요.');
                return;
            }
            if (!postData.trackingNumber || postData.trackingNumber.trim() === '') {
                alert('운송장 번호를 입력해 주세요.');
                return;
            }

            postData['stat'] = 2;

            if (confirm(`주문번호 ${postData.orderNumber}에 운송장 정보를 등록하고 상태를 '배송준비'상태로 변경하시겠습니까?`)) {
                $.ajax({
                    // 서버의 API 엔드포인트 URL
                    url: CONTEXT_PATH + '/admin/order/delivery-input',
                    type: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify(postData),
                    success: function(response) {
                        if (response.success) {
                            alert(`주문 ${postData.orderNumber}이(가) 배송준비중으로 변경되었습니다.`);
                            $('#addressModal').modal('hide');
                            location.reload();
                        } else {
                            alert(`배송 정보 등록 실패: ${response.message || '서버 오류'}`);
                        }
                    },
                    error: function(xhr, status, error) {
                        console.error("배송 정보 등록 오류:", error);
                        alert('시스템 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.');
                    }
                });
            }
        });
    });
});