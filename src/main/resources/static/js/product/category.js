/* 전체 카테고리 구현 기능 */
document.addEventListener("DOMContentLoaded", () => {
    const toggleBtn = document.getElementById("category-toggle");
    const drawer = document.getElementById("category-drawer");
    const lv1 = document.getElementById("category-drawer-lv1");
    const lv2 = document.getElementById("category-drawer-lv2");

    if (!toggleBtn || !drawer) return;

    // 1차 카테고리 렌더링
    Object.keys(CATEGORY_DATA).forEach((name, index) => {
        const li = document.createElement("li");
        li.textContent = name;
        li.dataset.key = name;
        if (index === 0) li.classList.add("is-active");
        lv1.appendChild(li);
    });

    // 2차 카테고리 렌더링 (DB용)
    function renderLv2(key) {
        lv2.innerHTML = "";
        (CATEGORY_DATA[key] || []).forEach(item => {
            const a = document.createElement("a");
            a.textContent = item.name;

            // 실제 카테고리 이동 링크
            a.href = `/kmarket/product/list?lv1=${encodeURIComponent(key)}&lv2=${encodeURIComponent(item.name)}&cate_cd=${item.code}`;

            lv2.appendChild(a);
        });
    }
    renderLv2(Object.keys(CATEGORY_DATA)[0]);

    // hover 시 2차 카테고리 갱신
    lv1.addEventListener("mouseover", e => {
        const item = e.target.closest("li");
        if (!item) return;
        lv1.querySelectorAll("li").forEach(li => li.classList.remove("is-active"));
        item.classList.add("is-active");
        renderLv2(item.dataset.key);
    });

    // 토글 버튼 (햄버거)
    function setIcon(isOpen) {
        const iconWrap = toggleBtn.querySelector(".menu-icon");
        iconWrap.innerHTML = isOpen
            ? '<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"><line x1="6" y1="6" x2="18" y2="18"/><line x1="18" y1="6" x2="6" y2="18"/></svg>'
            : '<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"><line x1="4" y1="6" x2="20" y2="6"/><line x1="4" y1="12" x2="20" y2="12"/><line x1="4" y1="18" x2="20" y2="18"/></svg>';
    }

    toggleBtn.addEventListener("click", () => {
        const isOpen = drawer.classList.toggle("category-drawer_open");
        toggleBtn.classList.toggle("is-open", isOpen);
        toggleBtn.setAttribute("aria-expanded", String(isOpen));
        setIcon(isOpen);
    });

    document.addEventListener("click", e => {
        if (!e.target.closest("#category-drawer") && !e.target.closest("#category-toggle")) {
            drawer.classList.remove("category-drawer_open");
            toggleBtn.classList.remove("is-open");
            toggleBtn.setAttribute("aria-expanded", "false");
            setIcon(false);
        }
    });
});