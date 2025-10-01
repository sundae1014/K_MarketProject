package kr.co.kmarket.controller.admin;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/cs/recruit")
public class CsRecruitController {
    @GetMapping("/list")
    public String list(){
        return "admin/cs/recruit/list";
    }
}
