/* 상품 상세 페이지 옵션 기능 */
const selectOpt = document.getElementById("opt");
const selectedList = document.getElementById("selected-list");
const totalPrice = document.getElementById("total-price");
const prodTotal = document.querySelector(".prod-total");

let selections = []; // 선택된 옵션 저장

selectOpt.addEventListener("change", () => {
    const value = selectOpt.value;
    const text = selectOpt.options[selectOpt.selectedIndex].text;
    const price = parseInt(selectOpt.options[selectOpt.selectedIndex].dataset.price || 0);

    if (!value) return; // 옵션 선택 안했으면 무시

    // 화살표 숨김 클래스 추가 (선택 후)
    selectOpt.classList.add("selected");

    // 중복 선택 방지
    if (selections.find(item => item.value === value)) return;

    const item = { value, text, price, qty: 1 };
    selections.push(item);
    renderSelections();
});

function renderSelections() {
    selectedList.innerHTML = "";
    let total = 0;

    if (selections.length === 0) {
        selectedList.style.display = "none";
        prodTotal.style.display = "none";
    } else {
        selectedList.style.display = "block";
        prodTotal.style.display = "block";
    }

    selections.forEach((item, idx) => {
        total += item.price * item.qty;

        const div = document.createElement("div");
        div.className = "prod-selected-item";
        div.innerHTML = `
          <p class="item-name">${item.text}</p>
          <div class="item-bottom">
            <div class="qty">
              <button onclick="changeQty(${idx}, -1)">-</button>
              <input type="text" name="quantity" value="${item.qty}" readonly>
              <button onclick="changeQty(${idx}, 1)">+</button>
            </div>
            <strong class="qty-price">${(item.price * item.qty).toLocaleString()}원</strong>
          </div>
        `;
        selectedList.appendChild(div);
    });

    totalPrice.textContent = total.toLocaleString() + "원";
}

function changeQty(idx, delta) {
    selections[idx].qty += delta;
    if (selections[idx].qty <= 0) selections[idx].qty = 1;
    renderSelections();
}
