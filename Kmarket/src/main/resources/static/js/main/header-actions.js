/* 헤더 바로가기 버튼 */
document.addEventListener("DOMContentLoaded", () => {
    const headerCart = document.getElementById("header-cart");

    if (!headerCart) return;

    headerCart.addEventListener("click", () => {
        if (userType === "guest") {
            if (confirm("로그인이 필요한 서비스입니다.\n로그인 페이지로 이동하시겠습니까?")) {
                location.href = "/kmarket/member/login";
            }
        } else if (userType === "user") {
            location.href = "/kmarket/product/cart";
        }
    });
});
