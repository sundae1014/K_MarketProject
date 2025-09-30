/* 2차 카테고리 > 상품 목록 타이틀 갱신 기능 */
document.addEventListener("DOMContentLoaded", () => {
    const params = new URLSearchParams(window.location.search);
    const lv1 = params.get("lv1");
    const lv2 = params.get("lv2");

    // 타이틀 세팅
    const categoryTitle = document.getElementById("category-title");
    if (lv2 && categoryTitle) {
        categoryTitle.textContent = lv2;
    }

    // 경로(nav-path)도 동적으로 구성
    const navPath = document.querySelector(".nav-path ol");
    if (lv1 && lv2 && navPath) {
        navPath.innerHTML = `
      <li><a href="/">HOME</a></li>
      <li><a href="#">${lv1}</a></li>
      <li><a href="#">${lv2}</a></li>
    `;
    }
});