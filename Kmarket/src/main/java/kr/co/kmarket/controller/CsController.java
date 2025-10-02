package kr.co.kmarket.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/cs")
@Controller
public class CsController {

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
    public String qnaList(){return "/cs/cs_inquiresList";}

    @GetMapping("/qna/view")
    public String qnaView(){return "/cs/cs_inquiresView";}

    @GetMapping("/qna/write")
    public String qnaWrite(){return "/cs/cs_inquiresWrite";}
}
