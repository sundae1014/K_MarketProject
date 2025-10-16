/* 베스트상품 온앤오프 & 스크롤 기능 */
function basePath() {
    // 현재 경로가 /kmarket로 시작하면 컨텍스트 패스를 자동 판별
    return (window.location.pathname.startsWith('/kmarket')) ? '/kmarket' : '';
}

function toAbs(path) {
    if (!path) return basePath() + '/images/no-image.png'; // 빈 값 대비
    if (path.startsWith('http')) return path;              // 절대 URL은 그대로
    const b = basePath();
    return b + (path.startsWith('/') ? path : `/${path}`); // /kmarket + /images/.. or /kmarket + images/..
}

async function fetchBestProducts() {
    try {
        const res = await fetch(basePath() + '/api/best-products');
        if (!res.ok) throw new Error('API Error');
        return await res.json();
    } catch (e) {
        console.error(e);
        return [];
    }
}

function fmt(n) {
    if (n == null) return '';
    return n.toLocaleString('ko-KR') + '원';
}

function createBestCard(d, rank) {
    const a = document.createElement('a');
    // 상세 페이지 이동 경로
    a.href = toAbs(`/product/view?prod_number=${encodeURIComponent(d.prod_number)}`);
    a.className = 'best-card';
    a.innerHTML = `
    <div class="best-thumb">
      <img src="${toAbs(d.img_1)}" alt="${d.prod_name || ''}">
    </div>
    <div class="best-info">
      <p class="best-name">${d.prod_name ?? ''}</p>
      <div class="best-prices">
        ${d.discount ? `<span class="best-discount">${d.discount}%</span>` : ''}
        ${d.price != null ? `<del class="best-regular">${fmt(d.price)}</del>` : ''}
        ${d.salePrice != null ? `<strong class="best-sale">${fmt(d.salePrice)}</strong>` : ''}
      </div>
    </div>
  `;
    return a;
}

document.addEventListener('DOMContentLoaded', async () => {
    const bestList = await fetchBestProducts();

    const list = document.getElementById('best-num-list');
    let openCard = null;
    let lastBtn  = null;

    list.addEventListener('click', (e) => {
        const btn = e.target.closest('button[data-idx]');
        if (!btn) return;

        const idx = parseInt(btn.dataset.idx, 10);
        const li = btn.parentElement;

        // 같은 버튼 다시 누르면 닫기
        if (openCard && lastBtn === btn) {
            openCard.remove();
            openCard = null;
            lastBtn.classList.remove('active');
            lastBtn = null;
            return;
        }

        // 기존 카드 제거
        if (openCard) openCard.remove();

        // 새 카드 삽입
        const d = bestList[idx];
        if (!d) return; // 방어코드
        const card = createBestCard(d, idx + 1);
        li.insertAdjacentElement('afterend', card);
        openCard = card;

        // 버튼 active 토글
        list.querySelectorAll('button').forEach(b =>
            b.classList.toggle('active', b === btn)
        );
        lastBtn = btn;
    });
});
