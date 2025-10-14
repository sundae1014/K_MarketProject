// seller-modal.js

document.addEventListener("DOMContentLoaded", function () {
    // 🚨 1. 판매자 정보 링크에 클릭 이벤트 리스너를 추가해야 합니다.
    //    이 링크에는 data-manufacture 속성을 통해 제조사 이름을 담아줘야 합니다.
    const sellerLinks = document.querySelectorAll('.seller-detail-link');
    const sellerModalElement = document.getElementById('sellerModal');

    sellerLinks.forEach(link => {
        link.addEventListener('click', function (e) {
            e.preventDefault();

            const manufactureName = this.dataset.manufacture;

            // 컨텍스트 루트 포함 및 API 경로 호출
            const url = `/kmarket/my/sellerDetail?manufacture=${manufactureName}`;

            fetch(url)
                .then(response => {
                    if (!response.ok) throw new Error(`서버 응답 오류 (상태: ${response.status})`);
                    // JSON API이므로, 응답을 JSON 객체로 파싱합니다.
                    return response.json();
                })
                .then(data => {
                    console.log('판매자 상세 정보 JSON:', data);
                    // 2. MemberDTO 필드명(snake_case)을 사용하여 데이터 확인 및 모달 채우기
                    if (data && data.company_name) {
                        document.getElementById('seller-companyName').textContent = data.company_name;
                        document.getElementById('seller-name').textContent = data.name;
                        document.getElementById('seller-hp').textContent = data.hp;
                        document.getElementById('seller-faxNumber').textContent = data.fax_number;
                        document.getElementById('seller-email').textContent = data.email;
                        document.getElementById('seller-businessNumber').textContent = data.business_number;

                        // 🚨 [핵심 수정] 우편번호, 기본주소, 상세주소를 조합하여 하나의 문자열로 만듭니다.
                        const zip = data.zip || '';
                        const addr1 = data.addr1 || '';
                        const addr2 = data.addr2 || '';

                        const fullAddress = `[${zip}] ${addr1} ${addr2}`;
                        document.getElementById('seller-addressDisplay').textContent = fullAddress;

                        // 모달 띄우기
                        const sellerModal = new bootstrap.Modal(sellerModalElement);
                        sellerModal.show();
                    } else {
                        alert(`판매자 [${manufactureName}]의 정보를 찾을 수 없습니다.`);
                    }
                })
                .catch(err => {
                    console.error('판매자 정보를 불러오는 데 실패했습니다.', err);
                });
        });
    });
});