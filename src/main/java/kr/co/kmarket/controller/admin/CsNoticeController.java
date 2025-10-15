package kr.co.kmarket.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/admin/cs")
@Controller
public class CsNoticeController {

    @GetMapping("/notice/list")
    public String noticeList() {return "admin/cs/notice/noticeList";}

    @GetMapping("/notice/view")
    public String noticeView() {return "admin/cs/notice/noticeView";}

    @GetMapping("/notice/write")
    public String noticeWrite() {return "admin/cs/notice/noticeWrite";}

    @GetMapping("/notice/modify")
    public String noticeModify() {return "admin/cs/notice/noticeModify";}


}
