package kr.co.kmarket.security;


import kr.co.kmarket.dto.MemberDTO;
import kr.co.kmarket.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MyUserDetailsService implements UserDetailsService {

    private final MemberMapper memberMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 사용자가 입력한 아이디로 사용자 조회, 비밀번호에 대한 검증은 이전 컴포넌트인 AuthenticationProvider에서 수행
        MemberDTO memberDTO = memberMapper.findByCustid(username);

        if (memberDTO == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return MyUserDetails.builder()
                .memberDTO(memberDTO)
                .build();
    }
}
