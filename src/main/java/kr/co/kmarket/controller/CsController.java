package kr.co.kmarket.controller;

import kr.co.kmarket.dto.FaqDTO;
import kr.co.kmarket.dto.PageRequestDTO;
import kr.co.kmarket.dto.PageResponseDTO;
import kr.co.kmarket.dto.QnaDTO;
import kr.co.kmarket.service.FaqService;
import kr.co.kmarket.service.QnaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/cs")
@Controller
public class CsController {
    private final FaqService faqService;
    private final QnaService qnaService;

    /* 고객센터 메인 */
    @GetMapping("/index")
    public String index(){return "cs/cs_main";}

    /* 고객센터 공지사항 */
    @GetMapping("/notice/list")
    public String noticeList(){return "cs/cs_noticeList";}

    @GetMapping("/notice/view")
    public String noticeView(){return "cs/cs_noticeView";}

    /* 고객센터 자주묻는 질문 */
    @GetMapping("/faq/list")
    public String faqList(Model  model){
        String type1 = "회원";
        List<String> typeName2 = switch("member") {
            case "member" -> List.of("가입", "탈퇴", "회원정보", "로그인");
            default -> List.of();
        };
        List<FaqDTO> faqList = faqService.selectType1(type1);

        model.addAttribute("faqList", faqList);
        model.addAttribute("typeName", type1);
        model.addAttribute("typeName2", typeName2);

        return "cs/cs_faqList";
    }

    @GetMapping("/faq/list/{type1}")
    public String faqList(@PathVariable("type1") String type1, Model model) {
        String typeName = switch(type1){
            case "member" -> "회원";
            case "benefit" -> "쿠폰 / 혜택 / 이벤트";
            case "orderAndPay" -> "주문 / 결제";
            case "deliver" -> "배송";
            case "cancelAndReturnAndExchange" -> "취소 / 반품 / 교환";
            case "traverlAndAccommodationAndAviation" -> "여행 / 숙박 / 항공";
            case "Safetrade" -> "안전거래";
            default -> throw new IllegalStateException("Unexpected value: " + type1);
        };

        List<String> typeName2 = switch(type1) {
            case "member" -> List.of("가입", "탈퇴", "회원정보", "로그인");
            case "benefit" -> List.of("쿠폰 / 할인혜택", "포인트", "제휴", "이벤트");
            case "orderAndPay" -> List.of("상품", "결제", "구매내역", "영수증 / 증빙");
            case "deliver" -> List.of("배송상태 / 기간", "배송정보확인 / 변경", "해외배송", "당일배송", "해외직구");
            case "cancelAndReturnAndExchange" -> List.of("반품신청 / 철회", "반품정보확인 / 변경", "교환 AS신청 / 철회", "교환정보확인 / 변경", "취소신청 / 철회", "취소확인 / 환불정보");
            case "traverlAndAccommodationAndAviation" -> List.of("여행 / 숙박", "항공");
            case "Safetrade" -> List.of("서비스 이용규칙 위반", "지식재산권침해", "법령 및 정책위반 상품", "게시물 정책위반", "직거래 / 외부거래유도", "표시광고", "청소년 위해상품 / 이미지");
            default -> List.of();
        }; //값 확인하기

        List<FaqDTO> faqList = faqService.selectType1(typeName);
        model.addAttribute("faqList", faqList);
        model.addAttribute("typeName", typeName);
        model.addAttribute("typeName2", typeName2);
        return "cs/cs_faqList";
    }
    @GetMapping("/faq/list/{type1}/{type2}")
    public String faqList(@PathVariable("type1") String type1, @PathVariable("type2") String type2, Model model) {
        String typeName = switch(type1){
            case "member" -> "회원";
            case "benefit" -> "쿠폰 / 혜택 / 이벤트";
            case "orderAndPay" -> "주문 / 결제";
            case "deliver" -> "배송";
            case "cancelAndReturnAndExchange" -> "취소 / 반품 / 교환";
            case "traverlAndAccommodationAndAviation" -> "여행 / 숙박 / 항공";
            case "Safetrade" -> "안전거래";
            default -> throw new IllegalStateException("Unexpected value: " + type1);
        };
        String typeName2 = switch(type2){
            case "register" -> "가입"; //회원
            case "withdrawal" -> "탈퇴";
            case "info" -> "회원정보";
            case "login" -> "로그인";
            case "couponAndDiscount" -> "쿠폰 / 할인혜택"; //쿠폰 / 혜택 / 이벤트
            case "point" -> "포인트";
            case "partnership" -> "제휴";
            case "event" -> "이벤트";
            case "product" -> "상품"; //주문 / 결제
            case "pay" -> "결제";
            case "buyList" -> "구매내역";
            case "receipt" -> "영수증 / 증빙";
            case "statusAndPeriod" -> "배송상태 / 기간"; //배송
            case "checkAndChange" -> "배송정보확인 / 변경";
            case "foreign" -> "해외배송";
            case "day" -> "당일배송";
            case "direct" -> "해외직구";
            case "returnAndWithdrawal" -> "반품신청 / 철회"; //취소 / 반품 / 교환
            case "returnAndChange" -> "반품정보확인 / 변경";
            case "exchangeAndWithdrawal" -> "교환 AS신청 / 철회";
            case "exchangeAndChange" -> "교환정보확인 / 변경";
            case "cancelAndWithdrawal" -> "취소신청 / 철회";
            case "cancelAndRefund" -> "취소확인 / 환불정보";
            case "travel" -> "여행 / 숙박"; //여행 / 숙박 / 항공
            case "flight" -> "항공";
            case "service" -> "서비스 이용규칙 위반"; // 안전거래
            case "knowledge" -> "지식재산권침해";
            case "contraveningGoods" -> "법령 및 정책위반 상품";
            case "article" -> "게시물 정책위반";
            case "induction" -> "직거래 / 외부거래유도";
            case "advertising" -> "표시광고";
            case "teenager" -> "청소년 위해상품 / 이미지";
            default -> throw new IllegalStateException("Unexpected value: " + type1);
        };

        List<FaqDTO> faqList = faqService.selectTypeAll(typeName, typeName2);
        model.addAttribute("faqList", faqList);
        model.addAttribute("typeName", typeName);
        model.addAttribute("typeName2", typeName2);

        return "cs/cs_faqList";
    }


