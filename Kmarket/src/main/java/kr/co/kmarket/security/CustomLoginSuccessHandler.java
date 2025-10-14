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

        session.setAttribute("cust_number", member.getCust_number());

        // 🚨 [핵심 수정] custid 필드에 맞는 getCustid() 사용
        String userIdToStore = member.getCustid();

        // 세션 키는 QnA Controller에 맞게 "user_id"로 저장합니다.
        session.setAttribute("user_id", userIdToStore);

        // 로그를 확인하여 실제 ID가 찍히는지 확인해 보세요!
        log.info("로그인 성공! user_id 세션에 저장된 값: {}", userIdToStore);

        response.sendRedirect("/kmarket");
    }
}
