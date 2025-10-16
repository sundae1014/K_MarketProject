// =========================
// 전역 변수
// =========================
let cartData = [];

// =========================
// 장바구니 데이터 로드
// =========================
async function fetchCartData() {
    try {
        const res = await fetch("/kmarket/product/cart/list");
        if (!res.ok) throw new Error("서버 응답 오류");
        cartData = await res.json();

        console.log("장바구니 응답 데이터:", cartData); // 🔍 디버깅용
        renderCart();
    } catch (e) {
        console.error(e);
        alert("장바구니 데이터를 불러오는 중 오류가 발생했습니다.");
    }
}

// =========================
// 유틸 함수
// =========================
const formatPrice = num => num.toLocaleString() + "원";
const getPoint = price => Math.floor(price * 0.01); // 1% 적립

// =========================
// 장바구니 렌더링
// =========================
function renderCart() {
    const list = document.querySelector(".cart-list");
    list.innerHTML = "";

    if (!cartData || cartData.length === 0) {
        list.innerHTML = `<p style="padding:20px;">장바구니가 비어 있습니다.</p>`;
        updateSummary();
        return;
    }

    cartData.forEach(item => {
        const discountPrice = Math.floor(item.price * (1 - item.discount / 100));
        const div = document.createElement("div");
        div.className = "cart-item";
        div.innerHTML = `
        <input type="checkbox" class="chk-item" data-id="${item.cart_number}" checked>
        <img src="/kmarket${item.img_1}" alt="${item.prod_name}">
        <div class="cart-info">
            <p class="cart-name">${item.prod_name}
                <a href="#" class="btn-remove" data-id="${item.cart_number}">삭제</a>
            </p>
            <p class="cart-sub">내일 도착 보장 <span class="badge">무료배송</span></p>
            <div class="cart-price">
                <span class="discount">${item.discount}%</span>
                <strong class="sale">${formatPrice(discountPrice)}</strong>
                <del class="regular">${formatPrice(item.price)}</del>
            </div>
            <div class="cart-point">
                <p>최대 ${formatPrice(getPoint(discountPrice))} 적립</p>
            </div>
            <div class="cart-qty">
                <button class="minus" data-id="${item.cart_number}">-</button>
                <input type="text" value="${item.quantity}" data-id="${item.cart_number}">
                <button class="plus" data-id="${item.cart_number}">+</button>
            </div>
        </div>
        `;
        list.appendChild(div);
    });

    renderSelectAll();
    updateSummary();
}

// =========================
// 전체 선택 / 삭제
// =========================
function renderSelectAll() {
    const list = document.querySelector(".cart-list");
    const total = cartData.length;
    const checked = document.querySelectorAll(".chk-item:checked").length;

    const div = document.createElement("div");
    div.className = "cart-select-all";
    div.innerHTML = `
        <input type="checkbox" id="chkAll" ${total === checked ? "checked" : ""}>
        <p>전체 선택(${checked}/${total})</p>
        <button id="btnRemoveSelected">선택삭제</button>
    `;
    list.appendChild(div);

    // 전체 선택/해제
    div.querySelector("#chkAll").addEventListener("change", e => {
        const state = e.target.checked;
        document.querySelectorAll(".chk-item").forEach(chk => chk.checked = state);
        updateSummary();
    });

    // 선택 삭제
    div.querySelector("#btnRemoveSelected").addEventListener("click", async () => {
        const selected = [...document.querySelectorAll(".chk-item:checked")].map(chk => chk.dataset.id);
        for (const id of selected) {
            await fetch(`/kmarket/product/cart/${id}`, { method: "DELETE" });
        }
        cartData = cartData.filter(i => !selected.includes(String(i.cart_number)));
        renderCart();
    });
}

// =========================
// 합계 계산
// =========================
function updateSummary() {
    const checked = [...document.querySelectorAll(".chk-item:checked")];
    let totalCount = 0, totalPrice = 0, totalDiscount = 0, totalPoint = 0;

    checked.forEach(chk => {
        const item = cartData.find(i => i.cart_number == chk.dataset.id);
        if (!item) return;
        const discountPrice = Math.floor(item.price * (1 - item.discount / 100));
        totalCount += item.quantity;
        totalPrice += item.price * item.quantity;
        totalDiscount += (item.price - discountPrice) * item.quantity;
        totalPoint += getPoint(discountPrice) * item.quantity;
    });

    document.querySelector(".cart-summary").innerHTML = `
        <h3>전체 합계</h3>
        <ul>
            <li><span>상품수</span><span><strong>${totalCount}</strong></span></li>
            <li><span>상품금액</span><span><strong>${formatPrice(totalPrice)}</strong></span></li>
            <li><span>할인금액</span><span><strong>-${formatPrice(totalDiscount)}</strong></span></li>
            <li><span>배송비</span><span><strong>+0원</strong></span></li>
            <li><span>적립 포인트</span><span><strong>${formatPrice(totalPoint)}</strong></span></li>
        </ul>
        <p class="cart-total"><strong>${formatPrice(totalPrice - totalDiscount)}</strong></p>
        <button class="btn-order">총 ${totalCount}개 상품 구매하기</button>
    `;
}

// =========================
// 수량 변경 / 삭제 이벤트
// =========================
document.addEventListener("click", e => {
    const id = e.target.dataset.id;

    if (e.target.classList.contains("btn-remove")) {
        e.preventDefault();
        if (!confirm("이 상품을 장바구니에서 삭제할까요?")) return;
        fetch(`/kmarket/product/cart/${id}`, { method: "DELETE" })
            .then(res => res.text())
            .then(result => {
                if (result === "deleted") {
                    cartData = cartData.filter(i => i.cart_number != id);
                    renderCart();
                }
            });
    }

    if (e.target.classList.contains("plus") || e.target.classList.contains("minus")) {
        const item = cartData.find(i => i.cart_number == id);
        if (!item) return;

        if (e.target.classList.contains("plus")) item.quantity++;
        if (e.target.classList.contains("minus") && item.quantity > 1) item.quantity--;

        fetch("/kmarket/product/cart/updateQty", {
            method: "PATCH",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ cart_number: id, quantity: item.quantity })
        });

        renderCart();
    }
});

// =========================
// 초기 실행
// =========================
document.addEventListener("DOMContentLoaded", fetchCartData);
