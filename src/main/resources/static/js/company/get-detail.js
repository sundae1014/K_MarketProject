
const detailDiv = document.getElementById("post-detail");

if(detailDiv){
    const params = new URLSearchParams(window.location.search);
    const id = parseInt(params.get("id"));

// blogPosts에서 해당 id 글 찾기
    const post = blogPosts.find(p => p.id === id);



    if(post) {
        detailDiv.innerHTML = `
        <article class="blog-detail">
            <h1>${post.title}</h1>
            <p class="meta">${post.date} | ${post.author}</p>
            <img src="${post.img}" alt="${post.title}" style="max-width:100%; margin: 20px 0;">
            <div class="content">
                ${post.content}
            </div>
        </article>
    `;
    } else {
        detailDiv.innerHTML = "<p>글을 찾을 수 없습니다.</p>";
    }

}
