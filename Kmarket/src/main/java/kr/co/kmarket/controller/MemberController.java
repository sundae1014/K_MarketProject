package kr.co.kmarket.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.kmarket.dto.MemberDTO;
import kr.co.kmarket.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

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

    @PostMapping("/register")
    public String register(MemberDTO memberDTO, HttpServletRequest req){
        memberDTO.setAuth(1);
        memberDTO.setPoint(0);
        memberService.save(memberDTO);
        return "redirect:/member/login";
    }

    @GetMapping("/registerSeller")
    public String registerSeller(){
        return "member/registerSeller";
    }

    @PostMapping("/registerSeller")
    public String registerSeller(MemberDTO memberDTO, HttpServletRequest req){
        memberDTO.setAuth(3);
        memberService.save(memberDTO);
        return "redirect:/member/login";
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


    // API 요청 메서드
    @ResponseBody
    @GetMapping("/{type}/{value}")
    public ResponseEntity<Map<String, Integer>> getUserCount(@PathVariable("type") String type,
                                                             @PathVariable("value") String value){
        log.info("type = {}, value = {}", type, value);

        int count = memberService.countUser(type, value);


        // Json 생성
        Map<String,Integer> map = Map.of("count", count);

        return ResponseEntity.ok(map);
    }
}
