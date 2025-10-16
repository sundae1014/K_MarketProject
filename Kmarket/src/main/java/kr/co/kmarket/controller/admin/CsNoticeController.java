package kr.co.kmarket.controller.admin;

import kr.co.kmarket.dto.NoticeDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/cs")
@Controller
public class CsNoticeController { //관리자 고객센터 공지사항 구현하기 일단 작성하기부터

    @GetMapping("/notice/list")
    public String noticeList() {return "admin/cs/notice/noticeList";}

    @GetMapping("/notice/view")
    public String noticeView() {return "admin/cs/notice/noticeView";}

    @GetMapping("/notice/write")
    public String noticeWrite() {return "admin/cs/notice/noticeWrite";}

    @PostMapping("/notice/write")
    public String noticeWrite(NoticeDTO noticeDTO) {
        log.info("noticeDTO={}", noticeDTO);
        return "admin/cs/notice/noticeWrite";
    }

    @GetMapping("/notice/modify")
    public String noticeModify() {return "admin/cs/notice/noticeModify";}


}
