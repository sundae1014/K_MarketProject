/* 전체 카테고리 구현 기능 */
// 카테고리 데이터
const CATEGORY_DATA = {
    '패션의류/잡화': [
        { name: '여성패션', code: 'C0101' }, { name: '남성패션', code: 'C0102' },
        { name: '남녀 공용 의류', code: 'C0103' }, { name: '속옷/잠옷', code: 'C0104' },
        { name: '신발', code: 'C0105' }, { name: '가방/잡화', code: 'C0106' },
        { name: '유아동패션', code: 'C0107' }, { name: '럭셔리패션', code: 'C0108' }
    ],
    '뷰티': [
        { name: '럭셔리뷰티', code: 'C0201' }, { name: '스킨케어', code: 'C0202' },
        { name: '클렌징/필링', code: 'C0203' }, { name: '선케어/태닝', code: 'C0204' },
        { name: '메이크업', code: 'C0205' }, { name: '향수', code: 'C0206' },
        { name: '남성화장품', code: 'C0207' }, { name: '네일', code: 'C0208' },
        { name: '어린이화장품',  code: 'C0209' }, { name: '헤어', code: 'C0210' },
        { name: '바디', code: 'C0211' }, { name: '선물세트/기프트', code: 'C0212' }
    ],
    '출산/유아동': [
        { name: '기저귀', code: 'C0301' }, { name: '물티슈', code: 'C0302' },
        { name: '분유/어린이식품', code: 'C0303' }, { name: '어린이 건강식품', code: 'C0304' },
        { name: '수유용품', code: 'C0305' }, { name: '이유용품/유아식기', code: 'C0306' },
        { name: '아기띠/외출용품', code: 'C0307' }, { name: '유모차/웨건', code: 'C0308' },
        { name: '카시트', code: 'C0309' }, { name: '유아가구/인테리어', code: 'C0310' },
        { name: '유아동침구', code: 'C0311' }, { name: '유아동도서', code: 'C0312' }
    ],
    '식품': [
        { name: '수입식품관', code: 'C0401' }, { name: '식품 선물세트', code: 'C0402' },
        { name: '생수/음료', code: 'C0403' }, { name: '커피/원두/차', code: 'C0404' },
        { name: '과자/초콜릿/시리얼', code: 'C0405' }, { name: '반찬/간편식/이유식', code: 'C0406' },
        { name: '면/통조림/가공식품', code: 'C0407' }, { name: '가루/조미료/오일', code: 'C0408' },
        { name: '장/소스/드레싱/식초', code: 'C0409' }, { name: '냉장/냉동/간편요리', code: 'C0410' },
        { name: '과일', code: 'C0411' }, { name: '축산/계란', code: 'C0412' }
    ],
    '주방용품': [
        { name: '프리미엄 키친', code: 'C0501' }, { name: '냄비/프라이팬', code: 'C0502' },
        { name: '주방조리도구', code: 'C0503' }, { name: '그릇/홈세트', code: 'C0504' },
        { name: '컵/텀블러/와인용품', code: 'C0505' }, { name: '밀폐저장/도시락', code: 'C0506' },
        { name: '수저/커트러리', code: 'C0507' }, { name: '주방잡화', code: 'C0508' },
        { name: '주방수납/정리', code: 'C0509' }, { name: '일회용품/종이컵', code: 'C0510' },
        { name: '주방가전', code: 'C0511' }, { name: '1인가구 주방용품', code: 'C0512' }
    ],
    '생활용품': [
        { name: '헤어', code: 'C0601' }, { name: '바디/세안', code: 'C0602' },
        { name: '구강/면도', code: 'C0603' }, { name: '화장지/물티슈', code: 'C0604' },
        { name: '생리대/성인용기저귀', code: 'C0605' }, { name: '기저귀', code: 'C0606' },
        { name: '세탁세제', code: 'C0607' }, { name: '청소/주방세제', code: 'C0608' },
        { name: '탈취/방향/살충', code: 'C0609' }, { name: '건강/의료용품', code: 'C0610' },
        { name: '욕실용품', code: 'C0611' }, { name: '생활전기용품', code: 'C0612' }
    ],
    '홈인테리어': [
        { name: '설치가구', code: 'C0701' }, { name: '홈데코/디퓨저', code: 'C0702' },
        { name: '조명/스탠드', code: 'C0703' }, { name: '커튼/블라인드', code: 'C0704' },
        { name: '카페트/쿠션', code: 'C0705' }, { name: '가구', code: 'C0706' },
        { name: '수납/정리', code: 'C0707' }, { name: '침대/매트리스', code: 'C0708' },
        { name: '침구', code: 'C0709' }, { name: '공구/철물/DIY', code: 'C0710' },
        { name: '셀프인테리어', code: 'C0711' }
    ],
    '가전디지털': [
        { name: 'TV/영상가전', code: 'C0801' }, { name: '냉장고', code: 'C0802' },
        { name: '세탁기/건조기', code: 'C0803' }, { name: '청소기', code: 'C0804' },
        { name: '계절가전', code: 'C0805' }, { name: '뷰티/헤어가전', code: 'C0806' },
        { name: '건강가전', code: 'C0807' }, { name: '주방가전', code: 'C0808' },
        { name: '노트북', code: 'C0809' }, { name: '데스크탑', code: 'C0810' },
        { name: '모니터', code: 'C0811' }, { name: '휴대폰', code: 'C0812' }
    ],
    '스포츠/레저': [
        { name: '캠핑전문관', code: 'C0901' }, { name: '홈트레이닝', code: 'C0902' },
        { name: '헬스/요가/댄스', code: 'C0903' }, { name: '수영/수상스포츠', code: 'C0904' },
        { name: '구기스포츠', code: 'C0905' }, { name: '라켓스포츠', code: 'C0906' },
        { name: '자전거', code: 'C0907' }, { name: '킥보드/스케이트', code: 'C0908' },
        { name: '등산/아웃도어', code: 'C0909' }, { name: '낚시', code: 'C0910' },
        { name: '골프', code: 'C0911' }
    ],
    '문구/오피스': [
        { name: '사무용품', code: 'C1001' }, { name: '미술/화방용품', code: 'C1002' },
        { name: '캐릭터문구', code: 'C1003' }, { name: '학용품/수업준비', code: 'C1004' },
        { name: '필기류', code: 'C1005' }, { name: '노트/메모지', code: 'C1006' },
        { name: '다이어리/플래너', code: 'C1007' }, { name: '바인더/파일', code: 'C1008' },
        { name: '파티/이벤트', code: 'C1009' }, { name: '데코/포장용품', code: 'C1010' },
        { name: '카드/엽서/봉투', code: 'C1011' }, { name: '앨범', code: 'C1012' }
    ],
    '헬스/건강식품': [
        { name: '건강기능식품', code: 'C1101' }, { name: '비타민/미네랄', code: 'C1102' },
        { name: '건강식품', code: 'C1103' }, { name: '허브/식물추출물', code: 'C1104' },
        { name: '홍삼/인삼', code: 'C1105' }, { name: '꿀/프로폴리스', code: 'C1106' },
        { name: '헬스보충식품', code: 'C1107' }, { name: '다이어트/이너뷰티', code: 'C1108' },
        { name: '영양식/선식', code: 'C1109' }, { name: '어린이 건강식품', code: 'C1110' }
    ],
    '반려동물': [
        { name: '강아지용품', code: 'C1201' }, { name: '고양이용품', code: 'C1202' },
        { name: '사료', code: 'C1203' }, { name: '간식', code: 'C1204' },
        { name: '영양제', code: 'C1205' }, { name: '산책용품', code: 'C1206' },
        { name: '관상어 용품', code: 'C1207' }, { name: '소동물/가축용품', code: 'C1208' }
    ]
};

