document.addEventListener('DOMContentLoaded', () => {
    // 1. DOM 요소 가져오기
    const emailDomain = document.getElementById('emailDomain');
    const select = document.getElementById('emailDomainSelect');
    const modiBtns = document.querySelectorAll('.modi-btn');
    // finalModifyBtn은 폼 제출(type="submit") 역할을 하므로 별도의 클릭 이벤트는 불필요합니다.

    // 이전에 입력된 원본 값을 저장하는 객체
    const originalValues = {};

    // 이메일 도메인 입력 필드의 readOnly 상태를 업데이트하는 함수
    const updateDomainInput = () => {
        if (!emailDomain || !select) return;

        if (select.value === '') {
            // '직접입력' 선택 시
            emailDomain.value = '';
            emailDomain.readOnly = false; // 편집 가능
            emailDomain.focus();
        } else {
            // 도메인 선택 시
            emailDomain.value = select.value;
            emailDomain.readOnly = true;  // 편집 불가능
        }
    };

    if (emailDomain && select) {
        // 페이지 로드 시 초기 상태 설정
        if (select.value !== '') {
            emailDomain.readOnly = true;
        } else {
            emailDomain.readOnly = false;
        }

        // change 이벤트 리스너 등록
        select.addEventListener('change', updateDomainInput);
    }

    // 2. 수정/취소 버튼(.modi-btn) 이벤트 리스너 (기존 로직 유지)
    modiBtns.forEach(button => {
        button.addEventListener('click', function(e) {
            e.preventDefault(); // 폼 전체 제출이 아닌 섹션별 입력 활성화를 위한 버튼이므로 제출을 막습니다.

            const targetClass = this.dataset.target;
            const targetInputs = document.querySelectorAll(`.${targetClass}`);

            const isDisabling = this.textContent === '취소';

            // 상태 변경 로직
            if (isDisabling) {
                this.textContent = '수정'; // 버튼 텍스트를 '수정'으로 변경

                // 원본 값으로 복원
                if (originalValues[targetClass]) {
                    targetInputs.forEach(input => {
                        const key = input.id || input.name;
                        if (key && originalValues[targetClass].hasOwnProperty(key)) {
                            input.value = originalValues[targetClass][key];
                        }
                    });

                    // 이메일 그룹일 경우 도메인 입력 필드 상태 업데이트
                    if (targetClass === 'input-group-email') {
                        updateDomainInput();
                    }
                }

            } else {
                this.textContent = '취소'; // 버튼 텍스트를 '취소'로 변경

                // 현재 입력 필드의 값들을 원본 값으로 저장
                originalValues[targetClass] = {};
                targetInputs.forEach(input => {
                    const key = input.id || input.name;
                    if (key) {
                        originalValues[targetClass][key] = input.value;
                    }
                    if (input.tagName === 'SELECT') {
                        originalValues[targetClass]['selectValue'] = input.value;
                    }
                });

                // 이메일 그룹이 활성화될 때 readOnly 상태를 업데이트합니다.
                if (targetClass === 'input-group-email') {
                    updateDomainInput();
                }
            }

            // disabled 속성 토글
            targetInputs.forEach(input => {
                input.disabled = isDisabling;
            });

            // 활성화 후 첫 번째 입력 필드에 포커스
            if (!isDisabling && targetInputs.length > 0 && targetInputs[0].tagName === 'INPUT') {
                targetInputs[0].focus();
            }
        });
    });

    const optionForm = document.querySelector('form');
    const finalModifyBtn = document.querySelector('.final-modify-btn');

    // 2. 폼 제출 이벤트를 가로채서 처리
    if (optionForm && finalModifyBtn) {
        finalModifyBtn.addEventListener('click', function(e) {
            // e.preventDefault(); // 기본 폼 제출을 막습니다.

            // 💡 핵심 해결 로직: 제출 직전에 모든 입력 필드의 disabled 속성을 제거합니다.
            //    이렇게 해야 수정하지 않은 기존 값도 서버로 전송됩니다.

            // 폼 내부에 있는 모든 <input>, <select> 요소를 선택
            optionForm.querySelectorAll('input, select').forEach(input => {
                if (input.hasAttribute('disabled')) {
                    // disabled 속성을 제거하여 서버로 값이 전송되도록 합니다.
                    input.removeAttribute('disabled');
                }
            });

            // 이제 폼을 수동으로 제출합니다.
            optionForm.submit();
        });
    }

});