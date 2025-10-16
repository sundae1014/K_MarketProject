package kr.co.kmarket.service;

import kr.co.kmarket.dto.MemberDTO;
import kr.co.kmarket.dto.PolicyDTO;
import lombok.RequiredArgsConstructor;
import kr.co.kmarket.mapper.MemberMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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
    public void saveSocial(MemberDTO memberDTO){
        memberDTO.setPw(passwordEncoder.encode("SOCIAL_USER"));
        memberMapper.insertMember(memberDTO);
    }

    public MemberDTO getUser(String custid){
        return memberMapper.findByCustid(custid);
    }

    public MemberDTO getUserIdInfo(String name, String email){
        return memberMapper.findCustIdInfo(name, email);
    }

    public MemberDTO getUserIdInfoHp(String name, String hp){
        return memberMapper.findCustIdInfoHp(name, hp);
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

    public boolean changePw(String custid, String pw){
        String encodedPass = passwordEncoder.encode(pw);
        int result = memberMapper.updatePw(custid, encodedPass);
        return result > 0;
    }

    public List<PolicyDTO> getAllPolicies(){
        return memberMapper.selectAllPolicy();
    }

    public MemberDTO login(String custid, String pw) {
        return memberMapper.login(custid, pw);
    }

}