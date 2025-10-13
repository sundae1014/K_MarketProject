const API_KEY = "AIzaSyAQjhwx0D9Iza3ntEaZFDP0BGYGSYsZn9c";
const PLAYLIST_ID = "PLZGDyqLjHoLQpBJ59m1lRHGP__TCvSDF2";

async function loadPlaylist() {
    const container = document.getElementById("playlist-container");

    if (!container) return;

    let nextPageToken = "";
    do {
        const res = await fetch(`https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=${PLAYLIST_ID}&maxResults=10&pageToken=${nextPageToken}&key=${API_KEY}`);
        const data = await res.json();

        data.items.forEach(item => {
            const videoId = item.snippet.resourceId.videoId;
            const title = item.snippet.title;
            const channel = item.snippet.channelTitle;
            const publishedAt = new Date(item.snippet.publishedAt).toLocaleDateString();

            const div = document.createElement("div");
            div.classList.add("media-item");
            div.innerHTML = `
                <iframe width="400" height="250" src="https://www.youtube.com/embed/${videoId}" frameborder="0" allowfullscreen></iframe>
                <div class="media-info">
                    <h2>${title}</h2>
                    <p>#MiniProject | ${channel} | ${publishedAt}</p>
                </div>
            `;
            container.appendChild(div);
        });

        nextPageToken = data.nextPageToken || "";
    } while(nextPageToken);
}

loadPlaylist();