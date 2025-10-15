package kr.co.kmarket.mapper;

import kr.co.kmarket.dto.MemberDTO;
import kr.co.kmarket.dto.PolicyDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MemberMapper {

    void insertMember(MemberDTO member);

    MemberDTO findByCustid(@Param("custid") String custid);

    MemberDTO findCustIdInfo(@Param("name") String name, @Param("email") String email);

    MemberDTO findCustIdInfoHp(@Param("name") String name, @Param("hp") String hp);

    int countByCustid(@Param("custid") String custid);
    int countByEmail(@Param("email") String email);
    int countByHp(@Param("hp") String hp);

    int updatePw(@Param("custid") String custid, @Param("pw") String pw);

    public List<PolicyDTO> selectAllPolicy();

}
