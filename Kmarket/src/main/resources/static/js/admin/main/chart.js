new Chart(document.getElementById("bar-chart-grouped"), {
    type: 'bar',
    data: {
      labels: ["10-10", "10-11", "10-12", "10-13", "10-14"],
      datasets: [
        { label: "주문", backgroundColor: "#436CFF", data: [133,221,783,1478,2311] },
        { label: "결제", backgroundColor: "#ED5325", data: [408,547,675,734,323] },
        { label: "취소", backgroundColor: "#8FCC17", data: [133,221,783,1478,421] }

      ]
    },
    options: {
      responsive: false,
      title: { display: true, text: 'Population growth (millions)' },
      maintainAspectRatio: false
    }
});

new Chart(document.getElementById("pie-chart"), {
    type: 'pie',
    data: {
      labels: ["가전", "식품", "의류", "기타"],
      datasets: [{ 
          label: "Population (millions)",
          backgroundColor: ["#436CFF","#ED5325","#8FCC17","#F9C300"],
          data: [5267,2267,734,784]
      }]
    },
     options: {
      responsive: false,
      maintainAspectRatio: false,
      plugins: {
        title: {
          display: true,
          text: '주요 매출',
          font: { size: 15 }
        },
        legend: {
          display: true,
          position: 'bottom'
        },
      }
    }
});