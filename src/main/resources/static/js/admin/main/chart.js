window.addEventListener('DOMContentLoaded', () => {
    const ctx = document.getElementById('bar-chart-grouped').getContext('2d');

    new Chart(ctx, {
        type: 'bar',
        data: {
            labels: barLabels,
            datasets: [
                { label: "주문", backgroundColor: "#436CFF", data: orderData },
                { label: "결제", backgroundColor: "#ED5325", data: paymentData },
                { label: "취소", backgroundColor: "#8FCC17", data: cancelData }
            ]
        },
        options: {
            responsive: false,
            maintainAspectRatio: false,
            plugins: {
                title: { display: true, text: '최근 5일 주문/결제/취소 현황', font: { size: 15 } },
                legend: { display: true, position: 'bottom' }
            }
        }
    });
});

window.addEventListener('DOMContentLoaded', () => {
    const ctx = document.getElementById('pie-chart').getContext('2d');

    const labels = pieData.map(item => item.label);
    const values = pieData.map(item => item.value);

    new Chart(ctx, {
        type: 'pie',
        data: {
            labels: labels,
            datasets: [{
                data: values,
                backgroundColor: [
                    "#436CFF","#ED5325","#8FCC17","#F9C300","#FF7F50","#7B68EE",
                    "#20B2AA","#FFB6C1","#FFA500","#66CDAA","#BA55D3","#00CED1"
                ]
            }]
        },
        options: {
            responsive: false,
            maintainAspectRatio: false,
            plugins: {
                title: { display: true, text: '주요 매출', font: { size: 15 } },
                legend: { display: true, position: 'bottom' }
            }
        }
    });
});
