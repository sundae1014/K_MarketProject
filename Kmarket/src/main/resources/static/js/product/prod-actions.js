/* 찜/장바구니/바로구매 버튼 기능 */
document.addEventListener("DOMContentLoaded", () => {
    const btnWish = document.querySelector(".btn.wish");
    const btnCart = document.querySelector(".btn.cart");
    const btnBuy  = document.querySelector(".btn.buy");

    /* 🟡 공통: 비로그인 시 처리 */
    function requireLogin() {
        if (userType === "guest") {
            if (confirm("로그인이 필요한 서비스입니다.\n로그인 페이지로 이동하시겠습니까?")) {
                location.href = "/kmarket/member/login";
            }
            return true; // 로그인 필요하므로 이후 코드 중단
        }
        return false;
    }

    /* ❤️ 찜 기능 */
    btnWish?.addEventListener("click", () => {
        if (requireLogin()) return;
        btnWish.classList.toggle("active");
        // TODO: 서버 연동 시 여기서 fetch("/kmarket/wish/add", { ... })
    });

    /* 🛒 장바구니 기능 */
    btnCart?.addEventListener("click", async () => {
        if (requireLogin()) return;

        const optionEl = document.querySelector("#opt");
        const selectedOpt = optionEl.options[optionEl.selectedIndex];
        if (!selectedOpt.value) {
            alert("옵션을 선택해주세요!");
            return;
        }

        const prodNumber = btnCart.dataset.prodNumber;
        const quantity = parseInt(document.querySelector(".prod-selected input")?.value || 1);
        const optName = selectedOpt.text;

        try {
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

    /* 💳 바로구매 기능 */
    btnBuy?.addEventListener("click", () => {
        if (requireLogin()) return;

        // 옵션 확인
        const optionEl = document.querySelector("#opt");
        const selectedOpt = optionEl.options[optionEl.selectedIndex];
        if (!selectedOpt.value) {
            alert("옵션을 선택해주세요!");
            return;
        }

        const prodNumber = document.querySelector("#btnAddCart").dataset.prodNumber;
        const quantityEl = document.querySelector(".prod-selected input[name='quantity']");
        const quantity = quantityEl ? quantityEl.value : 1;

        // ✅ 주문 페이지로 이동 (수량과 상품번호 전달)
        window.location.href = `/kmarket/order/form?prod_number=${prodNumber}&quantity=${quantity}`;
    });
});

