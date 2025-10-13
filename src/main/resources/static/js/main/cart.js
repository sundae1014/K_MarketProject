// =========================
// 샘플 데이터 (추후 백엔드 연동 예정)
// =========================
let cartData = [
    { id: 1, name: "동원 튜나리챔 세트1", price: 69900, discount: 0.49, qty: 1 },
    { id: 2, name: "동원 튜나리챔 세트2", price: 69900, discount: 0.49, qty: 1 }
];

// =========================
// 유틸 함수
// =========================
function formatPrice(num) {
    return num.toLocaleString() + "원";
}
function getPoint(price) {
    return Math.floor(price * 0.01); // 기본 1% 적립
}

// =========================
// 장바구니 렌더링
// =========================
function renderCart() {
    const list = document.querySelector(".cart-list");
    list.innerHTML = "";

    cartData.forEach(item => {
        const discountPrice = Math.floor(item.price * (1 - item.discount));
        const div = document.createElement("div");
        div.className = "cart-item";
        div.innerHTML = `
      <input type="checkbox" class="chk-item" data-id="${item.id}" checked>
      <img src="/cupang/images/Hit-product_1.png" alt="상품">
      <div class="cart-info">
        <p class="cart-name">${item.name}
          <a href="#" class="btn-remove" data-id="${item.id}">삭제</a>
        </p>
        <p class="cart-sub">내일 도착 보장 <span class="badge">무료배송</span></p>
        <div class="cart-price">
          <span class="discount">${Math.round(item.discount * 100)}%</span>
          <strong class="sale">${formatPrice(discountPrice)}</strong>
          <del class="regular">${formatPrice(item.price)}</del>
        </div>
        <div class="cart-point">
          <p>최대 ${formatPrice(getPoint(discountPrice))} 적립</p>
        </div>
        <div class="cart-qty">
          <button class="minus" data-id="${item.id}">-</button>
          <input type="text" value="${item.qty}" data-id="${item.id}">
          <button class="plus" data-id="${item.id}">+</button>
        </div>
      </div>
    `;
        list.appendChild(div);
    });

    renderSelectAll();
    updateSummary();

    // 개별 체크 이벤트 연결
    document.querySelectorAll(".chk-item").forEach(chk => {
        chk.addEventListener("change", () => {
            updateSelectedCount();
            updateSummary();
        });
    });
}

// =========================
// 전체 선택 영역
// =========================
function updateSelectedCount() {
    const total = cartData.length;
    const checked = document.querySelectorAll(".chk-item:checked").length;
    document.querySelector(".cart-select-all p").textContent = `전체 선택(${checked}/${total})`;
}

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
        updateSelectedCount();
        updateSummary();
    });

    // 선택 삭제
    div.querySelector("#btnRemoveSelected").addEventListener("click", () => {
        const remain = cartData.filter(item => {
            const chk = document.querySelector(`.chk-item[data-id="${item.id}"]`);
            return !chk || !chk.checked;
        });
        cartData = remain;
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
        const id = parseInt(chk.dataset.id);
        const item = cartData.find(i => i.id === id);
        if (!item) return;

        const discountPrice = Math.floor(item.price * (1 - item.discount));
        totalCount += item.qty;
        totalPrice += item.price * item.qty;
        totalDiscount += (item.price - discountPrice) * item.qty;
        totalPoint += getPoint(discountPrice) * item.qty;
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

    document.querySelector(".btn-order").addEventListener("click", () => {
        const orderItems = checked.map(chk => {
            const id = parseInt(chk.dataset.id);
            return cartData.find(i => i.id === id);
        });
        localStorage.setItem("orderData", JSON.stringify(orderItems));
        window.location.href = "/cupang/prodOrder";
    });
}

// =========================
// 이벤트 위임 (삭제 / 수량변경)
// =========================
document.addEventListener("click", e => {
    if (e.target.classList.contains("btn-remove")) {
        e.preventDefault();
        const id = parseInt(e.target.dataset.id);
        cartData = cartData.filter(i => i.id !== id);
        renderCart();
    }
    if (e.target.classList.contains("plus") || e.target.classList.contains("minus")) {
        const id = parseInt(e.target.dataset.id);
        const item = cartData.find(i => i.id === id);
        if (!item) return;
        if (e.target.classList.contains("plus")) item.qty++;
        if (e.target.classList.contains("minus") && item.qty > 1) item.qty--;
        renderCart();
    }
});

// =========================
// 초기 실행
// =========================
document.addEventListener("DOMContentLoaded", () => {
    renderCart();
});
