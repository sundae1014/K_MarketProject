package kr.co.kmarket.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.co.kmarket.dto.MemberDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
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

        String userIdToStore = member.getCustid();

        // 세션 키는 QnA Controller에 맞게 "user_id"로 저장합니다.
        session.setAttribute("user_id", userIdToStore);

        // 추가: Thymeleaf와 JS에서도 쓸 수 있게 전체 member 객체 세션 등록
        session.setAttribute("member", member);


        RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

        RequestCache requestCache = new HttpSessionRequestCache();
        SavedRequest savedRequest = requestCache.getRequest(request, response);

        // 로그인 이전 요청 했던 주소 이동
        String targetUrl = null;
        if (savedRequest != null) {
            // Spring Security가 저장한 원래 요청 주소
            targetUrl = savedRequest.getRedirectUrl();
        }

        // 2. targetUrl이 있으면 해당 주소로 리다이렉트하고 종료
        if (targetUrl != null && !targetUrl.isEmpty()) {
            redirectStrategy.sendRedirect(request, response, targetUrl);
            return; // 리다이렉트 완료 후 메서드 종료
        }


        String redirectUri = null;
        if (session != null) {
            redirectUri = (String) session.getAttribute("redirect_uri");
            session.removeAttribute("redirect_uri");
        }

        if (redirectUri != null) {
            redirectStrategy.sendRedirect(request, response, redirectUri);
        } else {
            if(member.getAuth() == 2 || member.getAuth() == 3) {
                redirectStrategy.sendRedirect(request, response, "/admin");
            }else{
                redirectStrategy.sendRedirect(request, response, "/");
            }
        }
    }
}
