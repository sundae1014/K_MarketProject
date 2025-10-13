package kr.co.kmarket.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin/member")
@Controller
public class AdminMemberController {

    @GetMapping("/list")
    public String list() {return "admin/member/admin_memberList";}

    @GetMapping("/point")
    public String point() {return "admin/member/admin_pointManagement";}
}
