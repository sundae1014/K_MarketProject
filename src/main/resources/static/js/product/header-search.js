// 헤더 검색창 기능
document.addEventListener("DOMContentLoaded", () => {
    const form = document.querySelector(".search-bar");
    const cateInput = document.getElementById("cate_cd");
    const select = document.getElementById("search-category");

    if (!form) return;

    // 현재 페이지 cate_cd 확인
    const params = new URLSearchParams(window.location.search);
    const currentCate = params.get("cate_cd");

    if (currentCate) {
        cateInput.value = currentCate;
        const mainCode = currentCate.substring(0, 3);
        select.value = mainCode;
    }

    // select 변경 시 cate_cd 업데이트
    select.addEventListener("change", (e) => {
        const mainCode = e.target.value;
        cateInput.value = mainCode === "all" ? "" : mainCode; // C01 등으로 보냄
    });

    // submit 시 cate_cd 없으면 전체 검색으로 처리
    form.addEventListener("submit", (e) => {
        if (!cateInput.value || cateInput.value === "all") {
            cateInput.value = "all"; // 전체 검색용 코드
        }
    });
});
