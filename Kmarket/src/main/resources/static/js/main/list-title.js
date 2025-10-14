document.addEventListener("DOMContentLoaded", () => {
    const params = new URLSearchParams(window.location.search);
    const lv1 = params.get("lv1");
    const lv2 = params.get("lv2");

    const categoryTitle = document.getElementById("category-title");
    const navPath = document.querySelector(".nav-path ol");

    if (!categoryTitle || !navPath) return;

    // ✅ URL에 lv1, lv2가 있으면 JS로 덮어쓰기
    if (lv1 && lv2) {
        categoryTitle.textContent = lv2;
        navPath.innerHTML = `
            <li><a href="/kmarket/">HOME</a></li>
            <li><a href="#">${lv1}</a></li>
            <li><a href="#">${lv2}</a></li>
        `;
    }
});