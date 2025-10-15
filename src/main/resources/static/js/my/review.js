let currentRating = 0;
let uploadedFiles = [];

// 별점 기능 및 DOM 로직
document.addEventListener('DOMContentLoaded', function() {
    const stars = document.querySelectorAll('.star');
    const ratingValue = document.getElementById('ratingValue');
    const textarea = document.querySelector('textarea[name="reviewContent"]');
    const MAX_CHARS = 255;

    // 별점 이벤트 리스너
    stars.forEach(star => {
        star.addEventListener('mouseenter', function () {
            const rating = parseInt(this.dataset.rating);
            highlightStars(rating, true);
        });

        star.addEventListener('mouseleave', function () {
            highlightStars(currentRating, false);
        });

        star.addEventListener('click', function () {
            currentRating = parseInt(this.dataset.rating);
            ratingValue.value = currentRating;
            highlightStars(currentRating, false);
        });
    });

    function highlightStars(rating, isHover) {
        stars.forEach((star, index) => {
            star.classList.remove('active', 'hover');
            if (index < rating) {
                star.classList.add(isHover ? 'hover' : 'active');
            }
        });
    }


// 파일 업로드 처리
    function handleFileUpload(event) {
        const files = Array.from(event.target.files);

        files.forEach(file => {
            if (uploadedFiles.length >= 3) {
                alert('최대 3장까지 업로드 가능합니다.');
                return;
            }

            if (file.size > 5 * 1024 * 1024) {
                alert('파일 크기는 5MB 이하로 업로드해주세요.');
                return;
            }

            const fileObj = {
                file: file,
                id: Date.now() + Math.random()
            };

            uploadedFiles.push(fileObj);
            displayFilePreview(fileObj);
        });

        // 파일 입력 초기화
        event.target.value = '';
    }

// 파일 미리보기 표시
    function displayFilePreview(fileObj) {
        const preview = document.getElementById('filePreview');
        const previewItem = document.createElement('div');
        previewItem.className = 'file-preview-item';
        previewItem.dataset.fileId = fileObj.id;

        const img = document.createElement('img');
        const reader = new FileReader();

        reader.onload = function (e) {
            img.src = e.target.result;
        };
        reader.readAsDataURL(fileObj.file);

        const removeBtn = document.createElement('button');
        removeBtn.className = 'remove-file';
        removeBtn.innerHTML = '×';
        removeBtn.onclick = () => removeFile(fileObj.id);

        previewItem.appendChild(img);
        previewItem.appendChild(removeBtn);
        preview.appendChild(previewItem);
    }

// 파일 제거
    function removeFile(fileId) {
        uploadedFiles = uploadedFiles.filter(f => f.id !== fileId);
        const previewItem = document.querySelector(`[data-file-id="${fileId}"]`);
        if (previewItem) {
            previewItem.remove();
        }
    }

// 폼 제출 처리
    document.getElementById('reviewForm').addEventListener('submit', function (e) {
        e.preventDefault();

        // 필수 항목 검증
        if (!validateForm()) {
            return;
        }

        const orderNumber = document.getElementById('reviewOrderNumber').value;
        const prodNo = document.getElementById('reviewProdNo').value;
        const content = textarea.value.trim(); // 💡 (개선) 상위 스코프의 textarea 변수 사용

        // 폼 데이터 수집
        const formData = new FormData();
        formData.append('orderNumber', orderNumber);
        formData.append('prodNo', prodNo);
        formData.append('rating', currentRating);
        formData.append('reviewContent', content);

        // 파일 추가
        uploadedFiles.forEach((fileObj, index) => {
            formData.append(`images`, fileObj.file);
        });

        //  AJAX 요청으로 서버에 전송
        $.ajax({
            url: '/kmarket/my/registerReview',
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,

            success: function(response) {
                const modal = bootstrap.Modal.getInstance(document.getElementById('reviewModal'));
                if(response.success) {
                    alert("상품평이 등록되었습니다. 감사합니다.");
                    modal.hide();
                    location.reload();
                } else {
                    alert("상품평 등록 실패: " + (response.message || "처리 중 오류가 발생했습니다."));
                }
            },
            error: function() {
                alert("상품평 등록 중 통신 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.");
            }
        });
    });

// 폼 유효성 검사 함수
    function validateForm() {
        let isValid = true;
        let errorMessage = '';

        // 별점(만족도) 필수 검증
        if (currentRating === 0) {
            errorMessage += '• 만족도(별점)를 선택해주세요.\n';
            isValid = false;

            // 별점 영역 하이라이트
            const starRating = document.getElementById('starRating');
            starRating.style.border = '2px solid #dc3545';
            starRating.style.borderRadius = '4px';
            starRating.style.padding = '5px';

            setTimeout(() => {
                starRating.style.border = 'none';
                starRating.style.padding = '0';
            }, 3000);
        }

        // 내용 필수 검증
        const content = textarea.value.trim();

        if (content.length === 0) {
            errorMessage += '• 상품평 내용을 작성해주세요.\n';
            isValid = false;
        } else if (content.length < 10) {
            errorMessage += '• 상품평은 최소 10자 이상 작성해주세요.\n';
            isValid = false;
        } else if (content.length > MAX_CHARS) { // 💡 (추가) 글자 수 초과 유효성 검사
            errorMessage += `• 상품평은 최대 ${MAX_CHARS}자 이하로 작성해주세요.\n`;
            isValid = false;
        }

        // 오류 메시지 표시
        if (!isValid) {
            alert('다음 항목을 확인해주세요:\n\n' + errorMessage);
        }

        return isValid;
    }

// 폼 초기화
    function resetForm() {
        currentRating = 0;
        uploadedFiles = [];

        document.getElementById('ratingValue').value = 0;
        document.querySelectorAll('.star').forEach(star => {
            star.classList.remove('active', 'hover');
        });
        document.querySelectorAll('.file-upload-btn').forEach(btn => {
            btn.classList.remove('selected');
        });
        document.getElementById('filePreview').innerHTML = '';
        document.getElementById('reviewForm').reset();

        // 스타일 초기화
        textarea.style.borderColor = '#ced4da';
        textarea.style.boxShadow = 'none';

        const starRating = document.getElementById('starRating');
        starRating.style.border = 'none';
        starRating.style.padding = '0';

    }

// 모달이 열릴 때 주문 정보를 Hidden Input에 설정하는 로직
    const reviewModal = document.getElementById('reviewModal');
    reviewModal.addEventListener('show.bs.modal', function (event) {
        const button = event.relatedTarget;
        const orderNumber = button.getAttribute('data-order-number');
        const prodNo = button.getAttribute('data-prod-no');
        const prodName = button.getAttribute('data-prod-name');

        document.getElementById('reviewOrderNumber').value = orderNumber;
        document.getElementById('reviewProdNo').value = prodNo;
        document.getElementById('reviewProductName').textContent = prodName;

        resetForm();
    });

    window.handleFileUpload = handleFileUpload;
    window.removeFile = removeFile;

})