package kr.co.kmarket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/member")
public class MemberController {

    @GetMapping("/join")
    public String join(){
        return "member/join";
    }

    @GetMapping("/login")
    public String login(){
        return "member/login";
    }

    @GetMapping("/register")
    public String register(){
        return "member/register";
    }

    @GetMapping("/registerSeller")
    public String registerSeller(){
        return "member/registerSeller";
    }

    @GetMapping("/signup")
    public String signup(@RequestParam String type, Model model){
        model.addAttribute("type",type);
        return "member/signup";
    }

    @GetMapping("/find/changePassword")
    public String changePassword(){
        return "member/find/changePassword";
    }

    @GetMapping("/find/password")
    public String password(){
        return "member/find/password";
    }

    @GetMapping("/find/resultId")
    public String resultId(){
        return "member/find/resultId";
    }

    @GetMapping("/find/userId")
    public String userId(){
        return "member/find/userId";
    }
}
