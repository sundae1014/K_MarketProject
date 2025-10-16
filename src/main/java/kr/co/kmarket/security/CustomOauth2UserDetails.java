package kr.co.kmarket.security;

import kr.co.kmarket.dto.MemberDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class CustomOauth2UserDetails implements OAuth2User {

    private final MemberDTO member;
    private final Map<String, Object> attributes;

    public CustomOauth2UserDetails(MemberDTO member, Map<String, Object> attributes) {
        this.member = member;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + member.getAuth()));
    }


    @Override
    public String getName() {
        //소셜 로그인 시 name을 MemberDTO 기준으로 반환
        return member.getName();
    }

    public MemberDTO getMember() {
        return member;
    }
}