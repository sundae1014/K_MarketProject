document.addEventListener("DOMContentLoaded", function () {
    const qnaModalElement = document.getElementById('qnaModal');
    const qnaForm = document.querySelector('.qna-form');

    if (qnaForm) {
        qnaForm.addEventListener('submit', function (e) {
            e.preventDefault();

            // 🚨 [필수 추가] 문의 종류 선택 여부 검사 (DB 오류 방지)
            // HTML에서 name="type2"로 수정했으므로, 여기서도 name="type2"를 사용해야 합니다.
            const qnaTypeSelected = document.querySelector('input[name="type2"]:checked');

            if (!qnaTypeSelected) {
                alert('문의 종류를 선택해주세요.');
                return; // 제출 중단
            }

            // 1. 폼 데이터 수집
            const formData = new FormData(this);

            // 🚨 API 엔드포인트 설정 (백엔드 컨트롤러에서 처리할 경로)
            const url = '/kmarket/my/qna';

            // 2. 서버로 비동기 POST 요청 전송
            fetch(url, {
                method: 'POST',
                body: formData
            })
                .then(response => {
                    if (!response.ok) {
                        return response.json().then(errorData => {
                            throw new Error(errorData.message || `서버 응답 오류 (상태: ${response.status})`);
                        });
                    }
                    return response.json();
                })
                .then(result => {
                    // 3. 성공 처리
                    if (result.success) {
                        alert('문의가 성공적으로 등록되었습니다.');

                        // 4. 모달 닫기
                        const qnaModal = bootstrap.Modal.getInstance(qnaModalElement);
                        if (qnaModal) {
                            qnaModal.hide();
                        }

                        // 5. 필요하다면 페이지 새로고침
                        // window.location.reload();

                    } else {
                        alert(result.message || '문의 등록에 실패했습니다.');
                    }
                })
                .catch(error => {
                    // 6. 실패 처리
                    console.error('QnA 제출 오류:', error);
                    alert(`문의 등록 중 통신 오류가 발생했습니다: ${error.message}`);
                });
        });
    }
});