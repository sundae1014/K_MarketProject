package kr.co.kmarket.controller.admin;

import kr.co.kmarket.dto.PageRequestDTO;
import kr.co.kmarket.dto.PageResponseDTO;
import kr.co.kmarket.dto.QnaDTO;
import kr.co.kmarket.service.QnaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/cs")
@Controller
public class CsQnaController {
    private final  QnaService qnaService;

    @GetMapping("/qna/list")
    public String qnaList(Model model, PageRequestDTO pageRequestDTO) {
        PageResponseDTO pageResponseDTO = qnaService.selectAll(pageRequestDTO);
        log.info("pageResponseDTO={}", pageResponseDTO);

        model.addAttribute("pageResponseDTO", pageResponseDTO);

        return "admin/cs/qna/inquryList";
    }
    @GetMapping("/qna/list/{type1}/{type2}")
    public String qnaList(@PathVariable("type1") String type1, @PathVariable("type2") String type2, PageRequestDTO pageRequestDTO, Model model) {
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
        log.info("typeName={}, typeName2={}", typeName, typeName2);
        PageResponseDTO pageResponseDTO = qnaService.selectTypeAll2(typeName, typeName2, pageRequestDTO);
        model.addAttribute("pageResponseDTO", pageResponseDTO);

        return "admin/cs/qna/inquryList";
    }

    @DeleteMapping("/qna/list")
    @ResponseBody
    public ResponseEntity<Void> qnaDelete(@RequestBody List<Long> idList) { //관리자 문의하기 목록의 삭제
        log.info("idList = " + idList);
        qnaService.remove(idList);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/qna/delete")
    public String deleteQna(@RequestParam Long id) { //관리자 문의 답변,보기 삭제
        log.info("id = {}", id);
        qnaService.remove2(id);

        return "redirect:/admin/cs/qna/list";
    }


    @GetMapping("/qna/reply")
    public String qnaReply(int id, Model model) {
        log.info("id: {}", id);

        QnaDTO qnaDTO = qnaService.getQna(id);
        log.info("qnaDTO: {}", qnaDTO);
        model.addAttribute("qnaDTO", qnaDTO);

        return "admin/cs/qna/inquryAnswer";
    }

    @PostMapping("/qna/reply")
    public String qnaReply(QnaDTO qnaDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String admin_id = auth.getName();

        qnaDTO.setAdmin_id(admin_id);
        qnaDTO.setStatus("complete");
        log.info("qnaDTO: {}", qnaDTO);

        qnaService.insertAnswer(qnaDTO);

        return "redirect:/admin/cs/qna/list";
    }

    @GetMapping("/qna/view")
    public String qnaView(int id, Model model) {
        log.info("id: {}", id);

        QnaDTO qnaDTO = qnaService.getQna(id);
        log.info("qnaDTO: {}", qnaDTO);
        model.addAttribute("qnaDTO", qnaDTO);

        return "admin/cs/qna/inquryView";
    }

}
