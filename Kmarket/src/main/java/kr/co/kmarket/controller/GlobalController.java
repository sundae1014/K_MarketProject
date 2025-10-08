package kr.co.kmarket.controller;


import kr.co.kmarket.dto.MemberDTO;
import kr.co.kmarket.security.MyUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(basePackages = {"kr.co.kmarket.controller"})
@RequiredArgsConstructor
public class GlobalController {
    @ModelAttribute("member")
    public MemberDTO addUserToModel(@AuthenticationPrincipal MyUserDetails myMember) {
        if (myMember != null) {
            return myMember.getMemberDTO();
        }
        return new MemberDTO();
    }

}
