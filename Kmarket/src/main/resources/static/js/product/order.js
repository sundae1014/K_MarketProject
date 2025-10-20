/* 주문/결제 - 쿠폰 & 포인트 계산 */
document.addEventListener("DOMContentLoaded", () => {
    const basePriceEl = document.getElementById("basePrice");
    const orderTotalEl = document.querySelector(".order-total strong");
    const currentPointEl = document.getElementById("currentPoint");
    const usePointEl = document.getElementById("usePoint");
    const quantityEl = document.getElementById("quantity");

    // 수량 읽기 함수
    function getQuantity() {
        return parseInt(quantityEl?.value) || 1;
    }

    let basePrice = parseInt(basePriceEl.textContent.replace(/,/g, ""));
    let finalPrice = basePrice;

    let userPoint = parseInt(
        document.getElementById("currentPoint").textContent.replace(/,/g, "")
    ) || 0;

    let couponDiscount = 0;
    let usedPoint = 0;

    // 최종 결제 정보
    function updateTotal() {
        const summaryBox = document.querySelector(".order-summary ul");

        // 상품 수량 (임시: 1세트)
        let quantity = getQuantity();

        summaryBox.innerHTML = `
        <li><span>상품수</span><span><strong>${quantity}</strong></span></li>
        <li><span>상품금액</span><span><strong>${basePrice.toLocaleString()}</strong>원</span></li>
        <li><span>쿠폰할인</span><span><strong>-${couponDiscount.toLocaleString()}</strong>원</span></li>
        <li><span>포인트사용</span><span><strong>-${usedPoint.toLocaleString()}</strong>원</span></li>
        <li><span>배송비</span><span><strong>+0</strong>원</span></li>
        <li><span>적립 포인트</span><span><strong>${Math.floor(finalPrice * 0.01).toLocaleString()}</strong>원</span></li>
    `;
        orderTotalEl.textContent = finalPrice.toLocaleString();
        currentPointEl.textContent = userPoint.toLocaleString();
    }

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
        finalPrice = basePrice - couponDiscount - usedPoint; // ✅ 수정됨
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

    // =========================
    // ✅ 쿠폰 선택 시 할인 적용 (최종 수정)
    // =========================
    document.querySelectorAll("input[name='coupon']").forEach(chk => {
        chk.addEventListener("change", e => {
            // 하나만 선택
            document.querySelectorAll("input[name='coupon']").forEach(c => {
                if (c !== e.target) c.checked = false;
            });

            if (e.target.checked) {
                // "10%" 또는 "0.1" 모두 처리
                let rateRaw = (e.target.dataset.discount || "").toString().trim();
                let discountRate = rateRaw.endsWith('%')
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
});

// =========================
// ✅ 결제방법 선택 활성화 기능
// =========================
document.addEventListener("DOMContentLoaded", () => {
    const payButtons = document.querySelectorAll(".pay-btn");

    payButtons.forEach(btn => {
        btn.addEventListener("click", () => {
            // 모든 버튼에서 active 제거
            payButtons.forEach(b => b.classList.remove("active"));

            // 클릭된 버튼에 active 추가
            btn.classList.add("active");

            // ✅ 시각적 효과 (선택 시 살짝 확대 + 그림자)
            btn.animate(
                [
                    { transform: "scale(1)", boxShadow: "none" },
                    { transform: "scale(1.05)", boxShadow: "0 2px 8px rgba(51,106,253,0.3)" },
                    { transform: "scale(1)", boxShadow: "none" }
                ],
                { duration: 300, easing: "ease-out" }
            );
        });
    });
});

