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
public class SocialLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        // 소셜 로그인 전용 principal 처리
        CustomOauth2UserDetails oauthUser = (CustomOauth2UserDetails) authentication.getPrincipal();
        MemberDTO member = oauthUser.getMember();

        // 세션 저장
        HttpSession session = request.getSession();
        session.setAttribute("cust_number", member.getCust_number());
        session.setAttribute("user_id", member.getCustid());
        session.setAttribute("member", member);

        log.info("Social login successful: {}", member);

        RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

        // 1. 로그인 이전 요청 주소 확인
        RequestCache requestCache = new HttpSessionRequestCache();
        SavedRequest savedRequest = requestCache.getRequest(request, response);

        if (savedRequest != null) {
            String targetUrl = savedRequest.getRedirectUrl();
            redirectStrategy.sendRedirect(request, response, targetUrl);
            return; // 리다이렉트 완료 후 종료
        }

        // 2. 세션에 저장된 redirect_uri 확인
        String redirectUri = (String) session.getAttribute("redirect_uri");
        if (redirectUri != null) {
            session.removeAttribute("redirect_uri");
            redirectStrategy.sendRedirect(request, response, redirectUri);
            return;
        }

        // 3. 기본 리다이렉트
        if (member.getAuth() == 2 || member.getAuth() == 3) {
            redirectStrategy.sendRedirect(request, response, "/admin");
        } else {
            redirectStrategy.sendRedirect(request, response, "/");
        }
    }
}
