document.addEventListener('DOMContentLoaded', () => {
    const emailDomain = document.getElementById('emailDomain');
    const select = document.getElementById('emailDomainSelect');

    if (emailDomain && select) {

        // 5번째 줄 오류를 일으킨 초기 로직
        if (select.value === '') {
            emailDomain.value = '';
            emailDomain.readOnly = false;
        } else {
            emailDomain.value = select.value;
            emailDomain.readOnly = true;
        }

        // change 이벤트 리스너 로직
        select.addEventListener('change', () => {
            if (select.value === '') {
                emailDomain.value = '';
                emailDomain.readOnly = false;
                emailDomain.focus();
            } else {
                emailDomain.value = select.value;
                emailDomain.readOnly = true;
            }
        });
    }
});