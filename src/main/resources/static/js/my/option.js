document.addEventListener('DOMContentLoaded', () => {
    const emailDomain = document.getElementById('emailDomain');
    const select = document.getElementById('emailDomainSelect');

    if (select.value === '') {
        emailDomain.value = '';
        emailDomain.readOnly = false;
    } else {
        emailDomain.value = select.value;
        emailDomain.readOnly = true;
    }

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
});