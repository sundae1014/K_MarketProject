package kr.co.kmarket.controller;

import kr.co.kmarket.dto.TemporaryDTO;
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
    public String qnaList(){
        return "/cs/cs_inquiresList";
    }
    @GetMapping("/qna/list/{type1}") //임시 코드 문의글 보기
    public String qnaList(@PathVariable("type1") String type1, Model model) {
        log.info("type1: {}", type1);
        //List<TemporaryDTO> dtoList = qnaService.findTypeAll(type1);
        //model.addAttribute("dtoList", dtoList);

        return "/cs/cs_inquiresList";
    }




    @GetMapping("/qna/view")
    public String qnaView(){return "/cs/cs_inquiresView";}

    @GetMapping("/qna/write")
    public String qnaWrite(){
        return "/cs/cs_inquiresWrite";
    }





    @PostMapping("/qna/write") //임시 코드
    public String qnaInsert(TemporaryDTO temporaryDTO) { //임시 코드, 문의글 작성
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String user_id = auth.getName();
        log.info("user_id = {}", user_id);
        temporaryDTO.setUser_id(user_id);

        log.info("temporaryDTO = {}", temporaryDTO);
        //qnaService.insertQna(temporaryDTO); //나중에 사용할 코드

        return "redirect:/cs/qna/list";
    }
}
