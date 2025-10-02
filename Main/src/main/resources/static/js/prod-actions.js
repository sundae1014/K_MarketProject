/* 찜/장바구니/바로구매 버튼 기능 */
const btnWish = document.querySelector(".btn.wish");
const btnCart = document.querySelector(".btn.cart");
const btnBuy  = document.querySelector(".btn.buy");

/* 찜 기능 */
btnWish.addEventListener("click", () => {
    if (userType === "guest") {
        // 비로그인 → 로그인 페이지
        location.href = "/login";
    } else if (userType === "user") {
        // 로그인 상태 → 하트 토글
        btnWish.classList.toggle("active");
    }
});

/* 장바구니 기능 */
btnCart.addEventListener("click", () => {
    if (userType === "guest") {
        if (confirm("로그인이 필요한 서비스입니다.\n로그인 페이지로 이동하시겠습니까?")) {
            location.href = "/cupang/login"; // context-path 포함
        }
    } else {
        // 옵션 데이터 가져오기
        const optionEl = document.querySelector("#opt");
        const selectedOpt = optionEl.options[optionEl.selectedIndex];
        if (!selectedOpt.value) {
            alert("옵션을 선택해주세요!");
            return;
        }

        const name = document.querySelector(".prod-title").textContent;
        const qty = parseInt(document.querySelector(".prod-selected input").value) || 1;
        const price = parseInt(selectedOpt.dataset.price);
        const discount = 0;

        // 기존 장바구니 불러오기
        let cartData = JSON.parse(localStorage.getItem("cartData")) || [];

        // 중복 여부 확인
        const exist = cartData.find(i => i.id === selectedOpt.value);
        if (exist) {
            exist.qty += qty;
        } else {
            cartData.push({
                id: selectedOpt.value,
                name: name + " - " + selectedOpt.text,
                price: price,
                discount: discount,
                qty: qty
            });
        }

        // localStorage 갱신
        localStorage.setItem("cartData", JSON.stringify(cartData));

        // 알림창
        if (confirm("상품이 장바구니에 담겼습니다.\n장바구니로 이동하시겠습니까?")) {
            location.href = "/cupang/prodCart";  // ✅ context-path 포함
        }
    }
});

/* 바로구매 기능 */
btnBuy.addEventListener("click", () => {
    if (userType === "guest") {
        location.href = "/login";
    } else {
        location.href = "/checkout";
    }
});
