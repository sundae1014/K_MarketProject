const listDiv = document.getElementById("blog-list");
if(listDiv){
    blogPosts.forEach(post => {
        const card = document.createElement("div");
        card.innerHTML = `
      <h2><a href="detail.html?id=${post.id}">${post.title}</a></h2>
      <p>${post.author} | ${post.date}</p>
      <p>${post.content.substring(0, 100)}...</p>
    `;
        listDiv.appendChild(card);
    });
}
