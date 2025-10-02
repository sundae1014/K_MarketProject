/* 슬라이드  */
document.addEventListener("DOMContentLoaded", () => {
    const slides = document.querySelectorAll(".slide");
    const slidesWrapper = document.querySelector(".slides");
    const prevBtn = document.querySelector(".prev");
    const nextBtn = document.querySelector(".next");
    const dots = document.querySelectorAll(".dot");
    let currentIndex = 0;

    if (!slidesWrapper || slides.length === 0 || !prevBtn || !nextBtn || dots.length === 0) return;


    const totalSlides = slides.length;

    // 슬라이드 1개 너비 + gap 계산
    const gap = 20;
    const slideWidth = slides[0].offsetWidth;

    function updateSlider(index) {
        slidesWrapper.style.transform = `translateX(-${index * slideWidth}px)`;

        dots.forEach((dot, i) => {
            dot.classList.toggle("active", i === index);
        });
    }

    nextBtn.addEventListener("click", () => {
        if (currentIndex < totalSlides - 1) {
            currentIndex++;
        } else {
            currentIndex = 0;
        }
        updateSlider(currentIndex);
    });

    prevBtn.addEventListener("click", () => {
        if (currentIndex > 0) {
            currentIndex--;
        } else {
            currentIndex = totalSlides - 1;
        }
        updateSlider(currentIndex);
    });

    dots.forEach((dot, i) => {
        dot.addEventListener("click", () => {
            currentIndex = i;
            updateSlider(currentIndex);
        });
    });

    updateSlider(currentIndex);
    });
