/* 소식과 이야기 블로그  */
document.addEventListener('DOMContentLoaded', () => {
    const cateBtns = document.querySelectorAll('.blog-cateLink button');
    const posts = document.querySelectorAll('.blog-post');

    if (cateBtns.length && posts.length) {
        cateBtns.forEach(btn => {
            btn.addEventListener('click', () => {
                cateBtns.forEach(b => b.classList.remove('active'));
                btn.classList.add('active');

                const cate = btn.dataset.cate;

                posts.forEach(post => {
                    if(cate === '0' || post.dataset.cate === cate){
                        post.classList.add('show');
                    } else {
                        post.classList.remove('show');
                    }
                });
            });
        });
        document.querySelector('[data-cate="0"]').click();
    }

});
