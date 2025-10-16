package kr.co.kmarket.controller.admin;

import kr.co.kmarket.dto.FaqDTO;
import kr.co.kmarket.dto.PageRequestDTO;
import kr.co.kmarket.service.FaqService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/cs")
@Controller
public class CsFaqController {
    private final FaqService faqService;

    @GetMapping("/faq/list")
    public String faqList(Model model) {
        List<FaqDTO> dtoList = faqService.selectAll();
        log.info("dtoList={}", dtoList);
        model.addAttribute("dtoList", dtoList);

        return "admin/cs/faq/FAQList";
    }

    @GetMapping("/faq/list/{type1}/{type2}")
    public String faqList(@PathVariable("type1") String type1, @PathVariable("type2") String type2, Model model) {
        log.info("type1={}, type2={}", type1,type2);
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
        model.addAttribute("dtoList", faqList);

        return "admin/cs/faq/FAQList";
    }

    @DeleteMapping("/faq/list")
    @ResponseBody
    public ResponseEntity<Void> faqDelete(@RequestBody List<Long> idList) {
        log.info("idList={}", idList);
        faqService.removeAll(idList);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/faq/view")
    public String faqView(int id, Model model) {
        log.info("id: {}", id);

        FaqDTO faqDTO = faqService.getFaq(id);
        log.info("faqDTO={}", faqDTO);
        model.addAttribute("faqDTO", faqDTO);

        faqDTO.setViews(faqDTO.getViews() + 1); //조회수 증가
        faqService.updateViews(faqDTO);

        return "admin/cs/faq/FAQView";
    }

    @GetMapping("/faq/delete")
    public String deleteFaq(@RequestParam Long id) {
        log.info("id: {}", id);
        faqService.remove(id);

        return "redirect:/admin/cs/faq/list";
    }

    @GetMapping("/faq/write")
    public String faqWrite() {return "admin/cs/faq/FAQWrite";}

    @PostMapping("/faq/write")
    public String faqWrite(FaqDTO faqDTO) {
        log.info("faqDTO = {}",  faqDTO);
        faqDTO.setReg_date(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        faqService.insertFaq(faqDTO);

        return "redirect:/admin/cs/faq/list";
    }

    @GetMapping("/faq/modify")
    public String faqModify(int id, Model model) {
        log.info("id: {}", id);
        FaqDTO faqDTO = faqService.getFaq(id);
        log.info("수정전 데이터 = {}", faqDTO);
        model.addAttribute("faqDTO", faqDTO);

        return "admin/cs/faq/FAQModify";
    }

    @PostMapping("/faq/modify")
    public String faqModify(FaqDTO faqDTO) {
        log.info("수정할 데이터 = {}",  faqDTO);
        faqService.updateFaq(faqDTO);

        return "redirect:/admin/cs/faq/list";
    }
}
