/* 기준별 목록 정렬 기능 */
document.addEventListener("DOMContentLoaded", () => {
    const pathItems = document.querySelectorAll(".nav-path li");
    const categoryTitle = document.getElementById("category-title");
    if (pathItems.length > 0 && categoryTitle) {
        categoryTitle.textContent = pathItems[pathItems.length - 1].textContent;
    }

    // 정렬 탭 활성화
    const tabs = document.querySelectorAll(".sort-tabs a");
    tabs.forEach(a => {
        a.addEventListener("click", () => {
            tabs.forEach(x => x.classList.remove("on"));
            a.classList.add("on");
        });
    });
});