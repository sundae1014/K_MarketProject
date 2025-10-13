let currentRating = 0;
let uploadedFiles = [];
let currentUploadType = '';

// 별점 기능
document.addEventListener('DOMContentLoaded', function() {
    const stars = document.querySelectorAll('.star');
    const ratingText = document.getElementById('ratingText');
    const ratingValue = document.getElementById('ratingValue');
    const textarea = document.querySelector('textarea[name="reviewContent"]');
    const charCount = document.getElementById('charCount');

    const ratingTexts = {
        0: '별점을 선택해주세요 (필수)',
        1: '매우 불만족',
        2: '불만족',
        3: '보통',
        4: '만족',
        5: '매우 만족'
    };

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
            ratingText.textContent = ratingTexts[currentRating];
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


// 파일 업로드 타입 선택
    function selectFileType(type) {
        currentUploadType = type;

        // 버튼 스타일 업데이트
        document.querySelectorAll('.file-upload-btn').forEach(btn => {
            btn.classList.remove('selected');
        });
        event.target.classList.add('selected');

        // 파일 선택 창 열기
        document.getElementById('fileInput').click();
    }

// 파일 업로드 처리
    function handleFileUpload(event) {
        const files = Array.from(event.target.files);

        files.forEach(file => {
            if (uploadedFiles.length >= 5) {
                alert('최대 5장까지 업로드 가능합니다.');
                return;
            }

            if (file.size > 5 * 1024 * 1024) {
                alert('파일 크기는 5MB 이하로 업로드해주세요.');
                return;
            }

            const fileObj = {
                file: file,
                type: currentUploadType,
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

        // 내용 가져오기
        const content = this.reviewContent.value.trim();

        // 폼 데이터 수집
        const formData = new FormData();
        formData.append('rating', currentRating);
        formData.append('reviewContent', content);

        // 파일 추가
        uploadedFiles.forEach((fileObj, index) => {
            formData.append(`images`, fileObj.file);
            formData.append(`imageTypes`, fileObj.type);
        });

        // 여기서 실제 서버로 데이터 전송
        console.log('리뷰 데이터:', {
            rating: currentRating,
            content: content,
            fileCount: uploadedFiles.length
        });

        // 성공 메시지 표시
        alert(`상품평이 등록되었습니다!\n\n별점: ${currentRating}점\n내용: ${content.substring(0, 30)}...\n첨부 이미지: ${uploadedFiles.length}장`);

        // 모달 닫기
        const modal = bootstrap.Modal.getInstance(document.getElementById('reviewModal'));
        modal.hide();

        // 폼 초기화
        resetForm();
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
        const content = document.querySelector('textarea[name="reviewContent"]').value.trim();
        const textarea = document.querySelector('textarea[name="reviewContent"]');

        if (content.length === 0) {
            errorMessage += '• 상품평 내용을 작성해주세요.\n';
            isValid = false;
        } else if (content.length < 10) {
            errorMessage += '• 상품평은 최소 10자 이상 작성해주세요.\n';
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
        currentUploadType = '';

        document.getElementById('ratingValue').value = 0;
        document.getElementById('ratingText').textContent = '별점을 선택해주세요 (필수)';
        document.getElementById('charCount').textContent = '0';
        document.querySelectorAll('.star').forEach(star => {
            star.classList.remove('active', 'hover');
        });
        document.querySelectorAll('.file-upload-btn').forEach(btn => {
            btn.classList.remove('selected');
        });
        document.getElementById('filePreview').innerHTML = '';
        document.getElementById('reviewForm').reset();

        // 스타일 초기화
        const textarea = document.querySelector('textarea[name="reviewContent"]');
        textarea.style.borderColor = '#ced4da';
        textarea.style.boxShadow = 'none';

        const starRating = document.getElementById('starRating');
        starRating.style.border = 'none';
        starRating.style.padding = '0';

        const counter = document.getElementById('charCount').parentElement;
        counter.classList.remove('over-limit', 'under-limit', 'valid');
    }

// 모달이 닫힐 때 폼 초기화
    document.getElementById('reviewModal').addEventListener('hidden.bs.modal', function () {
        resetForm();
    });
})