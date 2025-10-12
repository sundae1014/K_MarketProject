/* 전체 카테고리 구현 기능 */
// 카테고리 데이터
const CATEGORY_DATA = {
    '패션의류/잡화': ['여성패션','남성패션','남녀 공용 의류','속옷/잠옷','신발','가방/잡화','유아동패션','럭셔리패션'],
    '뷰티': ['럭셔리뷰티','스킨케어','클렌징/필링','선케어/태닝','메이크업','향수','남성화장품','네일','어린이화장품','헤어','바디','선물세트/키트'],
    '출산/유아동': ['기저귀','물티슈','분유/어린이식품','어린이 건강식품','수유용품','이유용품/유아식기','아기띠/외출용품','유모차/웨건','카시트','유아가구/인테리어','유아동침구','유아동도서'],
    '식품': ['수입식품관','식품 선물세트','생수/음료','커피/원두/차','과자/초콜릿/시리얼','반찬/간편식/이유식','면/통조림/가공식품','가루/조미료/오일','장/소스/드레싱/식초','냉장/냉동/간편요리','과일','축산/계란'],
    '주방용품': ['프리미엄 키친','냄비/프라이팬','주방조리도구','그릇/홈세트','컵/텀블러/와인용품','밀폐저장/도시락','수저/커트러리','주방잡화','주방수납/정리','일회용품/종이컵','주방가전','1인가구 주방용품'],
    '생활용품': ['헤어','바디/세안','구강/면도','화장지/물티슈','생리대/성인용기저귀','기저귀','세탁세제','청소/주방세제','탈취/방향/살충','건강/의료용품','욕실용품','생활전기용품'],
    '홈인테리어': ['설치가구','홈데코/디퓨저','조명/스탠드','커튼/블라인드','카페트/쿠션','가구','수납/정리','침대/매트리스','침구','공구/철물/DIY','셀프인테리어'],
    '가전디지털': ['TV/영상가전','냉장고','세탁기/건조기','청소기','계절가전','뷰티/헤어가전','건강가전','주방가전','노트북','데스크탑','모니터','휴대폰'],
    '스포츠/레저': ['캠핑전문관','홈트레이닝','헬스/요가/댄스','수영/수상스포츠','구기스포츠','라켓스포츠','자전거','킥보드/스케이트','등산/아웃도어','낚시','골프'],
    '문구/오피스': ['사무용품','미술/화방용품','캐릭터문구','학용품/수업준비','필기류','노트/메모지','다이어리/플래너','바인더/파일','파티/이벤트','데코/포장용품','카드/엽서/봉투','앨범'],
    '헬스/건강식품': ['건강기능식품','비타민/미네랄','건강식품','허브/식물추출물','홍삼/인삼','꿀/프로폴리스','헬스보충식품','다이어트/이너뷰티','영양식/선식','어린이 건강식품'],
    '반려동물': ['강아지용품','고양이용품','사료','간식','영양제','산책용품','관상어 용품','소동물/가축용품']
};

document.addEventListener("DOMContentLoaded", () => {
    const toggleBtn = document.getElementById("category-toggle");
    const drawer = document.getElementById("category-drawer");
    const lv1 = document.getElementById("category-drawer-lv1");
    const lv2 = document.getElementById("category-drawer-lv2");

    if (!toggleBtn || !drawer) return;

    // 1차 카테고리 생성
    Object.keys(CATEGORY_DATA).forEach((name, index) => {
        const li = document.createElement("li");
        li.textContent = name;
        li.dataset.key = name;
        if (index === 0) li.classList.add("is-active");
        lv1.appendChild(li);
    });

    // 2차 카테고리 렌더링
    function renderLv2(key) {
        lv2.innerHTML = "";
        (CATEGORY_DATA[key] || []).forEach(label => {
            const a = document.createElement("a");
            /* 찐코드 */
            /* a.href = `prodList.html?lv1=${encodeURIComponent(key)}&lv2=${encodeURIComponent(label)}`; */

            /* 더미 데이터 */
            if (key === "식품" && label === "식품 선물세트") {
                // 임시: 식품 > 식품 선물세트만 연결
                a.href = "/kmarket/product/list";
            } else {
                // 다른 카테고리는 아직 연결 안 함
                a.href = "#";
            }

            a.textContent = label;
            lv2.appendChild(a);
        });
    }
    renderLv2(Object.keys(CATEGORY_DATA)[0]);

    // 1차 hover → 2차 갱신
    lv1.addEventListener("mouseover", e => {
        const item = e.target.closest("li");
        if (!item) return;
        lv1.querySelectorAll("li").forEach(li => li.classList.remove("is-active"));
        item.classList.add("is-active");
        renderLv2(item.dataset.key);
    });

    // 햄버거 ↔ X 아이콘 교체
    function setIcon(isOpen) {
        const iconWrap = toggleBtn.querySelector(".menu-icon");
        iconWrap.innerHTML = isOpen
            ? '<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"><line x1="6" y1="6" x2="18" y2="18"/><line x1="18" y1="6" x2="6" y2="18"/></svg>'
            : '<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"><line x1="4" y1="6" x2="20" y2="6"/><line x1="4" y1="12" x2="20" y2="12"/><line x1="4" y1="18" x2="20" y2="18"/></svg>';
    }

    // 버튼 토글
    toggleBtn.addEventListener("click", () => {
        const isOpen = drawer.classList.toggle("category-drawer_open");
        toggleBtn.classList.toggle("is-open", isOpen);
        toggleBtn.setAttribute("aria-expanded", String(isOpen));
        setIcon(isOpen);
    });

    // 바깥 클릭 → 닫기
    document.addEventListener("click", e => {
        if (!e.target.closest("#category-drawer") && !e.target.closest("#category-toggle")) {
            drawer.classList.remove("category-drawer_open");
            toggleBtn.classList.remove("is-open");
            toggleBtn.setAttribute("aria-expanded", "false");
            setIcon(false);
        }
    });
});