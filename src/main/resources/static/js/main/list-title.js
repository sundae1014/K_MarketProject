/* 2차 카테고리 > 상품 목록 타이틀 갱신 기능 */
document.addEventListener("DOMContentLoaded", () => {
    const params = new URLSearchParams(window.location.search);
    const lv1 = params.get("lv1");
    const lv2 = params.get("lv2");

    // 타이틀 표시
    const categoryTitle = document.getElementById("category-title");
    if (lv2 && categoryTitle) {
        categoryTitle.textContent = lv2;
    } else if (!lv1 && !lv2 && categoryTitle) {
        categoryTitle.textContent = "상품 검색 결과";
    }

    // 경로 표시
    const navPath = document.querySelector(".nav-path ol");
    if (navPath) {
        if (lv1 && lv2) {
            navPath.innerHTML = `
                <li><a href="/kmarket/">HOME</a></li>
                <li><a href="/kmarket/product/list?lv1=${encodeURIComponent(lv1)}">${lv1}</a></li>
                <li><a href="/kmarket/product/list?lv1=${encodeURIComponent(lv1)}&lv2=${encodeURIComponent(lv2)}">${lv2}</a></li>
            `;
        } else {
            // 검색결과 페이지
            navPath.innerHTML = `
                <li><a href="/kmarket/">HOME</a></li>
                <li><a href="#">상품 검색 결과</a></li>
            `;
        }
    }
});
