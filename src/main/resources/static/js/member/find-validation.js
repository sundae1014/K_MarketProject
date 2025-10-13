/**
 *  폼 유효성 검사 자바스크립트
 */
// 유효성 검사에 사용할 정규표현식
const reUid   = /^[a-z]+[a-z0-9]{4,19}$/g;
const rePass  = /^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+]).{5,16}$/;
const reName  = /^[가-힣]{2,10}$/
const reNick  = /^[a-zA-Zㄱ-힣0-9][a-zA-Zㄱ-힣0-9]*$/;
const reEmail = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
const reHp    = /^01(?:0|1|[6-9])-(?:\d{4})-\d{4}$/;

let isEmailOk = false;
let isHpOk = false;


document.addEventListener('DOMContentLoaded', function(){

    const btnCheckEmail = document.getElementById('btnCheckEmail');
    const btnEmailCode = document.getElementById('btnEmailCode');


    const emailResult = document.getElementsByClassName('emailResult')[0];
    const hpResult = document.getElementsByClassName('hpResult')[0];


    const auth = document.getElementsByClassName('auth')[0];

    const form = document.getElementsByTagName('form')[0];

    //////////////////////////////////////////////////////////
    // 이메일 검사
    //////////////////////////////////////////////////////////

    let preventDblClick = false; // 이중 클릭 방지를 위한 상태 변수

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

                if(data.count == 0){
                    emailResult.innerText = '존재하지 않는 사용자 이메일 입니다.';
                    emailResult.style.color = 'red';
                    isEmailOk = false;
                    preventDblClick = false;
                    return;
                } else {
                    emailResult.innerText = '이메일로 인증코드 전송 중 입니다.';
                    emailResult.style.color = 'green';

                    await fetch("/kmarket/member/email/send", {
                        method: "POST",
                        headers: { "Content-Type": "application/json" },
                        body: JSON.stringify({ email: value, mode: "find" })
                    });

                    emailResult.innerText = '이메일 인증번호를 입력하세요.';
                    emailResult.style.color = 'green';

                }

            } catch(err) {
                console.log(err);
                emailResult.innerText = '이메일 전송 실패';
                emailResult.style.color = 'red';
                emailResult.style.color = 'red';
            } finally {
                preventDblClick = false;
            }
        });
    }

    if(btnEmailCode){
        btnEmailCode.addEventListener('click', async function(e) {
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
    // 휴대폰 중복 체크
    //////////////////////////////////////////////////////////
    form.hp.addEventListener('focusout', function(e){

        const value = form.hp.value;
        console.log('value : ' + value);

        if(!value.match(reHp)){
            hpResult.innerText = '휴대폰 번호가 유효하지 않습니다.';
            hpResult.style.color = 'red';
            isHpOk = false;
            return;
        }

        fetch(`/kmarket/member/hp/${value}`)
            .then(res => res.json())
            .then(data => {
                console.log(data);
                if(data.count > 0){
                    hpResult.innerText = '이미 사용 중인 휴대폰 입니다.';
                    hpResult.style.color = 'red';
                    isHpOk = false;
                }else{
                    hpResult.innerText = '사용 가능한 휴대폰 입니다.';
                    hpResult.style.color = 'green';
                    isHpOk = true;
                }
            })
            .catch(err => {
                console.log(err);
            });
    });

    // 최종 폼 전송 처리
    form.addEventListener('submit', function(e){
        e.preventDefault(); // 기본 폼전송 해제

        if(btnCheckEmail&&!isEmailOk){
            alert('이메일을 확인하세요.');
            return;
        }

        if(hpResult&&!isHpOk){
            alert('휴대폰을 확인하세요.');
            return;
        }
        // 최종 폼 전송 실행
        form.submit();
    });

}); // DOMContentLoaded 끝