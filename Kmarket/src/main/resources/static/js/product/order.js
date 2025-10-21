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

    // 단일 상품 주문
    if (basePriceEl) {
        const orig = parseInt(basePriceEl.dataset.original?.replace(/[^0-9]/g, "")) || 0;
        const sale = parseInt(basePriceEl.textContent.replace(/[^0-9]/g, "")) || 0;
        originalPrice = orig;
        discountPrice = orig - sale;
        basePrice = sale;
    }

    // 장바구니 주문
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

    // 최종 결제 정보 갱신
    function updateTotal() {
        const summaryBox = document.querySelector(".order-summary ul");
        let totalQuantity = 0;
        orderItems.forEach(el => {
            const qty = parseInt(el.querySelector(".product-option")?.textContent.match(/(\d+)개/)?.[1]) || 1;
            totalQuantity += qty;
        });
        const totalPoint = getTotalPoint(); // ✅ 장바구니와 동일하게 계산

        summaryBox.innerHTML = `
        <li><span>상품수</span><span><strong>${totalQuantity}</strong></span></li>
        <li><span>상품금액</span><span><strong>${originalPrice.toLocaleString()}</strong>원</span></li>
        <li><span>할인금액</span><span><strong>-${discountPrice.toLocaleString()}</strong>원</span></li>
        <li><span>쿠폰할인</span><span><strong>-${couponDiscount.toLocaleString()}</strong>원</span></li>
        <li><span>포인트사용</span><span><strong>-${usedPoint.toLocaleString()}</strong>원</span></li>
        <li><span>배송비</span><span><strong>+0</strong>원</span></li>
        <li><span>적립 포인트</span><span><strong>${totalPoint.toLocaleString()}</strong>원</span></li>
    `;

        orderTotalEl.textContent = finalPrice.toLocaleString();
        currentPointEl.textContent = userPoint.toLocaleString();
    }

    // =========================
// 포인트 사용 기능
// =========================
    window.applyPoint = function () {
        const inputVal = parseInt(usePointEl.value) || 0;

        // 항상 최신 보유 포인트 읽기 (서버 or 화면 기준)
        userPoint = parseInt(currentPointEl.textContent.replace(/,/g, "")) || userPoint;

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

        // 남은 포인트 계산
        const remaining = userPoint - usedPoint;

        // ✅ 전역 userPoint도 갱신 (이게 핵심!!)
        userPoint = remaining;

        // ✅ UI 반영
        currentPointEl.textContent = remaining.toLocaleString();

        // ✅ 시각 피드백
        currentPointEl.animate(
            [
                { color: "#000" },
                { color: "#336AFD" },
                { color: "#000" },
            ],
            { duration: 600, easing: "ease-in-out" }
        );

        // ✅ 결제 요약 업데이트
        calcFinalPrice();
    };

    // =========================
// ✅ 적립 포인트 계산 (상품별 기준)
// =========================
    function getTotalPoint() {
        let total = 0;
        document.querySelectorAll(".product-info").forEach(el => {
            const sale = parseInt(el.querySelector(".sale")?.textContent.replace(/[^0-9]/g, "")) || 0;
            const qty  = parseInt(el.querySelector(".product-option")?.textContent.match(/(\d+)개/)?.[1]) || 1;
            total += Math.floor((sale / qty) * 0.01 * qty); // 상품별 할인가 × 수량 × 1%
        });
        return total;
    }


    // =========================
    // 최종 금액 계산
    // =========================
    function calcFinalPrice() {
        finalPrice = basePrice - couponDiscount - usedPoint;
        if (finalPrice < 0) finalPrice = 0;
        updateTotal();
        updateProductPoints(); // ✅ 추가 (포인트/쿠폰 적용 후 자동 갱신)
    }
    5000
    // =========================
    // 결제 확인 버튼
    // =========================
    const payBtn = document.querySelector(".btn-order");
    payBtn.addEventListener("click", () => {
        const totalAmount = document.querySelector(".order-total strong").textContent;
        const confirmed = confirm(`총 결제 금액은 ${totalAmount}원 입니다.\n결제하시겠습니까?`);

        if (confirmed) {
            // ✅ 수정 후
            const orderNumber = document.querySelector("input[name='order_number']")?.value
                || new URLSearchParams(location.search).get("order_number")
                || "";
            window.location.href = "/kmarket/order/complete?order_number=" + orderNumber;
        }
    });

    // =========================
    // 쿠폰 선택 시 할인 적용
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
    // 초기 세팅
    // =========================
    updateTotal();
    updateProductPoints();
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

// 개당 단가 동기화 (장바구니 포맷 기준)
function syncUnitOptionPrice() {
    document.querySelectorAll(".product-info").forEach(el => {
        const qty = parseInt(el.querySelector(".product-option")?.dataset.qty) || 1;
        const sale = parseInt(el.querySelector(".sale")?.textContent.replace(/[^0-9]/g, "")) || 0;
        const unit = Math.floor(sale / qty);
        const formatted = unit.toLocaleString();

        const optEl = el.querySelector(".product-option");
        if (optEl && !optEl.textContent.includes("(")) {
            optEl.textContent += ` (${formatted}원)`;
        }
    });
}

document.addEventListener("DOMContentLoaded", syncUnitOptionPrice);

// ✅ 상품 정보 내 최대 적립 포인트 갱신
function updateProductPoints() {
    document.querySelectorAll(".product-info").forEach(el => {
        const sale = parseInt(el.querySelector(".sale")?.textContent.replace(/[^0-9]/g, "")) || 0;
        const qty  = parseInt(el.querySelector(".product-option")?.textContent.match(/(\d+)개/)?.[1]) || 1;
        const point = Math.floor((sale / qty) * 0.01 * qty);
        const pointEl = el.querySelector(".product-point p");
        if (pointEl) pointEl.textContent = `최대 ${point.toLocaleString()}점 적립`;
    });
}

document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("orderForm");
    const btn = document.querySelector(".btn-order");

    btn.addEventListener("click", (e) => {
        // 배송정보 입력값 가져오기
        const name = document.getElementById("name").value.trim();
        const hp = document.getElementById("hp").value.trim();
        const addr = document.getElementById("addr").value.trim();
        const addr2 = document.getElementById("addr2").value.trim();

        if (!name || !hp || !addr) {
            alert("배송 정보를 모두 입력해주세요.");
            e.preventDefault();
            return;
        }

        // hidden input에 복사
        document.getElementById("hiddenReceiver").value = name;
        document.getElementById("hiddenHp").value = hp;
        document.getElementById("hiddenAddr1").value = addr;
        document.getElementById("hiddenAddr2").value = addr2;

        // 선택한 결제수단 (버튼 활성화된 값)
        const activePay = document.querySelector(".pay-btn.active");
        if (activePay) {
            document.getElementById("payment").value = activePay.textContent.trim();
        }
    });
});

