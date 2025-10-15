/* 주문/결제 - 쿠폰 & 포인트 계산 */
document.addEventListener("DOMContentLoaded", () => {
    const couponCheck = document.getElementById("coupon-check");
    const basePriceEl = document.getElementById("basePrice");
    const orderTotalEl = document.querySelector(".order-total strong");
    const currentPointEl = document.getElementById("currentPoint");
    const usePointEl = document.getElementById("usePoint");

    let basePrice = parseInt(basePriceEl.textContent.replace(/,/g, ""));
    let finalPrice = basePrice;
    let userPoint = 7200;
    let couponDiscount = 0;
    let usedPoint = 0;

    // 구매자 정보 더미 데이터 (추후 서버 세션 값으로 대체)
    const buyer = {
        name: "정순권",
        phone: "010-1234-5678",
        address: "부산광역시 해운대구 센텀중앙로"
    };

    document.getElementById("buyerName").value = buyer.name;
    document.getElementById("buyerPhone").value = buyer.phone;
    document.getElementById("buyerAddress").value = buyer.address;

    // 최종 결제 정보
    function updateTotal() {
        const summaryBox = document.querySelector(".order-summary ul");

        // 상품 수량 (임시: 1세트)
        let quantity = 1;
        // 나중에 수량 input이 있으면:
        // let quantity = parseInt(document.querySelector(".product-option-qty").value) || 1;

        summaryBox.innerHTML = `
        <li><span>상품수</span><span><strong>${quantity}</strong></span></li>
        <li><span>상품금액</span><span><strong>${(basePrice * quantity).toLocaleString()}</strong>원</span></li>
        <li><span>쿠폰할인</span><span><strong>-${couponDiscount.toLocaleString()}</strong>원</span></li>
        <li><span>포인트사용</span><span><strong>-${usedPoint.toLocaleString()}</strong>원</span></li>
        <li><span>배송비</span><span><strong>+0</strong>원</span></li>
        <li><span>적립 포인트</span><span><strong>${Math.floor(finalPrice * 0.01).toLocaleString()}</strong>원</span></li>
    `;
        orderTotalEl.textContent = finalPrice.toLocaleString();
        currentPointEl.textContent = userPoint.toLocaleString();
    }


    // 쿠폰 체크 이벤트
    couponCheck.addEventListener("change", function() {
        if (this.checked) {
            const discountRate = parseFloat(this.dataset.discount);
            couponDiscount = Math.floor(basePrice * discountRate);
        } else {
            couponDiscount = 0;
        }
        calcFinalPrice();
    });

    // 포인트 사용
    window.applyPoint = function() {
        const input = parseInt(usePointEl.value) || 0;
        if (input >= 5000 && input <= userPoint) {
            usedPoint = input;
            userPoint -= input;
        } else {
            alert("포인트는 5,000점 이상부터 사용 가능합니다.");
            usedPoint = 0;
        }
        calcFinalPrice();
    };

    function calcFinalPrice() {
        finalPrice = basePrice - couponDiscount - usedPoint;
        if (finalPrice < 0) finalPrice = 0;
        updateTotal();
    }

    // 결제 확인 btn 이벤트
    const payBtn = document.querySelector(".btn-order");
    payBtn.addEventListener("click", () => {
        const totalAmount = document.querySelector(".order-total strong").textContent;
        const confirmed = confirm(`총 결제 금액은 ${totalAmount}원 입니다.\n결제하시겠습니까?`);

        if (confirmed) {
            // 주문 완료 페이지로 이동
            window.location.href = "/kmarket/product/complete";
        }
    });

    // 초기 세팅
    updateTotal();
});
