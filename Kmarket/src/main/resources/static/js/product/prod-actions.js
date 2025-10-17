/* 찜/장바구니/바로구매 버튼 기능 */
const btnWish = document.querySelector(".btn.wish");
const btnCart = document.querySelector(".btn.cart");
const btnBuy  = document.querySelector(".btn.buy");

/* 찜 기능 */
btnWish.addEventListener("click", () => {
    if (userType === "guest") {
        // 비로그인 → 로그인 페이지
        location.href = "/kmarket/member/login";
    } else if (userType === "user") {
        // 로그인 상태 → 하트 토글
        btnWish.classList.toggle("active");
    }
});

/* 장바구니 기능 (서버 연동 버전) */
btnCart.addEventListener("click", async () => {
    if (userType === "guest") {
        if (confirm("로그인이 필요한 서비스입니다.\n로그인 페이지로 이동하시겠습니까?")) {
            location.href = "/kmarket/member/login";
        }
        return;
    }

    // 옵션 선택 확인
    const optionEl = document.querySelector("#opt");
    const selectedOpt = optionEl.options[optionEl.selectedIndex];
    if (!selectedOpt.value) {
        alert("옵션을 선택해주세요!");
        return;
    }

    // 수량, 상품 정보
    const prodNumber = btnCart.dataset.prodNumber; // ✅ 상품번호
    const quantity = parseInt(document.querySelector(".prod-selected input")?.value || 1);
    const optName = selectedOpt.text;

    try {
        // 서버에 장바구니 등록 요청
        const res = await fetch("/kmarket/product/cart/add", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                prod_number: prodNumber,
                quantity: quantity,
                opt_name: optName
            })
        });

        const result = await res.text();
        if (result === "success") {
            if (confirm("상품이 장바구니에 담겼습니다.\n장바구니로 이동하시겠습니까?")) {
                location.href = "/kmarket/product/cart";
            }
        } else {
            alert("장바구니 담기 중 오류가 발생했습니다.");
        }
    } catch (err) {
        console.error(err);
        alert("서버와 통신 중 오류가 발생했습니다.");
    }
});

/* 바로구매 기능 */
btnBuy.addEventListener("click", () => {
    if (userType === "guest") {
        location.href = "/kmarket/member/login";
    } else {
        location.href = "/kmarket/member/checkout";
    }
});
