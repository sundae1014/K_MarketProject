document.addEventListener('DOMContentLoaded', function() {
    const table = document.getElementById('recruit-tbody');

    document.getElementById('select-delete').addEventListener('click', (e) => {
        e.preventDefault();

        // 현재 DOM에서 체크된 항목 선택
        const checkedBoxes = Array.from(document.querySelectorAll('.row-check:checked'));
        if (checkedBoxes.length === 0) {
            alert('삭제할 항목을 선택하세요.');
            return;
        }

        const ids = checkedBoxes.map(cb => {
            return parseInt(cb.closest('tr').querySelector('.col-num').textContent.trim());
        });

        fetch('/kmarket/admin/cs/recruit/delete', {
            method: 'POST',
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(ids)
        })
            .then(response => response.text())
            .then(result => {
                console.log('서버 응답:', result);
                // 삭제 후 table 다시 로딩
                location.reload();
                alert('선택 항목이 삭제되었습니다.');
            })
            .catch(err => {
                console.error(err);
                alert('서버 통신 오류');
            });
    });
});