document.addEventListener("DOMContentLoaded", () => {
    const toggleBtn = document.getElementById("category-toggle");
    const drawer = document.getElementById("category-drawer");
    const lv1 = document.getElementById("category-drawer-lv1");
    const lv2 = document.getElementById("category-drawer-lv2");

    if (!toggleBtn || !drawer) return;

    // 1차 카테고리 렌더링
    Object.keys(CATEGORY_DATA).forEach((name, index) => {
        const li = document.createElement("li");
        li.textContent = name;
        li.dataset.key = name;
        if (index === 0) li.classList.add("is-active");
        lv1.appendChild(li);
    });

    // 2차 카테고리 렌더링 (DB용)
    function renderLv2(key) {
        lv2.innerHTML = "";
        (CATEGORY_DATA[key] || []).forEach(item => {
            const a = document.createElement("a");
            a.textContent = item.name;

            // 실제 카테고리 이동 링크
            a.href = `/kmarket/product/list?lv1=${encodeURIComponent(key)}&lv2=${encodeURIComponent(item.name)}&cate_cd=${item.code}`;

            lv2.appendChild(a);
        });
    }
    renderLv2(Object.keys(CATEGORY_DATA)[0]);

    // hover 시 2차 카테고리 갱신
    lv1.addEventListener("mouseover", e => {
        const item = e.target.closest("li");
        if (!item) return;
        lv1.querySelectorAll("li").forEach(li => li.classList.remove("is-active"));
        item.classList.add("is-active");
        renderLv2(item.dataset.key);
    });

    // 토글 버튼 (햄버거)
    function setIcon(isOpen) {
        const iconWrap = toggleBtn.querySelector(".menu-icon");
        iconWrap.innerHTML = isOpen
            ? '<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"><line x1="6" y1="6" x2="18" y2="18"/><line x1="18" y1="6" x2="6" y2="18"/></svg>'
            : '<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"><line x1="4" y1="6" x2="20" y2="6"/><line x1="4" y1="12" x2="20" y2="12"/><line x1="4" y1="18" x2="20" y2="18"/></svg>';
    }

    toggleBtn.addEventListener("click", () => {
        const isOpen = drawer.classList.toggle("category-drawer_open");
        toggleBtn.classList.toggle("is-open", isOpen);
        toggleBtn.setAttribute("aria-expanded", String(isOpen));
        setIcon(isOpen);
    });

    document.addEventListener("click", e => {
        if (!e.target.closest("#category-drawer") && !e.target.closest("#category-toggle")) {
            drawer.classList.remove("category-drawer_open");
            toggleBtn.classList.remove("is-open");
            toggleBtn.setAttribute("aria-expanded", "false");
            setIcon(false);
        }
    });
});