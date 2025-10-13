/* 토스트 메세지 기능 */
function showToast(message) {
    const toast = document.getElementById("toast");
    toast.textContent = message;
    toast.className = "show";

    // 3초 후 사라지기
    setTimeout(() => {
        toast.className = toast.className.replace("show", "");
    }, 3000);
}