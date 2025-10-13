package kr.co.kmarket.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/admin/cs")
@Controller
public class CsFaqController {

    @GetMapping("/faq/list")
    public String faqList() {return "admin/cs/faq/FAQList";}

    @GetMapping("/faq/view")
    public String faqView() {return "admin/cs/faq/FAQView";}

    @GetMapping("/faq/write")
    public String faqWrite() {return "admin/cs/faq/FAQWrite";}

    @GetMapping("/faq/modify")
    public String faqModify() {return "admin/cs/faq/FAQModify";}
}
