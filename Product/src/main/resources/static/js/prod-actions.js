/* 찜/장바구니/바로구매 버튼 기능 */
const btnWish = document.querySelector(".btn.wish");
const btnCart = document.querySelector(".btn.cart");
const btnBuy  = document.querySelector(".btn.buy");

btnWish.addEventListener("click", () => {
    if (userType === "guest") {
        // 비로그인 → 로그인 페이지
        location.href = "/login";
    } else if (userType === "user") {
        // 로그인 상태 → 하트 토글
        btnWish.classList.toggle("active");
    }
});

btnCart.addEventListener("click", () => {
    if (userType === "guest") {
        location.href = "/login";
    } else {
        location.href = "/cart";
    }
});

btnBuy.addEventListener("click", () => {
    if (userType === "guest") {
        location.href = "/login";
    } else {
        location.href = "/checkout";
    }
});
