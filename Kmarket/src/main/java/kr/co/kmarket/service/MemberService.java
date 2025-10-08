package kr.co.kmarket.service;

import kr.co.kmarket.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import kr.co.kmarket.mapper.MemberMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {


    private final MemberMapper memberMapper;

    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public void save(MemberDTO memberDTO){
        String encodedPass = passwordEncoder.encode(memberDTO.getPw());
        memberDTO.setPw(encodedPass);
        memberMapper.insertMember(memberDTO);
    }

    public MemberDTO getUser(String custid){
        return memberMapper.findByCustid(custid);
    }

    public MemberDTO getUserIdInfo(String name, String email){
        return memberMapper.findCustIdInfo(name, email);
    }

    public int countUser(String type, String value){
        int count = 0;
        if(type.equals("custid")){
            count = memberMapper.countByCustid(value);
        } else if(type.equals("email")){
            count = memberMapper.countByEmail(value);
//            if(count == 0){
//                emailService.sendCode(value);
//            }
        } else if(type.equals("hp")){
            count = memberMapper.countByHp(value);
        }
        return count;
    }
}