    @GetMapping("/faq/view")
    public String faqView(int id, Model model){
        log.info("id: {}", id);

        FaqDTO faqDTO = faqService.getFaq(id);
        log.info("faqDTO: {}", faqDTO);
        model.addAttribute("faqDTO", faqDTO);

        return "cs/cs_faqView";
    }

    /* 고객센터 문의글 */
    @GetMapping("/qna/list")
    public String qnaList(Model  model, PageRequestDTO pageRequestDTO){
        String type1 = "회원";
        PageResponseDTO pageResponseDTO = qnaService.selectTypeAll(type1, pageRequestDTO);
        log.info("pageResponseDTO = {}", pageResponseDTO);
        model.addAttribute("pageResponseDTO", pageResponseDTO);
        model.addAttribute("typeName", type1);
        return "cs/cs_inquiresList";
    }

    @GetMapping("/qna/list/{type1}")
    public String qnaList(@PathVariable("type1") String type1, PageRequestDTO pageRequestDTO, Model model) {
        String typeName = switch(type1){
            case "member" -> "회원";
            case "benefit" -> "쿠폰 / 혜택 / 이벤트";
            case "orderAndPay" -> "주문 / 결제";
            case "deliver" -> "배송";
            case "cancelAndReturnAndExchange" -> "취소 / 반품 / 교환";
            case "traverlAndAccommodationAndAviation" -> "여행 / 숙박 / 항공";
            case "Safetrade" -> "안전거래";
            default -> throw new IllegalStateException("Unexpected value: " + type1);
        };

        PageResponseDTO pageResponseDTO = qnaService.selectTypeAll(typeName, pageRequestDTO);
        log.info("pageResponseDTO = {}", pageResponseDTO);
        model.addAttribute("pageResponseDTO", pageResponseDTO);
        model.addAttribute("typeCode", type1);
        model.addAttribute("typeName", typeName);

        return "cs/cs_inquiresList";
    }

