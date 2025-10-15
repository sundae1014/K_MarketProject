// 카드사별 추가 혜택 기능
document.addEventListener("DOMContentLoaded", () => {
    const salePrice = parseInt(document.querySelector(".sale").textContent.replace(/,/g, "").replace("원", ""));
    const cardBenefit = document.getElementById("card-benefit");

    const cards = [
        { name: "국민카드", rate: 0.05 },
        { name: "신한카드", rate: 0.07 }
    ];

    const lines = cards.map(card => {
        const discountPrice = (salePrice * (1 - card.rate)).toLocaleString();
        return `${card.name} : ${discountPrice}원`;
    });

    cardBenefit.innerHTML = lines.join("<br>");
});
