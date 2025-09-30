document.addEventListener("DOMContentLoaded", () => {
    $('.bxslider').bxSlider({
        mode: 'horizontal',
        auto: true,
        pause: 3000,      // 3초 간격
        speed: 1000,       // 1초 전환
        pager: true,      // 하단 점
        controls: false,  // 좌우 버튼 노출 (노출 시 true)
        adaptiveHeight: false
    });
});