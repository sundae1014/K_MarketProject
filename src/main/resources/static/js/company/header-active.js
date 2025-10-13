const path = window.location.pathname;

document.querySelectorAll('.header-link a').forEach(a => {
    if(a.getAttribute('href') === path) {
        a.classList.add('active');
    }
});