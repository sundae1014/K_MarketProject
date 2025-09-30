/* 검색 기능 */
document.addEventListener("DOMContentLoaded", () => {
    const form = document.querySelector(".search-bar");
    const input = document.getElementById("search-input"); // id 수정 확인
    const category = document.querySelector(".search-bar_category");

    if (!form) return;

    form.addEventListener("submit", e => {
        e.preventDefault();
        const q = encodeURIComponent(input.value.trim());
        const cat = encodeURIComponent(category.value);
        const url = `search.html?category=${cat}&q=${q}`;
        window.location.href = url;
    });
});