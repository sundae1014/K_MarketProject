// 배송정보 (도착 예정일 자동 계산) 기능
document.addEventListener("DOMContentLoaded", () => {
    const deliveryEl = document.querySelector(".prod-detail dd strong");
    if (!deliveryEl) return;

    const today = new Date();
    const arrival = new Date(today);
    arrival.setDate(today.getDate() + 2); // 2일 후

    const days = ["일", "월", "화", "수", "목", "금", "토"];
    const dateStr = `${days[arrival.getDay()]}(${arrival.getDate()}일) 도착 예정`;

    deliveryEl.textContent = dateStr;
});
