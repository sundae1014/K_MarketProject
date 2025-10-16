/* 유저별 권한 기능 */
document.addEventListener("DOMContentLoaded", function() {
    console.log("✅ user-menu.js 실행됨, userType:", userType);

    const utilityMenu = document.getElementById("utility-menu");
    if (!utilityMenu) {
        console.warn("⚠️ utilityMenu 요소를 찾을 수 없습니다.");
        return;
    }

    let html = "";

    if (userType === "guest") {
        html = `
            <li><a href="/kmarket/member/login">로그인</a></li>
            <li><a href="/kmarket/member/join">회원가입</a></li>
            <li><a href="/kmarket/cs/index">고객센터</a></li>
        `;
    } else if (userType === "user") {
        html = `
            <li><a href="#">${userName} 님</a></li>
            <li><a href="/kmarket/member/logout">로그아웃</a></li>
            <li><a href="/kmarket/cs/index">고객센터</a></li>
        `;
    } else if (userType === "admin") {
        html = `
            <li><a href="/kmarket/admin/index">관리자</a></li>
            <li><a href="/kmarket/member/logout">로그아웃</a></li>
            <li><a href="/kmarket/cs/index">고객센터</a></li>
        `;
    }

    utilityMenu.innerHTML = html;
});
