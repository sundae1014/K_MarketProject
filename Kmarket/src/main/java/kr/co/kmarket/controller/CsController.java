package kr.co.kmarket.controller;

import kr.co.kmarket.dto.PageRequestDTO;
import kr.co.kmarket.dto.PageResponseDTO;
import kr.co.kmarket.dto.QnaDTO;
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
    private final QnaService qnaService;

    @GetMapping("/index")
    public String index(){return "/cs/cs_main";}

    @GetMapping("/notice/list")
    public String noticeList(){return "/cs/cs_noticeList";}

    @GetMapping("/notice/view")
    public String noticeView(){return "/cs/cs_noticeView";}

    @GetMapping("/faq/list")
    public String faqList(){return "/cs/cs_faqList";}

    @GetMapping("/faq/view")
    public String faqView(){return "/cs/cs_faqView";}





    @GetMapping("/qna/list")
    public String qnaList(Model  model, PageRequestDTO pageRequestDTO){
        PageResponseDTO pageResponseDTO = qnaService.selectAll(pageRequestDTO);
        log.info("pageResponseDTO = {}", pageResponseDTO);
        model.addAttribute("pageResponseDTO", pageResponseDTO);
        model.addAttribute("typeName", "회원");

        return "/cs/cs_inquiresList";
    }
    @GetMapping("/qna/list/{type1}") //임시 코드 문의글 보기
    public String qnaList(@PathVariable("type1") String type1, PageRequestDTO pageRequestDTO, Model model) {
        type1 = switch(type1){
            case "member" -> "회원";
            case "couponAnd" -> "쿠폰 / 이벤트";
            case "orderAndPay" -> "주문 / 결제";
            case "deliver" -> "배송";
            case "cancelAndReturnAndExchange" -> "취소 / 반품 / 교환";
            case "traverlAndAccommodationAndAviation" -> "여행 / 숙박 / 항공";
            case "Safetrade" -> "안전거래";
            default -> throw new IllegalStateException("Unexpected value: " + type1);
        };

        PageResponseDTO pageResponseDTO = qnaService.selectTypeAll(type1, pageRequestDTO);
        log.info("pageResponseDTO = {}", pageResponseDTO);
        model.addAttribute("pageResponseDTO", pageResponseDTO);
        model.addAttribute("typeName", type1);
        return "/cs/cs_inquiresList";
    }




    @GetMapping("/qna/view")
    public String qnaView(int id, Model model){
        log.info("id: {}", id);

        QnaDTO qnaDTO = qnaService.getQna(id);
        log.info("qnaDTO: {}", qnaDTO);
        model.addAttribute("qnaDTO", qnaDTO);

        return "/cs/cs_inquiresView";
    }

    @GetMapping("/qna/write")
    public String qnaWrite(){
        return "/cs/cs_inquiresWrite";
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
