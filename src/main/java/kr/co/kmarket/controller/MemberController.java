package kr.co.kmarket.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.co.kmarket.dto.MemberDTO;
import kr.co.kmarket.dto.PolicyDTO;
import kr.co.kmarket.service.EmailService;
import kr.co.kmarket.service.HpService;
import kr.co.kmarket.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final EmailService emailService;
    private final HpService hpService;

    @GetMapping("/join")
    public String join() {
        return "member/join";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "redirect_uri", required = false) String redirectUri,
                        HttpSession session) {
        if (redirectUri != null) {
            session.setAttribute("redirect_uri", redirectUri);
        }
        return "member/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("custid") String custid,
                        @RequestParam("pw") String pw,
                        HttpSession session,
                        RedirectAttributes ra) {

        MemberDTO member = memberService.login(custid, pw);

        if (member != null) {
            session.setAttribute("member", member);
            log.info("로그인 성공: {}", member);
            return "redirect:/"; // 로그인 성공 시 메인으로 이동
        } else {
            ra.addFlashAttribute("msg", "아이디 또는 비밀번호가 잘못되었습니다.");
            return "redirect:/member/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 전체 삭제
        return "redirect:/"; // 메인으로 이동
    }


    @GetMapping("/register")
    public String register() {
        return "member/register";
    }

    @PostMapping("/register")
    public String register(MemberDTO memberDTO, HttpServletRequest req) {
        log.info(memberDTO.toString());
        memberDTO.setAuth(1);
        memberDTO.setPoint(0);
        memberService.save(memberDTO);
        return "redirect:/member/login";
    }

    @GetMapping("/registerSeller")
    public String registerSeller() {
        return "member/registerSeller";
    }

    @PostMapping("/registerSeller")
    public String registerSeller(MemberDTO memberDTO, HttpServletRequest req) {
        log.info(memberDTO.toString());
        memberDTO.setAuth(3);
        memberDTO.setOperation("ready"); //관리자 상점목록에서 판매자 운영준비 기본값 추가
        memberService.save(memberDTO);
        return "redirect:/member/login";
    }


    @GetMapping("/signup")
    public String signup(@RequestParam String type, Model model) {
        model.addAttribute("type", type);

        List<PolicyDTO> policies = memberService.getAllPolicies();

        log.info("policies:" + policies);

        model.addAttribute("policies", policies);
        return "member/signup";
    }

    @GetMapping("/find/changePassword")
    public String changePassword() {
        return "member/find/changePassword";
    }

    @PostMapping("/find/changePassword")
    public String changePassword(@RequestParam("custid") String custid, @RequestParam("pw") String pw, Model model, RedirectAttributes ra) {
        log.info("custid: {}, pw: {}", custid, pw);
        boolean isChanged = memberService.changePw(custid, pw);
        if (isChanged) {
            ra.addFlashAttribute("msg", "비밀번호가 성공적으로 변경되었습니다.");
            return "redirect:/member/login";
        } else {
            model.addAttribute("msg", "비밀번호 변경에 실패했습니다. 다시 시도해주세요.");
            return "member/changePassword";
        }

    }

    @GetMapping("/find/password")
    public String password() {
        return "member/find/password";
    }

    @PostMapping("/find/password")
    public String password(@RequestParam("authMethod") int authMethod,
                           String name,
                           @RequestParam(value = "email", required = false) String email,
                           @RequestParam(value = "hp", required = false) String hp,
                           Model model) {
        log.info("name: {}, email: {}, hp: {}", name, email, hp);
        if (authMethod == 1) {
            MemberDTO findIdInfo = memberService.getUserIdInfo(name, email);
            if (findIdInfo == null) {
                model.addAttribute("msg", "회원정보가 일치하지 않습니다.");
                return "member/find/password";
            } else {
                model.addAttribute("findIdInfo", findIdInfo);
                return "member/find/changePassword";
            }
        } else if (authMethod == 2) {
            MemberDTO findIdInfo = memberService.getUserIdInfoHp(name, hp);
            if (findIdInfo == null) {
                model.addAttribute("msg", "회원정보가 일치하지 않습니다.");
                return "member/find/password";
            }
            model.addAttribute("findIdInfo", findIdInfo);
            return "member/find/changePassword";
        }
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
    public String userId(@RequestParam("authMethod") int authMethod,
                         String name,
                         @RequestParam(value = "email", required = false) String email,
                         @RequestParam(value = "hp", required = false) String hp,
                         Model model){

        log.info("name: {}, email: {}, hp: {}", name, email, hp);
        if(authMethod == 1){
            MemberDTO findIdInfo = memberService.getUserIdInfo(name, email);
            if(findIdInfo == null){
                model.addAttribute("msg", "회원정보가 일치하지 않습니다.");
                return "member/find/userId";
            } else {
                model.addAttribute("findIdInfo", findIdInfo);
                return "member/find/resultId";
            }
        }else if(authMethod == 2){
            MemberDTO finIdInfo = memberService.getUserIdInfoHp(name, hp);
            if(finIdInfo == null){
                model.addAttribute("msg", "회원정보가 일치하지 않습니다.");
                return "member/find/userId";
            }else{
                model.addAttribute("findIdInfo", finIdInfo);
                return "member/find/resultId";
            }
        }
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

    @PostMapping("/hp/send")
    @ResponseBody
    public ResponseEntity<String> sendHp(@RequestBody Map<String,String> req) {
        String hp = req.get("hp");
        String mode = req.get("mode"); // "join" 또는 "find"
        int count = memberService.countUser("hp", hp);

        if("join".equals(mode) && count > 0){
            return ResponseEntity.badRequest().body("이미 존재하는 휴대폰입니다..");
        }

        if("find".equals(mode) && count == 0){
            return ResponseEntity.badRequest().body("존재하지 않는 휴대폰입니다.");
        }
        hpService.sendCode(hp); // 조건 맞으면 발송
        return ResponseEntity.ok("인증 코드 발송 완료");
    }


}
