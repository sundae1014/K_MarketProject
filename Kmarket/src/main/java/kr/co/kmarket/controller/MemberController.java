package kr.co.kmarket.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.kmarket.dto.MemberDTO;
import kr.co.kmarket.service.EmailService;
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
    private final EmailService emailService;

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

    @PostMapping("/find/userId")
    public String userId(String name, String email, Model model){
        log.info("name: {}, email: {}", name, email);
        MemberDTO findIdInfo = memberService.getUserIdInfo(name, email);
        if(findIdInfo == null){
            model.addAttribute("findIdmsg", "회원정보가 일치하지 않습니다.");
        } else {
            model.addAttribute("findIdInfo", findIdInfo);
        }
        return "member/find/resultId";
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

    @PostMapping("/email/send")
    @ResponseBody
    public ResponseEntity<String> sendEmail(@RequestBody Map<String,String> req){
        String email = req.get("email");
        String mode = req.get("mode"); // "join" 또는 "find"
        int count = memberService.countUser("email", email);

        if("join".equals(mode) && count > 0){
            return ResponseEntity.badRequest().body("이미 존재하는 이메일입니다.");
        }

        if("find".equals(mode) && count == 0){
            return ResponseEntity.badRequest().body("존재하지 않는 이메일입니다.");
        }

        emailService.sendCode(email); // 조건 맞으면 발송
        return ResponseEntity.ok("인증 코드 발송 완료");
    }

}
