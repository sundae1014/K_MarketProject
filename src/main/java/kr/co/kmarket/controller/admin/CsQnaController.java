package kr.co.kmarket.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/admin/cs")
@Controller
public class CsQnaController {
    @GetMapping("/qna/list")
    public String qnaList() {return "admin/cs/qna/inquryList";}

    @GetMapping("/qna/reply")
    public String qnaReply() {return "admin/cs/qna/inquryAnswer";}

    @GetMapping("/qna/view")
    public String qnaView() {return "admin/cs/qna/inquryView";}
}
