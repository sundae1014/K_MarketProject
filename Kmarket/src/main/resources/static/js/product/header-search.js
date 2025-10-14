// 헤더 검색창 기능
document.addEventListener("DOMContentLoaded", () => {
    const form = document.querySelector(".search-bar");
    const cateInput = document.getElementById("cate_cd");
    const select = document.getElementById("search-category");

    if (!form) return;

    // 1️⃣ 현재 페이지 URL에 cate_cd 있으면 가져오기
    const params = new URLSearchParams(window.location.search);
    const currentCate = params.get("cate_cd");

    if (currentCate) {
        cateInput.value = currentCate;

        // 1차 카테고리 코드(C01~C12) 기준으로 select 옵션 선택 상태 표시
        const mainCode = currentCate.substring(0, 3); // C04 → 식품
        select.value = mainCode;
    }

    // 2️⃣ 사용자가 select에서 카테고리를 직접 바꿀 때 cate_cd 자동 반영
    select.addEventListener("change", (e) => {
        const mainCode = e.target.value;
        cateInput.value = mainCode === "all" ? "" : mainCode + "00"; // 예: C04 → C0400 (식품 전체)
    });

    // 3️⃣ submit 시 cate_cd가 비어 있으면 전체 검색
    form.addEventListener("submit", (e) => {
        if (!cateInput.value) cateInput.value = "";
    });
});