    @GetMapping("/qna/list/{type1}/{type2}")
    public String qnaList(@PathVariable("type1") String type1, @PathVariable("type2") String type2, PageRequestDTO pageRequestDTO, Model model) {
        String typeName = switch(type1){
            case "member" -> "회원";
            case "benefit" -> "쿠폰 / 혜택 / 이벤트";
            case "orderAndPay" -> "주문 / 결제";
            case "deliver" -> "배송";
            case "cancelAndReturnAndExchange" -> "취소 / 반품 / 교환";
            case "traverlAndAccommodationAndAviation" -> "여행 / 숙박 / 항공";
            case "Safetrade" -> "안전거래";
            default -> throw new IllegalStateException("Unexpected value: " + type1);
        };
        String typeName2 = switch(type2){
            case "register" -> "가입"; //회원
            case "withdrawal" -> "탈퇴";
            case "info" -> "회원정보";
            case "login" -> "로그인";
            case "couponAndDiscount" -> "쿠폰 / 할인혜택"; //쿠폰 / 혜택 / 이벤트
            case "point" -> "포인트";
            case "partnership" -> "제휴";
            case "event" -> "이벤트";
            case "product" -> "상품"; //주문 / 결제
            case "pay" -> "결제";
            case "buyList" -> "구매내역";
            case "receipt" -> "영수증 / 증빙";
            case "statusAndPeriod" -> "배송상태 / 기간"; //배송
            case "checkAndChange" -> "배송정보확인 / 변경";
            case "foreign" -> "해외배송";
            case "day" -> "당일배송";
            case "direct" -> "해외직구";
            case "returnAndWithdrawal" -> "반품신청 / 철회"; //취소 / 반품 / 교환
            case "returnAndChange" -> "반품정보확인 / 변경";
            case "exchangeAndWithdrawal" -> "교환 AS신청 / 철회";
            case "exchangeAndChange" -> "교환정보확인 / 변경";
            case "cancelAndWithdrawal" -> "취소신청 / 철회";
            case "cancelAndRefund" -> "취소확인 / 환불정보";
            case "travel" -> "여행 / 숙박"; //여행 / 숙박 / 항공
            case "flight" -> "항공";
            case "service" -> "서비스 이용규칙 위반"; // 안전거래
            case "knowledge" -> "지식재산권침해";
            case "contraveningGoods" -> "법령 및 정책위반 상품";
            case "article" -> "게시물 정책위반";
            case "induction" -> "직거래 / 외부거래유도";
            case "advertising" -> "표시광고";
            case "teenager" -> "청소년 위해상품 / 이미지";
            default -> throw new IllegalStateException("Unexpected value: " + type1);
        };

        PageResponseDTO pageResponseDTO = qnaService.selectTypeAll2(typeName, typeName2, pageRequestDTO);
        log.info("pageResponseDTO = {}", pageResponseDTO);
        model.addAttribute("pageResponseDTO", pageResponseDTO);
        model.addAttribute("typeCode", type1);
        model.addAttribute("typeName", typeName);

        return "cs/cs_inquiresList";
    }

    @GetMapping("/qna/view")
    public String qnaView(int id, Model model){
        log.info("id: {}", id);

        QnaDTO qnaDTO = qnaService.getQna(id);
        log.info("qnaDTO: {}", qnaDTO);
        model.addAttribute("qnaDTO", qnaDTO);

        return "cs/cs_inquiresView";
    }

    @GetMapping("/qna/write")
    public String qnaWrite(){
        return "cs/cs_inquiresWrite";
    }

    @PostMapping("/qna/write")
    public String qnaInsert(QnaDTO qnaDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String user_id = auth.getName();

        qnaDTO.setUser_id(user_id);
        qnaDTO.setReg_date(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        qnaDTO.setStatus("waiting");
        log.info("temporaryDTO = {}", qnaDTO);

        qnaService.insertQna(qnaDTO);

        return "redirect:/cs/qna/list";
    }
}