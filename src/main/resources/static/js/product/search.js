// ------------------------------------
// 상품 검색 결과 페이지 내부 검색 기능
// ------------------------------------
document.addEventListener("DOMContentLoaded", function() {
    const searchInput = document.querySelector(".search-result-input input");
    const searchBtn = document.querySelector(".search-result-input .btn-search");

    if (searchInput && searchBtn) {
        searchBtn.addEventListener("click", function() {
            const q = searchInput.value.trim();
            if (q === "") {
                alert("검색어를 입력해주세요.");
                return;
            }

            // URL 이동
            const url = `/kmarket/product/list?category=all&keyword=${encodeURIComponent(q)}`;
            window.location.href = url;
        });

        // 엔터로 검색
        searchInput.addEventListener("keypress", function(e) {
            if (e.key === "Enter") {
                e.preventDefault();
                searchBtn.click();
            }
        });
    }
});

// ------------------------------------
// 상품 정렬 기능
// ------------------------------------
document.addEventListener("DOMContentLoaded", () => {
    const sortButtons = document.querySelectorAll(".sort-btn");

    sortButtons.forEach(btn => {
        btn.addEventListener("click", () => {
            const sortValue = btn.dataset.sort;
            const url = new URL(window.location.href);
            url.searchParams.set("sort", sortValue);
            window.location.href = url.toString();
        });
    });
});
