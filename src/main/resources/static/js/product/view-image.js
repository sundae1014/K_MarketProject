// 상품 상세 페이지: 썸네일 클릭 시 메인 이미지 교체 기능
document.addEventListener("DOMContentLoaded", () => {
    const mainImg = document.querySelector(".main-img img");
    const thumbs = document.querySelectorAll(".thumb-list img");

    if (!mainImg || thumbs.length === 0) return;

    // 썸네일 클릭 이벤트 등록
    thumbs.forEach((thumb) => {
        thumb.addEventListener("click", () => {
            const newSrc = thumb.getAttribute("data-src");

            if (!newSrc) return;

            // 1️⃣ 메인 이미지에 페이드 효과 적용
            mainImg.classList.add("fade-out");

            // 2️⃣ 일정 시간 후 src 교체
            setTimeout(() => {
                mainImg.setAttribute("src", newSrc);
                mainImg.classList.remove("fade-out");
            }, 200);

            // 3️⃣ 클릭된 썸네일 강조
            thumbs.forEach((t) => t.classList.remove("active"));
            thumb.classList.add("active");
        });
    });
});
