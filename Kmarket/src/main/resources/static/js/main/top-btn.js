// TOP 버튼 동작
const topBtn = document.getElementById("topBtn");

window.addEventListener("scroll", () => {
    if (window.scrollY > 300) {
        topBtn.classList.remove("hidden");
    } else {
        topBtn.classList.add("hidden");
    }
});

topBtn.addEventListener("click", () => {
    window.scrollTo({
        top: 0,
        behavior: "smooth"
    });
});