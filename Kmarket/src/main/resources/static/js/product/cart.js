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
const getPoint = (price, quantity = 1) => Math.floor((price * quantity) * 0.01);

// 기본가(정상가): product.price 사용 (옵션가 포함 시 price + opt 추가금)
const getUnitBase = item => {
    return item.price; // 상품 원가(정상가)
};

// 할인가: DB에서 이미 할인된 opt_price 내려오면 그대로 사용
// opt_price가 없다면 정상가에 할인률 적용
const getUnitSale = item => {
    if (item.opt_price && item.opt_price > 0) {
        return item.opt_price; // DB에서 내려온 할인가 그대로 사용
    } else {
        return Math.floor(item.price * (1 - (item.discount || 0) / 100));
    }
};

// ✅ 합계 계산
const getTotals = item => {
    const regular = getUnitBase(item) * item.quantity;      // 정상가 합계
    const sale = getUnitSale(item) * item.quantity;          // 할인가 합계
    const discountAmt = regular - sale;                      // 할인 총액
    return { regular, sale, discountAmt };
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
        const unitBase = getUnitBase(item); // 정상가
        const unitSale = getUnitSale(item); // 할인가
        const totalBase = unitBase * item.quantity;
        const totalSale = unitSale * item.quantity;
        const totalPoint = getPoint(unitSale) * item.quantity;

        const optionHtml = item.opt_name
            ? `<p class="product-option">${item.quantity}개, ${item.opt_name} (${formatPrice(unitSale)})</p>`
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
// 개별 삭제 기능
// =========================
document.addEventListener("click", async (e) => {
    if (e.target.classList.contains("btn-remove")) {
        e.preventDefault(); // a 태그의 기본 동작(페이지 이동) 막기
        const id = e.target.dataset.id;

        if (!confirm("해당 상품을 장바구니에서 삭제하시겠습니까?")) return;

        try {
            const res = await fetch(`/kmarket/product/cart/${id}`, { method: "DELETE" });
            if (!res.ok) throw new Error("삭제 실패");

            // 로컬 데이터에서도 제거
            cartData = cartData.filter(item => item.cart_number != id);
            renderCart();
        } catch (err) {
            console.error(err);
            alert("상품 삭제 중 오류가 발생했습니다.");
        }
    }
});

// =========================
// 장바구니 합계 요약(cart-summary)
// =========================
function updateSummary() {
    const checked = [...document.querySelectorAll(".chk-item:checked")];
    let totalCount = 0, totalRegular = 0, totalDiscountAmt = 0, totalPoint = 0;

    checked.forEach(chk => {
        const item = cartData.find(i => i.cart_number == chk.dataset.id);
        if (!item) return;
        const { regular, discountAmt, sale } = getTotals(item);
        totalCount += item.quantity;
        totalRegular += regular;
        totalDiscountAmt += discountAmt;
        totalPoint += getPoint(getUnitSale(item), item.quantity);
    });

    const final = totalRegular - totalDiscountAmt; // ✅ 정상가 - 할인금액
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

// =========================
// 수량 변경 기능 (+ / - 버튼)
// =========================
document.addEventListener("click", async (e) => {
    if (e.target.classList.contains("plus") || e.target.classList.contains("minus")) {
        const id = e.target.dataset.id;
        const input = document.querySelector(`input[data-id="${id}"]`);
        let qty = parseInt(input.value);

        if (e.target.classList.contains("plus")) qty++;
        if (e.target.classList.contains("minus")) qty = Math.max(1, qty - 1);

        input.value = qty;

        // ✅ cartData 내부 수량도 갱신
        const item = cartData.find(i => i.cart_number == id);
        if (item) item.quantity = qty;

        // ✅ 서버에도 반영 (선택사항: DB 실시간 반영)
        await fetch(`/kmarket/product/cart/updateQty`, {
            method: "PATCH", // ✅ 컨트롤러에 맞춰 PATCH로 변경
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ cart_number: id, quantity: qty })
        });

        // ✅ 화면 갱신
        renderCart();
    }
});

// =========================
// 직접 입력 시(텍스트박스) 수량 변경
// =========================
document.addEventListener("change", async (e) => {
    if (e.target.matches("input[data-id]")) {
        const id = e.target.dataset.id;
        let qty = parseInt(e.target.value);
        if (isNaN(qty) || qty < 1) qty = 1;
        e.target.value = qty;

        const item = cartData.find(i => i.cart_number == id);
        if (item) item.quantity = qty;

        await fetch(`/kmarket/product/cart/update`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ cart_number: id, quantity: qty })
        });

        renderCart();
    }
});

