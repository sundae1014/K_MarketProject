/* 상품 상세 페이지 스크롤 기능 */
document.addEventListener("DOMContentLoaded", () => {
    const tabBtns = document.querySelectorAll(".tab-menu button");
    const sections = {
        spec: document.querySelector(".prod-spec"),
        detail: document.getElementById("detailSection"),
        review: document.getElementById("reviewSection")
    };

    // 클릭 → 스크롤 이동
    tabBtns[0].addEventListener("click", () => sections.spec.scrollIntoView({ behavior: "smooth" }));
    tabBtns[1].addEventListener("click", () => sections.detail.scrollIntoView({ behavior: "smooth" }));
    tabBtns[2].addEventListener("click", () => sections.review.scrollIntoView({ behavior: "smooth" }));

    // 스크롤 감지 → 탭 활성화
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                tabBtns.forEach(b => b.classList.remove("active"));
                if (entry.target.id === "detailSection") {
                    tabBtns[1].classList.add("active");
                } else if (entry.target.id === "reviewSection") {
                    tabBtns[2].classList.add("active");
                } else {
                    tabBtns[0].classList.add("active");
                }
            }
        });
    }, { threshold: 0.3 }); // 섹션의 30%가 보이면 활성화

    observer.observe(sections.spec);
    observer.observe(sections.detail);
    observer.observe(sections.review);
});
