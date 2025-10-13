document.addEventListener("DOMContentLoaded", () => {
    const checkAll = document.getElementById("checkAll");
    const rowChecks = document.querySelectorAll(".row-check");
    if (!checkAll || !rowChecks) return;
    checkAll.addEventListener("change", () => {
        rowChecks.forEach(chk => chk.checked = checkAll.checked);
    });

    rowChecks.forEach(chk => {
        chk.addEventListener("change", () => {
            const allChecked = Array.from(rowChecks).every(c => c.checked);
            checkAll.checked = allChecked;
        });
    });
});
