const agreeBtn = document.querySelector('.agree-btn');
if (agreeBtn) {
    agreeBtn.addEventListener('click', function (e) {
        e.preventDefault()
        const terms = document.querySelector(".terms");
        const finance = document.querySelector(".finance");
        const privacy = document.querySelector(".privacy");

        if (!terms.checked || !finance.checked || !privacy.checked) {
            alert("필수 약관에 모두 동의해야 합니다.");
            return;
        }

        const type = document.querySelector('main')?.dataset.type;

        if (type == 'buyer') {
            window.location.href = '/kmarket/member/register';
        } else if (type == 'seller') {
            window.location.href = '/kmarket/member/registerSeller';
        }

    })
}