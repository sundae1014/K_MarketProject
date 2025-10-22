// admin/order.js (최종 수정 버전 - DTO 필드명 매핑 및 String 타입 전송)

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
            const payOrderStatus = $('#payOrderStatus');
            const payFinalPriceFooter = $('#payFinalPriceFooter');
            const deliRecipName = $('#deliRecipName');
            const deliRecipHp = $('#deliRecipHp');
            const deliRecipAddr1 = $('#deliRecipAddr1');
            const deliRecipAddr2 = $('#deliRecipAddr2');
            const payOrderNumber = $('#payOrderNumber');
            const payPaymentMethod = $('#payPaymentMethod');
            const payOrdName = $('#payOrdName');
            const payOrdHp = $('#payOrdHp');


            modalTitle.text('주문번호: ' + orderNumber + ' 로딩 중...');
            modalProductList.html('<tr><td colspan="9" class="text-center"><i class="fas fa-spinner fa-spin"></i> 상품 정보 로딩 중...</td></tr>');

            $.ajax({
                url: CONTEXT_PATH + '/admin/order/detail/' + orderNumber,
                type: 'GET',
                dataType: 'json',
                success: function(response) {
                    if (response.success && response.order) {
                        const order = response.order;
                        const products = order.products || [];

                        let totalOriginalPrice = 0;
                        let totalDiscountAmount = 0;
                        let totalDeliveryFee = 0;
                        let totalFinalPrice = 0;

                        products.forEach(p => {
                            const originalPrice = p.price * p.itemPiece;
                            const finalSalePrice = p.salePrice * p.itemPiece;
                            const deliveryFee = p.deliveryFee || 0;

                            totalOriginalPrice += originalPrice;
                            totalDiscountAmount += (originalPrice - finalSalePrice);
                            totalDeliveryFee += deliveryFee;
                            totalFinalPrice += finalSalePrice + deliveryFee;
                        });

                        const finalPaymentPrice = order.price || totalFinalPrice;

                        $('#modalTotalOriginalPrice').text(priceFormatter.format(totalOriginalPrice) + ' 원');
                        $('#modalTotalDiscountAmount').text(priceFormatter.format(totalDiscountAmount) + ' 원');
                        $('#modalTotalDeliveryFee').text(priceFormatter.format(totalDeliveryFee) + ' 원');
                        $('#modalFinalPrice').text(priceFormatter.format(finalPaymentPrice) + ' 원');

                        modalTitle.text(order.order_number);
                        payOrderNumber.text(order.order_number);
                        payPaymentMethod.text(getPaymentText(order.payment));
                        payOrdName.text(order.ordName);
                        payOrdHp.text(order.ordHp);

                        payOrderStatus.text(getAdminStatusText(order.stat));
                        payFinalPriceFooter.text(priceFormatter.format(finalPaymentPrice) + '원');
                        deliRecipName.text(order.name);
                        deliRecipHp.text(order.hp);
                        deliRecipAddr1.text('(' + order.zip + ') ' + order.addr);
                        deliRecipAddr2.text(order.addr2);

                        let productHtml = '';
                        if (products.length > 0) {
                            products.forEach(p => {
                                const finalPrice = (p.salePrice * p.itemPiece);
                                const imgPath = p.img1;
                                const deliveryFee = p.delivery_fee || 0; // delivery_fee가 스네이크 케이스인지 확인

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
            var orderNumber = $(this).data('order-number');
            var recipName = $(this).data('order-name');
            var recipReq = $(this).data('order-req');
            var recipZip = $(this).data('order-zip');
            var recipAddr1 = $(this).data('order-addr');
            var recipAddr2 = $(this).data('order-addr2');

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

            var rawFormData = $(this).serializeArray();
            var temp = {}; // 임시 객체

            // 1. 폼 데이터 수집
            rawFormData.forEach(function(item) {
                temp[item.name] = item.value;
            });

            // 2. 💡 [최종 수정] OrderDTO 필드명에 맞게 최소 필수 필드 4개만 매핑/전송
            var postData = {
                // DTO: order_number (Snake Case) - DTO 필드명에 맞춤
                order_number: temp.orderNumber || temp.deliveryOrderNumberInput || temp.order_number,

                // DTO: deliveryCompany (클라이언트에서 보내는 카멜 케이스 그대로 전송)
                deliveryCompany: temp.deliveryCompany,

                // DTO: trackingNumber (String 그대로 전송 - DTO가 String으로 수정되었다고 가정)
                trackingNumber: temp.trackingNumber,

                // DTO: stat (주문 상태 코드는 Integer 타입이므로, 2로 설정)
                stat: 2

                // 🚨 배송지 관련 불필요 필드 (name, zip, addr, addr2, req)는 제거함
            };

            // 3. 유효성 검사
            if (!postData.deliveryCompany || postData.deliveryCompany === '') {
                alert('택배사를 선택해 주세요.');
                return;
            }
            if (!postData.trackingNumber || postData.trackingNumber.trim() === '') {
                alert('운송장 번호를 입력해 주세요.');
                return;
            }
            if (!postData.order_number || postData.order_number.trim() === '') {
                alert('주문 번호가 누락되었습니다. 다시 시도해 주세요.');
                return;
            }

            // 💡 최종 전송 데이터 확인
            // console.log("전송할 최종 JSON 데이터:", JSON.stringify(postData));

            if (confirm(`주문번호 ${postData.order_number}에 운송장 정보를 등록하고 상태를 '배송준비'상태로 변경하시겠습니까?`)) {
                $.ajax({
                    url: CONTEXT_PATH + '/admin/order/delivery-input',
                    type: 'POST',
                    contentType: 'application/json',
                    // DTO 필드명과 타입에 정확히 맞춰진 postData를 전송
                    data: JSON.stringify(postData),
                    success: function(response) {
                        if (response.success) {
                            alert(`주문 ${postData.order_number}이(가) 배송준비중으로 변경되었습니다.`);
                            $('#addressModal').modal('hide');
                            location.reload();
                        } else {
                            alert(`배송 정보 등록 실패: ${response.message || '서버 오류'}`);
                        }
                    },
                    error: function(xhr, status, error) {
                        console.error("배송 정보 등록 오류:", error);
                        alert('시스템 오류가 발생했습니다. (HTTP 400 발생)');
                    }
                });
            }
        });
    });
});