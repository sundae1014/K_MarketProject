const modal = document.querySelector('.modal');

const btnOpenModal = document.querySelector('.modal-openBtn');
const btnCloseModals = document.querySelectorAll('.close-modal');

btnOpenModal.addEventListener("click", () => {
  modal.style.display = "flex";
});

btnCloseModals.forEach(btn => {
    btn.addEventListener("click", () => {
        const formElements = modal.querySelectorAll('input, textarea, select');
        formElements.forEach(el => {
            if (el.tagName === 'SELECT') {
                el.selectedIndex = 0;
            } else if (el.type === 'date' || el.type === 'text') {
                el.value = '';
            }
        });
        modal.style.display = "none";
    });
});

<!-- 모달 데이터 등록 -->
const btnRegister = modal.querySelector('.register-btn');

btnRegister.addEventListener('click', () => {
    const data = {
        title: modal.querySelector('input[name="title"]').value,
        department: modal.querySelector('select[name="department"]').value,
        experience: modal.querySelector('select[name="experience"]').value,
        hire_type: modal.querySelector('select[name="hire_type"]').value,
        recruit_period_start: modal.querySelector('input[name="recruit_period_start"]').value,
        recruit_period_end: modal.querySelector('input[name="recruit_period_end"]').value,
        content: modal.querySelector('input[name="content"]').value
    };

    fetch('/kmarket/admin/cs/recruit/insert', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    })
        .then(response => response.json())
        .then(() => {
            // 등록 후 테이블 전체 새로고침
            refreshRecruitTable();

            const formElements = modal.querySelectorAll('input, textarea, select');
            formElements.forEach(el => {
                if (el.tagName === 'SELECT') {
                    el.selectedIndex = 0;
                } else if (el.type === 'date' || el.type === 'text') {
                    el.value = '';
                }
            });

            modal.style.display = 'none';

            alert("등록되었습니다!");
        })
        .catch(err => {
            console.error(err);
            alert('서버 통신 오류');
        });
});

// 테이블 전체 새로고침 함수
function refreshRecruitTable() {
    fetch('/kmarket/admin/cs/recruit/list/json')
        .then(res => res.json())
        .then(hires => {
            const tbody = document.getElementById('recruit-tbody');
            tbody.innerHTML = '';

            hires.forEach((hire, index) => {
                const row = document.createElement('tr');

                // 체크박스
                const tdCheck = document.createElement('td');
                tdCheck.innerHTML = `<input type="checkbox" class="row-check" value="${hire.hire_no}">`;
                row.appendChild(tdCheck);

                // 번호
                const tdNum = document.createElement('td');
                tdNum.className = 'col-num';
                tdNum.textContent = index + 1; // 연속 번호
                row.appendChild(tdNum);

                // 채용부서
                const tdDept = document.createElement('td');
                tdDept.className = 'col-dept';
                tdDept.textContent = hire.department;
                row.appendChild(tdDept);

                // 경력사항
                const tdCareer = document.createElement('td');
                tdCareer.className = 'col-career';
                tdCareer.textContent = hire.experience;
                row.appendChild(tdCareer);

                // 채용형태
                const tdType = document.createElement('td');
                tdType.className = 'col-type';
                tdType.textContent = hire.hire_type;
                row.appendChild(tdType);

                // 제목
                const tdTitle = document.createElement('td');
                tdTitle.className = 'col-title';
                tdTitle.textContent = hire.title;
                row.appendChild(tdTitle);

                // 작성자
                const tdAuthor = document.createElement('td');
                tdAuthor.className = 'col-writer';
                tdAuthor.textContent = hire.author;
                row.appendChild(tdAuthor);

                // 상태 (CSS 클래스 조건 적용)
                const tdStatus = document.createElement('td');
                tdStatus.className = 'col-status ' + (hire.status === '모집중' ? 'status-open' : 'status-closed');
                tdStatus.textContent = hire.status;
                row.appendChild(tdStatus);

                // 모집기간
                const tdPeriod = document.createElement('td');
                tdPeriod.className = 'col-period';
                tdPeriod.textContent = `${hire.recruit_period_start} ~ ${hire.recruit_period_end}`;
                row.appendChild(tdPeriod);

                // 작성일
                const tdDate = document.createElement('td');
                tdDate.className = 'col-write';
                tdDate.textContent = hire.create_date;
                row.appendChild(tdDate);

                // tbody에 추가
                tbody.appendChild(row);
            });

        })
        .catch(err => console.error(err));
}
