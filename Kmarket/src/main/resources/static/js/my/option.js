document.addEventListener('DOMContentLoaded', () => {
    // 1. DOM 요소 가져오기
    const emailDomain = document.getElementById('emailDomain');
    const select = document.getElementById('emailDomainSelect');
    const modiBtns = document.querySelectorAll('.modi-btn');

    const originalValues = {};

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

        if (select.value !== '') {
            emailDomain.readOnly = true;
        } else {
            emailDomain.readOnly = false;
        }

        // change 이벤트 리스너 등록
        select.addEventListener('change', updateDomainInput);
    }

    modiBtns.forEach(button => {
        button.addEventListener('click', function(e) {
            e.preventDefault();

            const targetClass = this.dataset.target;
            const targetInputs = document.querySelectorAll(`.${targetClass}`);

            const isDisabling = this.textContent === '취소';

            if (isDisabling) {
                this.textContent = '수정하기';


                if (originalValues[targetClass]) {
                    targetInputs.forEach(input => {
                        const key = input.id || input.name;
                        if (originalValues[targetClass].hasOwnProperty(key)) {
                            // select 박스 값 복원
                            if (input.tagName === 'SELECT') {
                                input.value = originalValues[targetClass]['selectValue'];
                            } else {
                                input.value = originalValues[targetClass][key];
                            }
                        }
                    });

                    if (targetClass === 'input-group-email') {
                        updateDomainInput();
                    }
                }

            } else {
                this.textContent = '취소';

                originalValues[targetClass] = {};
                targetInputs.forEach(input => {
                    // id 또는 name을 키로 사용 (input은 id, select는 id를 주로 사용)
                    const key = input.id || input.name;
                    if (key) {
                        originalValues[targetClass][key] = input.value;
                    }
                    // select 박스는 별도로 현재 선택된 값 저장
                    if (input.tagName === 'SELECT') {
                        originalValues[targetClass]['selectValue'] = input.value;
                    }
                });

                // 💡 이메일 그룹이 활성화될 때 readOnly 상태를 업데이트합니다.
                if (targetClass === 'input-group-email') {
                    updateDomainInput();
                }
            }

            // disabled 속성 토글 (전체 그룹 활성화/비활성화)
            targetInputs.forEach(input => {
                input.disabled = isDisabling;
            });

            // 활성화 후 첫 번째 입력 필드에 포커스
            if (!isDisabling && targetInputs.length > 0 && targetInputs[0].tagName === 'INPUT') {
                targetInputs[0].focus();
            }
        });
    });
});