package kr.co.kmarket.mapper;

import kr.co.kmarket.dto.MemberDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper {

    void insertMember(MemberDTO member);

    MemberDTO findByCustid(@Param("custid") String custid);

    int countByCustid(@Param("custid") String custid);
    int countByEmail(@Param("email") String email);
    int countByHp(@Param("hp") String hp);
}
