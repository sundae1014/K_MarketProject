// 배송정보 (도착 예정일 자동 계산) 기능
document.addEventListener("DOMContentLoaded", () => {
    const deliveryDateEl = document.querySelector(".delivery-date");
    if (!deliveryDateEl) return;

    const today = new Date();
    const arrival = new Date(today);
    arrival.setDate(today.getDate() + 2); // 2일 후

    const days = ["일", "월", "화", "수", "목", "금", "토"];
    const dayName = days[arrival.getDay()];
    const dateStr = `모레(${dayName}) ${arrival.getMonth() + 1}/${arrival.getDate()} 도착 예정`;

    deliveryDateEl.textContent = dateStr;
});