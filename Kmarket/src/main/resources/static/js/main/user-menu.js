/* 유저별 권한 기능 */
// 상태값 (임시), 서버 연동 시 세션 값으로 구분 할 예정
// guest = 비로그인, user = 일반 회원, admin = 관리자
let userType = "guest";  // 필요할 때 "user", "admin" 으로 바꾸기

const utilityMenu = document.getElementById("utility-menu");

function renderMenu(type) {
    let html = "";

    if (type === "guest") {
        html = `
      <li><a href="#">로그인</a></li>
      <li><a href="#">회원가입</a></li>
      <li><a href="#">고객센터</a></li>
    `;
    } else if (type === "user") {
        html = `
      <li><a href="#">정순권 님</a></li>
      <li><a href="#">로그아웃</a></li>
      <li><a href="#">고객센터</a></li>
    `;
    } else if (type === "admin") {
        html = `
      <li><a href="/admin">관리자</a></li>
      <li><a href="#">로그아웃</a></li>
      <li><a href="#">고객센터</a></li>
    `;
    }

    utilityMenu.innerHTML = html;
}

// 실행
renderMenu(userType);