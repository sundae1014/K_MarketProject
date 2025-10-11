package kr.co.kmarket.controller;

import kr.co.kmarket.dto.HireDTO;
import kr.co.kmarket.service.admin.RecruitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/company")
public class CompanyController {

    private final RecruitService recruitService;

    public CompanyController(RecruitService recruitService) {
        this.recruitService = recruitService;
    }

    @GetMapping("/culture")
    public String culture(){
        return "company/culture";
    }

    @GetMapping("/index")
    public String index(){
        return "company/index";
    }

    @GetMapping("/media")
    public String media(){
        return "company/media";
    }

    @GetMapping("/recruit")
    public String recruit(Model model){
        List<HireDTO> hires = recruitService.getAllHires();

        log.info("hires={}", hires);

        model.addAttribute("hires", hires);
        return "company/recruit";
    }

    @GetMapping("/story")
    public String story(){
        return "company/story";
    }

    @GetMapping("/story/detail")
    public String detail() {
        return "company/story/detail";
    }
}
