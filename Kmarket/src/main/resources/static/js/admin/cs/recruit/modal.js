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
        recruit_period_start: modal.querySelector('input[name="recruit_period_start"]').value || null,
        recruit_period_end: modal.querySelector('input[name="recruit_period_end"]').value || null,
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
            location.reload();

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
