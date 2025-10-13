// 페이지네이션 기능
document.addEventListener("DOMContentLoaded", () => {
    const pagination = document.querySelector(".pagination");
    const totalCount = parseInt(pagination.dataset.totalCount, 10);
    const itemsPerPage = 10;
    const totalPages = Math.ceil(totalCount / itemsPerPage);
    const currentPage = 1; // 추후 URL 파라미터에서 읽게 개선 가능

    let html = "";

    if (totalPages > 1) {
        // 이전 버튼
        html += `<a href="?page=${currentPage - 1}" class="page-link prev" ${currentPage === 1 ? "style='pointer-events:none;opacity:0.3'" : ""}>
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24"
                         viewBox="0 0 24 24" fill="none" stroke="currentColor"
                         stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                         class="lucide lucide-chevron-left-icon">
                        <path d="m15 18-6-6 6-6"/>
                    </svg>
                 </a>`;

        // 페이지 번호
        for (let i = 1; i <= totalPages; i++) {
            html += `<a href="?page=${i}" class="page-link ${i === currentPage ? "active" : ""}">${i}</a>`;
        }

        // 다음 버튼
        html += `<a href="?page=${currentPage + 1}" class="page-link next" ${currentPage === totalPages ? "style='pointer-events:none;opacity:0.3'" : ""}>
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24"
                         viewBox="0 0 24 24" fill="none" stroke="currentColor"
                         stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                         class="lucide lucide-chevron-right-icon">
                        <path d="m9 18 6-6-6-6"/>
                    </svg>
                 </a>`;
    }

    pagination.innerHTML = html;
});
