package kr.co.kmarket.service;

import kr.co.kmarket.dto.MemberDTO;
import kr.co.kmarket.security.CustomOauth2UserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private final MemberService memberService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("OAuth2UserRequest: " + userRequest);
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        log.info("CustomOauth2UserService attributes: " + attributes);
        MemberDTO member = null;
        String custid = null;
        String memberEmail = null;
        String memberName = null;

        if (attributes.get("sub") != null) { // 구글
            String googleId = (String) attributes.get("sub");
            memberEmail = (String) attributes.get("email");
            memberName = (String) attributes.get("name");
            custid = "google_" + googleId;
        } else if (attributes.get("response") != null) { //네이버
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            String naverId = (String) response.get("id");
            memberEmail = (String) response.get("email");
            memberName = (String) response.get("name");
            custid = "naver_" + naverId;
        }else if(attributes.get("id") != null){ // 카카오
            String kakaoId = String.valueOf(attributes.get("id"));
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

            memberName = (String) profile.get("nickname"); // 카카오 닉네임을 이름으로 사용
            custid = "kakao_" + kakaoId;
            memberEmail = null;

            log.info("CustomOauth2UserService profile: " + profile);
        }
        // MemberDTO 빌드 후 DB 저장 (회원가입 처리)
        member = memberService.getUser(custid);
        log.info("CustomOauth2UserService member: " + member);
        if (member == null) {
            member = MemberDTO.builder()
                    .custid(custid)
                    .name(memberName)
                    .email(memberEmail)
                    .stat(1)
                    .auth(1)
                    .build();
            memberService.saveSocial(member);
        }
        log.info("CustomOauth2UserService member: " + member);
        return new CustomOauth2UserDetails(member, attributes);

    }
}