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
const getPoint = price => Math.floor(price * 0.01);
const getUnitBase = item => (item.price + (item.opt_price || 0));
const getUnitSale = item => Math.floor(getUnitBase(item) * (1 - (item.discount || 0) / 100));
const getTotals = item => {
    const unitBase = getUnitBase(item);
    const unitSale = getUnitSale(item);
    return {
        regular: unitBase * item.quantity,
        sale: unitSale * item.quantity,
        discountAmt: (unitBase - unitSale) * item.quantity
    };
};

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
        const optPrice = item.opt_price || 0;
        const unitBase = item.price + optPrice;
        const unitSale = Math.floor(unitBase * (1 - (item.discount || 0) / 100));
        const totalBase = unitBase * item.quantity;
        const totalSale = unitSale * item.quantity;
        const totalPoint = getPoint(unitSale) * item.quantity;

        const optionHtml = item.opt_name
            ? `<p class="product-option">${item.quantity}개, ${item.opt_name} (${formatPrice(optPrice)})</p>`
            : `<p class="product-option">${item.quantity}개</p>`;

        const div = document.createElement("div");
        div.className = "cart-item";
        div.innerHTML = `
        <input type="checkbox" class="chk-item" data-id="${item.cart_number}" checked>
        <img src="/kmarket${item.img_1}" alt="${item.prod_name}">
        <div class="cart-info">
            <p class="cart-name">${item.prod_name}
                <a href="#" class="btn-remove" data-id="${item.cart_number}">삭제</a>
            </p>
            ${optionHtml}
            <div class="cart-price">
                <span class="discount">${item.discount}%</span>
                <strong class="sale">${formatPrice(totalSale)}</strong>
                <del class="regular">${formatPrice(totalBase)}</del>
            </div>
            <div class="cart-point">
                <p>최대 ${formatPrice(totalPoint)} 적립</p>
            </div>
            <div class="cart-qty">
                <button class="minus" data-id="${item.cart_number}">-</button>
                <input type="text" value="${item.quantity}" data-id="${item.cart_number}">
                <button class="plus" data-id="${item.cart_number}">+</button>
            </div>
        </div>`;
        list.appendChild(div);
    });

    renderSelectAll();
    updateSummary(); // ✅ 단 한 번만 호출
}

// =========================
// 전체 선택 / 삭제 기능
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

    // ✅ 전체 선택/해제
    div.querySelector("#chkAll").addEventListener("change", e => {
        const state = e.target.checked;
        document.querySelectorAll(".chk-item").forEach(chk => chk.checked = state);
        updateSummary();
    });

    // ✅ 선택 삭제
    div.querySelector("#btnRemoveSelected").addEventListener("click", async () => {
        const selected = [...document.querySelectorAll(".chk-item:checked")].map(chk => chk.dataset.id);
        if (selected.length === 0) {
            alert("삭제할 상품을 선택하세요!");
            return;
        }

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
    let totalCount = 0, totalRegular = 0, totalDiscountAmt = 0, totalPoint = 0;

    checked.forEach(chk => {
        const item = cartData.find(i => i.cart_number == chk.dataset.id);
        if (!item) return;
        const { regular, discountAmt } = getTotals(item);
        totalCount += item.quantity;
        totalRegular += regular;
        totalDiscountAmt += discountAmt;
        totalPoint += getPoint(getUnitSale(item)) * item.quantity;
    });

    const final = totalRegular - totalDiscountAmt;

    const summary = document.querySelector(".cart-summary");
    summary.innerHTML = `
        <h3>전체 합계</h3>
        <ul>
            <li><span>상품수</span><span><strong>${totalCount}</strong></span></li>
            <li><span>상품금액</span><span><strong>${formatPrice(totalRegular)}</strong></span></li>
            <li><span>할인금액</span><span><strong>-${formatPrice(totalDiscountAmt)}</strong></span></li>
            <li><span>배송비</span><span><strong>+0원</strong></span></li>
            <li><span>적립 포인트</span><span><strong>${formatPrice(totalPoint)}</strong></span></li>
        </ul>
        <p class="cart-total"><strong>${formatPrice(final)}</strong></p>
        <button class="btn-order">총 ${totalCount}개 상품 구매하기</button>`;
}

// =========================
// 주문 페이지로 이동
// =========================
document.addEventListener("click", e => {
    if (e.target.classList.contains("btn-order")) {
        const selected = [...document.querySelectorAll(".chk-item:checked")]
            .map(chk => chk.dataset.id);

        if (selected.length === 0) {
            alert("구매할 상품을 선택하세요!");
            return;
        }

        const cartNumbers = selected.join(",");
        window.location.href = `/kmarket/order/form?cartNumbers=${cartNumbers}`;
    }
});

// =========================
// 초기 실행
// =========================
document.addEventListener("DOMContentLoaded", fetchCartData);
