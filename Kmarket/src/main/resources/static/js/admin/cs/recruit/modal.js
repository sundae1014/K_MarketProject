const modal = document.querySelector('.modal');
const btnOpenModal = document.querySelector('.modal-openBtn');
const btnCloseModals = document.querySelectorAll('.close-modal');
btnOpenModal.addEventListener("click", () => {
  modal.style.display = "flex";
});

btnCloseModals.forEach(btn => {
  btn.addEventListener("click", () => {
    modal.style.display = "none";
  });
});
