package kr.co.kmarket.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.co.kmarket.dto.MemberDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        MemberDTO member = userDetails.getMemberDTO();

        HttpSession session = request.getSession();
        session.setAttribute("cust_number", member.getCust_number());  // 여기 cust_number 사용

        log.info("로그인 성공! cust_number 세션에 저장: {}", member.getCust_number());

        response.sendRedirect("/kmarket");
    }
}
