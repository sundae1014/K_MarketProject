// 폼 제출 처리
document.getElementById('qnaForm').addEventListener('submit', function(e) {
    e.preventDefault();

    // 문의종류 체크
    const qnaType = document.querySelector('input[name="qnaType"]:checked');
    if (!qnaType) {
        alert('문의종류를 선택해주세요.');
        return;
    }

    // 폼 데이터 수집
    const formData = new FormData(this);
    const data = {
        qnaType: qnaType.value,
        email: formData.get('email'),
        title: formData.get('title'),
        content: formData.get('content')
    };

    // 여기서 실제 서버로 데이터 전송
    console.log('문의 데이터:', data);

    // 성공 메시지 표시
    alert(`문의가 접수되었습니다!\n\n문의종류: ${data.qnaType}\n이메일: ${data.email}\n제목: ${data.title}`);

    // 모달 닫기
    const modal = bootstrap.Modal.getInstance(document.getElementById('qnaModal'));
    modal.hide();

    // 폼 초기화
    this.reset();
});

// 모달이 닫힐 때 폼 초기화
document.getElementById('qnaModal').addEventListener('hidden.bs.modal', function() {
    document.getElementById('qnaForm').reset();
});