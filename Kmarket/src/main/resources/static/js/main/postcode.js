/* 카카오 주소 검색 API 실행 함수 */
function execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            // 선택한 주소 데이터 세팅
            document.getElementById("postcode").value = data.zonecode; // 우편번호
            document.getElementById("address").value = data.roadAddress; // 도로명 주소
            // 상세주소는 사용자가 직접 입력
            document.getElementById("detailAddress").focus();
        }
    }).open();
}