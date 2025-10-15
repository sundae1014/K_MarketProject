package kr.co.kmarket.controller.admin;

import kr.co.kmarket.dto.FaqDTO;
import kr.co.kmarket.service.FaqService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/cs")
@Controller
public class CsFaqController {
    private final FaqService faqService;

    @GetMapping("/faq/list")
    public String faqList() {return "admin/cs/faq/FAQList";}

    @GetMapping("/faq/view")
    public String faqView() {return "admin/cs/faq/FAQView";}

    @GetMapping("/faq/write")
    public String faqWrite() {return "admin/cs/faq/FAQWrite";}

    @PostMapping("/faq/write")
    public String faqWrite(FaqDTO faqDTO) {
        log.info("faqDTO = {}",  faqDTO);
        faqDTO.setReg_date(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        faqService.insertFaq(faqDTO);

        return "admin/cs/faq/FAQList";
    }

    @GetMapping("/faq/modify")
    public String faqModify() {return "admin/cs/faq/FAQModify";}
}
