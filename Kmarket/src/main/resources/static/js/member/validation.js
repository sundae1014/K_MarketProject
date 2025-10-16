/**
 *  폼 유효성 검사 자바스크립트
 */
// 유효성 검사에 사용할 정규표현식
const reUid   = /^[a-z]+[a-z0-9]{4,19}$/g;
const rePass  = /^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+]).{5,16}$/;
const reName  = /^[가-힣]{2,10}$/
const reEmail = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
const reHp    = /^01(?:0|1|[6-9])-(?:\d{4})-\d{4}$/;

// 유효성 검사 상태 변수
let isUidOk = false;
let isPassOk = false;
let isNameOk = false;
let isEmailOk = false;
let isHpOk = false;

document.addEventListener('DOMContentLoaded', function(){

    const checkCustid = document.getElementById('checkCustid')
    const checkName = document.getElementById('checkName');
    const checkPw = document.getElementById('checkPw');
    const btnCheckEmail = document.getElementById('btnCheckEmail');
    const emailCode = document.getElementById('emailCode');
    const btnCheckHp = document.getElementById('btnCheckHp');
    const HpCode = document.getElementById('HpCode');

    const uidResult = document.getElementsByClassName('uidResult')[0];
    const emailResult = document.getElementsByClassName('emailResult')[0];
    const hpResult = document.getElementsByClassName('hpResult')[0];
    const passResult = document.getElementsByClassName('passResult')[0];
    const nameResult = document.getElementsByClassName('nameResult')[0];

    const auth = document.getElementsByClassName('auth')[0];

    const form = document.getElementsByTagName('form')[0];

    //////////////////////////////////////////////////////////
    // 아이디 검사
    //////////////////////////////////////////////////////////
    if(checkCustid){
        checkCustid.addEventListener('focusout', function(e){

            const value = form.custid.value;
            console.log('value : ' + value);

            // 아이디 유효성 검사
            if(!value.match(reUid)){
                uidResult.innerText = '아이디가 유효하지 않습니다.';
                uidResult.style.color = 'red';
                isUidOk = false;
                return;
            }

            // 아이디 중복체크 요청
            fetch(`/kmarket/member/custid/${value}`)
                .then(res => res.json())
                .then(data => {
                    console.log(data);
                    if(data.count > 0){
                        uidResult.innerText = '이미 사용 중인 아이디 입니다.';
                        uidResult.style.color = 'red';
                        isUidOk = false;
                    }else{
                        uidResult.innerText = '사용 가능한 아이디 입니다.';
                        uidResult.style.color = 'green';
                        isUidOk = true;
                    }
                })
                .catch(err => {
                    console.log(err);
                });
        });
    }


    //////////////////////////////////////////////////////////
    // 비밀번호 검사
    //////////////////////////////////////////////////////////
    form.pw2.addEventListener('focusout', function(e){

        const pw1 = form.pw.value;
        const pw2 = form.pw2.value;

        // 비밀번호 유효성 검사
        if(!pw1.match(rePass)){
            passResult.innerText = '비밀번호가 유효하지 않습니다.';
            passResult.style.color = 'red';
            isPassOk = false;
            return;
        }

        // 비밀번호 2회 일치 여부
        if(pw1 == pw2){
            passResult.innerText = '비밀번호가 일치합니다.';
            passResult.style.color = 'green';
            isPassOk = true;
        }else{
            passResult.innerText = '비밀번호가 일치하지 않습니다.';
            passResult.style.color = 'red';
            isPassOk = false;
        }
    });

    //////////////////////////////////////////////////////////
    // 이름 검사
    //////////////////////////////////////////////////////////
    if(checkName){
        checkName.addEventListener('focusout', function(e){

            const value = checkName.value;

            if(!value.match(reName)){
                nameResult.innerText = '이름이 유효하지 않습니다.';
                nameResult.style.color = 'red';
                isNameOk = false;
            }else{
                nameResult.innerText = '';
                isNameOk = true;
            }
        });


    }

    //////////////////////////////////////////////////////////
    // 이메일 검사
    //////////////////////////////////////////////////////////

    let preventDblClick = false;


    // 이메일 코드 전송 버튼 클릭
    if(btnCheckEmail){
        btnCheckEmail.addEventListener('click', async function(e){
            // 이중 클릭 방지
            if(preventDblClick){
                return;
            }

            const value = form.email.value;
            console.log('value : ' + value);

            // 이메일 유효성 검사
            if(!value.match(reEmail)){
                emailResult.innerText = '이메일이 유효하지 않습니다.';
                emailResult.style.color = 'red';
                isEmailOk = false;
                return;
            }

            e.preventDefault();

            // 이중 클릭 방지
            if(preventDblClick) return;
            preventDblClick = true;



            // 이메일 중복 체크 & 코드 전송
            try {
                const res = await fetch(`/kmarket/member/email/${value}`);
                const data = await res.json();

                if(data.count > 0){
                    emailResult.innerText = '이미 사용 중인 이메일 입니다.';
                    emailResult.style.color = 'red';
                    isEmailOk = false;
                    preventDblClick = false;
                    return;
                } else {
                    emailCode.style.display = "block";
                    emailResult.innerText = '이메일로 인증코드 전송 중 입니다.';
                    emailResult.style.color = 'green';

                    await fetch("/kmarket/member/email/send", {
                        method: "POST",
                        headers: { "Content-Type": "application/json" },
                        body: JSON.stringify({ email: value, mode: "join" })
                    });

                    emailResult.innerText = '이메일 인증번호를 입력하세요.';
                    emailResult.style.color = 'green';
                    emailCode.style.display = 'block';
                }

            } catch(err) {
                console.log(err);
                emailResult.innerText = '이메일 전송 실패';
                emailResult.style.color = 'red';
            } finally {
                preventDblClick = false;
            }
        });
    }

    if(emailCode){
        emailCode.addEventListener('focusout', async function(e) {
            const code = emailCode.value.trim();
            if(code === '') return; // 빈 값이면 체크 안함

            try {
                const response = await fetch('/kmarket/member/email/code', {
                    method: 'POST',
                    headers: {"Content-Type": "application/json"},
                    body: JSON.stringify({code: code})
                });

                const verifyData = await response.json();

                if(verifyData.isMatched){
                    emailResult.innerText = '이메일이 인증되었습니다.';
                    emailResult.style.color = 'green';
                    isEmailOk = true;
                } else {
                    emailResult.innerText = '인증코드가 일치하지 않습니다.';
                    emailResult.style.color = 'red';
                    isEmailOk = false;
                }
            } catch(err) {
                console.error(err);
                emailResult.innerText = '인증 확인 중 오류가 발생했습니다.';
                emailResult.style.color = 'red';
                isEmailOk = false;
            }
        });
    }




    //////////////////////////////////////////////////////////
    // 휴대폰 검사
    //////////////////////////////////////////////////////////
    let preventDblClickHp = false;

    if(btnCheckHp){
       btnCheckHp.addEventListener('click', async function(e){
           if(preventDblClickHp){
               return;
           }

           const value = form.hp.value;
           console.log('value: '+value);

           if(!value.match(reHp)){
               hpResult.innerText = '휴대폰 번호가 유효하지 않습니다.'
               hpResult.style.color = 'red';
               isHpOk = false;
               return;
           }

           e.preventDefault();

           if(preventDblClickHp) return;
           preventDblClickHp = true;

           try{
                const res = await fetch(`/kmarket/member/hp/${value}`);
                const data = await res.json();

                if(data.count>0){
                    hpResult.innerText = '이미 사용중인 휴대폰 입니다.';
                    hpResult.style.color = 'red';
                    isHpOk = false;
                    preventDblClickHp = false;
                    return;
                }else{
                    HpCode.style.display = "block";
                    hpResult.innerText = '휴대폰으로 인증코드 전송 중 입니다.';
                    hpResult.style.color = 'green';

                    await fetch("/kmarket/member/hp/send", {
                        method: "POST",
                        headers: {"Content-Type": "application/json"},
                        body: JSON.stringify({hp: value, mode: "join"})
                    });

                    hpResult.innerText = '휴대폰 인증번호를 입력하세요.';
                    hpResult.style.color = 'green';
                    HpCode.style.display = 'block';
                }
           }catch (err){
               console.log(err);
               hpResult.innerText = '휴대폰 전송 실패';
               hpResult.style.color = 'red';
           }finally {
               preventDblClickHp = false;
           }

       });

    }

    if(HpCode){
        HpCode.addEventListener('focusout', async function(e){
            const code = HpCode.value.trim();
            if(code == '') return;

            try{
                const response = await fetch('/kmarket/member/hp/code', {
                    method: 'POST',
                    headers: {"Content-Type": "application/json"},
                    body: JSON.stringify({code:code})
                });

                const verifyData = await response.json();

                if(verifyData.isMatched){
                    hpResult.innerText = '휴대폰이 인증되었습니다.';
                    hpResult.style.color = 'green';
                    isHpOk = true;
                }else{
                    hpResult.innerText = '인증코드가 일치하지 않습니다.';
                    hpResult.style.color = 'red';
                    isHpOk = false;
                }
            }catch(err){
                console.error(err);
                hpResult.innerText = '인증 확인 중 오류가 발생했습니다.'
                hpResult.style.color = 'red';
                isHpOk = false;
            }
        });
    }

    // 최종 폼 전송 처리
    form.addEventListener('submit', function(e){
        e.preventDefault(); // 기본 폼전송 해제

        if(uidResult&&!isUidOk){
            alert('아이디를 확인하세요.');
            return;
        }

        if(passResult&&!isPassOk){
            alert('비밀번호를 확인하세요.');
            return;
        }

        if(nameResult&&!isNameOk){
            alert('이름을 확인하세요.');
            return;
        }


        if(btnCheckEmail&&!isEmailOk){
            alert('이메일을 확인하세요.');
            return;
        }

        if(btnCheckHp&&!isHpOk){
            alert('휴대폰을 확인하세요.');
            return;
        }

        // 판매자 전용 필드 유효성 검사
        const company_name = form.querySelector('input[name="company_name"]');
        const bisiness_number = form.querySelector('input[name="bisiness_number"]');
        const ecommerce_number = form.querySelector('input[name="ecommerce_number"]');
        const fax_number = form.querySelector('input[name="fax_number"]');
        const zip = form.querySelector('input[name="zip"]');
        const addr2 = form.querySelector('input[name="addr2"]');
        const hp = form.querySelector('input[name="hp"]');

        if (company_name && company_name.value.trim() === '') {
            alert('회사명을 입력하세요.');
            companyName.focus();
            return;
        }

        if (bisiness_number && bisiness_number.value.trim() === '') {
            alert('사업자등록번호를 입력하세요.');
            bisiness_number.focus();
            return;
        }

        if (ecommerce_number && ecommerce_number.value.trim() === '') {
            alert('통신판매업번호를 입력하세요.');
            ecommerce_number.focus();
            return;
        }

        if (fax_number && fax_number.value.trim() === '') {
            alert('팩스번호를 입력하세요.');
            fax_number.focus();
            return;
        }
        if (!btnCheckHp && hp && hp.value.trim() === '') {
            alert('전화번호를 입력하세요.');
            hp.focus();
            return;
        }

        if(!btnCheckEmail){
            if (zip && zip.value.trim() === '') {
                alert('주소를 입력하세요.');
                zip.focus();
                return;
            }else if (addr2 && addr2.value.trim() === '') {
                alert('상세 주소를 입력하세요.');
                addr2.focus();
                return;
            }
        }


            // 최종 폼 전송 실행
        form.submit();
    });

}); // DOMContentLoaded 끝