const agreeBtn = document.querySelector('.agree-btn');

agreeBtn.addEventListener('click', function (e){
    e.preventDefault()
    const terms = document.querySelector(".terms");
    const finance = document.querySelector(".finance");
    const privacy = document.querySelector(".privacy");

    if(!terms.checked || !finance.checked || !privacy.checked){
        alert("필수 약관에 모두 동의해야 합니다.");
        return;
    }

    const type = document.body.dataset.type;

    if(type == 'individual'){
        window.location.href = '/Kmarket/member/register';
    }else if(type == 'seller'){
        window.location.href = '/Kmarket/member/registerSeller';
    }

})
