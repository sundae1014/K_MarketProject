/* =========================
   주문/결제 - 쿠폰 & 포인트 계산
   (단일상품 + 장바구니 통합 버전)
========================= */
document.addEventListener("DOMContentLoaded", () => {
    let originalPrice = 0;   // 정상가 합계
    let discountPrice = 0;   // 할인금액 합계
    let basePrice = 0;       // 할인가 (정상가 - 할인금액)
    let finalPrice = 0;      // 최종 결제금액
    let couponDiscount = 0;
    let usedPoint = 0;

    const orderItems = document.querySelectorAll(".product-info");
    const basePriceEl = document.querySelector("#basePrice");
    const orderTotalEl = document.querySelector(".order-total strong");
    const currentPointEl = document.getElementById("currentPoint");
    const usePointEl = document.getElementById("usePoint");

    // ✅ 단일 상품 주문
    if (basePriceEl) {
        const orig = parseInt(basePriceEl.dataset.original?.replace(/[^0-9]/g, "")) || 0;
        const sale = parseInt(basePriceEl.textContent.replace(/[^0-9]/g, "")) || 0;
        originalPrice = orig;
        discountPrice = orig - sale;
        basePrice = sale;
    }

    // ✅ 장바구니 주문
    else if (orderItems.length > 0) {
        orderItems.forEach(el => {
            const orig = parseInt(el.querySelector(".regular")?.textContent.replace(/[^0-9]/g, "")) || 0;
            const sale = parseInt(el.querySelector(".sale")?.textContent.replace(/[^0-9]/g, "")) || 0;
            originalPrice += orig;
            discountPrice += (orig - sale);
            basePrice += sale;
        });
    }

    finalPrice = basePrice;
    let userPoint = parseInt(currentPointEl?.textContent.replace(/,/g, "")) || 0;

    // ✅ 최종 결제 정보 갱신
    function updateTotal() {
        const summaryBox = document.querySelector(".order-summary ul");
        const quantity = orderItems.length > 0 ? orderItems.length : 1;

        summaryBox.innerHTML = `
            <li><span>상품수</span><span><strong>${quantity}</strong></span></li>
            <li><span>상품금액(정상가)</span><span><strong>${originalPrice.toLocaleString()}</strong>원</span></li>
            <li><span>할인금액</span><span><strong>-${discountPrice.toLocaleString()}</strong>원</span></li>
            <li><span>할인적용금액(할인가)</span><span><strong>${basePrice.toLocaleString()}</strong>원</span></li>
            <li><span>쿠폰할인</span><span><strong>-${couponDiscount.toLocaleString()}</strong>원</span></li>
            <li><span>포인트사용</span><span><strong>-${usedPoint.toLocaleString()}</strong>원</span></li>
            <li><span>배송비</span><span><strong>+0</strong>원</span></li>
            <li><span>적립 포인트</span><span><strong>${Math.floor(finalPrice * 0.01).toLocaleString()}</strong>원</span></li>
        `;

        orderTotalEl.textContent = finalPrice.toLocaleString();
        currentPointEl.textContent = userPoint.toLocaleString();
    }

    // =========================
    // ✅ 포인트 사용 기능
    // =========================
    window.applyPoint = function () {
        const inputVal = parseInt(usePointEl.value) || 0;

        // 입력 검증
        if (inputVal < 5000) {
            alert("포인트는 5,000점 이상부터 사용 가능합니다.");
            usePointEl.value = "";
            usedPoint = 0;
            calcFinalPrice();
            return;
        }

        if (inputVal > userPoint) {
            alert("보유 포인트를 초과할 수 없습니다.");
            usePointEl.value = userPoint;
            usedPoint = userPoint;
        } else {
            usedPoint = inputVal;
        }

        // ✅ 차감 후 표시 업데이트
        const remaining = userPoint - usedPoint;
        currentPointEl.textContent = remaining.toLocaleString();

        // 입력 시 강조 효과
        usePointEl.animate(
            [
                { backgroundColor: "#fff" },
                { backgroundColor: "#e8f0fe" },
                { backgroundColor: "#fff" },
            ],
            { duration: 600, easing: "ease-in-out" }
        );

        calcFinalPrice();
    };

    // =========================
    // ✅ 최종 금액 계산
    // =========================
    function calcFinalPrice() {
        finalPrice = basePrice - couponDiscount - usedPoint;
        if (finalPrice < 0) finalPrice = 0;
        updateTotal();
    }

    // =========================
    // ✅ 결제 확인 버튼
    // =========================
    const payBtn = document.querySelector(".btn-order");
    payBtn.addEventListener("click", () => {
        const totalAmount = document.querySelector(".order-total strong").textContent;
        const confirmed = confirm(`총 결제 금액은 ${totalAmount}원 입니다.\n결제하시겠습니까?`);

        if (confirmed) {
            window.location.href = "/kmarket/product/complete";
        }
    });

    // =========================
    // ✅ 쿠폰 선택 시 할인 적용
    // =========================
    document.querySelectorAll("input[name='coupon']").forEach((chk) => {
        chk.addEventListener("change", (e) => {
            // 하나만 선택
            document.querySelectorAll("input[name='coupon']").forEach((c) => {
                if (c !== e.target) c.checked = false;
            });

            if (e.target.checked) {
                let rateRaw = (e.target.dataset.discount || "").toString().trim();
                let discountRate = rateRaw.endsWith("%")
                    ? parseFloat(rateRaw) / 100
                    : parseFloat(rateRaw);

                if (isNaN(discountRate)) discountRate = 0;

                couponDiscount = Math.floor(basePrice * discountRate);
            } else {
                couponDiscount = 0;
            }

            calcFinalPrice();
        });
    });

    // =========================
    // ✅ 초기 세팅
    // =========================
    updateTotal();
});

/* =========================
   결제방법 선택 활성화 기능
========================= */
document.addEventListener("DOMContentLoaded", () => {
    const payButtons = document.querySelectorAll(".pay-btn");

    payButtons.forEach((btn) => {
        btn.addEventListener("click", () => {
            payButtons.forEach((b) => b.classList.remove("active"));
            btn.classList.add("active");

            btn.animate(
                [
                    { transform: "scale(1)", boxShadow: "none" },
                    { transform: "scale(1.05)", boxShadow: "0 2px 8px rgba(51,106,253,0.3)" },
                    { transform: "scale(1)", boxShadow: "none" },
                ],
                { duration: 300, easing: "ease-out" }
            );
        });
    });
});
