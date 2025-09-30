/* 기준별 목록 정렬 기능 */
document.addEventListener("DOMContentLoaded", () => {
    const pathItems = document.querySelectorAll(".nav-path li");
    const categoryTitle = document.getElementById("category-title");
    if (pathItems.length > 0 && categoryTitle) {
        categoryTitle.textContent = pathItems[pathItems.length - 1].textContent;
    }

    // 정렬 버튼 활성화 토글
    const sortBtns = document.querySelectorAll(".sort-btn");
    sortBtns.forEach(btn => {
        btn.addEventListener("click", () => {
            sortBtns.forEach(b => b.classList.remove("active"));
            btn.classList.add("active");
            // TODO: 여기서 실제 정렬 함수 호출
        });
    });
});