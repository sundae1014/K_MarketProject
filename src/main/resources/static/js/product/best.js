/* 베스트상품 온앤오프 & 스크롤 기능 */
async function fetchBestProducts() {
    try {
        const res = await fetch('/api/best-products');
        if (!res.ok) throw new Error('API Error');
        return await res.json();
    } catch (e) {
        console.error(e);
        return [];
    }
}

function fmt(n) {
    return n.toLocaleString('ko-KR') + "원";
}

function createBestCard(d, rank) {
    const a = document.createElement('a');
    a.href = `/product/${d.id}`;
    a.className = 'best-card';
    a.innerHTML = `
    <div class="best-thumb">
      <img src="${d.image}" alt="${d.name}">
    </div>
    <div class="best-info">
      <p class="best-name">${d.name}</p>
      <div class="best-prices">
        <span class="best-discount">${d.discount}%</span>
        <del class="best-regular">${fmt(d.regular)}</del>
        <strong class="best-sale">${fmt(d.sale)}</strong>
      </div>
    </div>
  `;
    return a;
}

document.addEventListener('DOMContentLoaded', async () => {
    // let bestList = await fetchBestProducts();

    // 더미 데이터
    const bestList = [
        { id: 101, name: "샘플 상품1", regular: 10000, sale: 8000, discount: 20, image: "/cupang/images/Hit-product_1.png" },
        { id: 102, name: "샘플 상품2", regular: 20000, sale: 15000, discount: 25, image: "/cupang/images/Hit-product_2.png" },
        { id: 103, name: "샘플 상품3", regular: 30000, sale: 27000, discount: 10, image: "/cupang/images/Hit-product_3.png" },
        { id: 104, name: "샘플 상품4", regular: 40000, sale: 25000, discount: 38, image: "/cupang/images/Hit-product_4.png" },
        { id: 105, name: "샘플 상품5", regular: 50000, sale: 30000, discount: 40, image: "/cupang/images/Hit-product_5.png" },
    ];

    const list = document.getElementById('best-num-list');
    let openCard = null; // 현재 열려있는 카드 참조
    let lastBtn  = null; // 마지막으로 클릭된 버튼

    list.addEventListener('click', (e) => {
        const btn = e.target.closest('button[data-idx]');
        if (!btn) return;

        const idx = parseInt(btn.dataset.idx, 10);
        const li = btn.parentElement;

        // 같은 버튼 다시 누르면 접기
        if (openCard && lastBtn === btn) {
            openCard.remove();
            openCard = null;
            lastBtn.classList.remove('active');
            lastBtn = null;
            return; // 여기서 블록 닫기
        }

        // 기존 카드 닫기
        if (openCard) openCard.remove();

        // 새 카드 열기 (해당 버튼 li 바로 아래)
        const d = bestList[idx];
        const card = createBestCard(d, idx + 1);
        li.insertAdjacentElement('afterend', card);
        openCard = card;

        // 버튼 active 토글
        list.querySelectorAll('button').forEach(b =>
            b.classList.toggle('active', b === btn)
        );

        // 마지막 버튼 기억
        lastBtn = btn;
    });
});
