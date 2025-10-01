package kr.co.kmarket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/company")
public class CompanyController {

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
    public String recruit(){
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
