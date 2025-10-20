package kr.co.kmarket.mapper.admin;

import kr.co.kmarket.dto.AdminMemberDTO;
import kr.co.kmarket.dto.PageRequestDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdminMemberMapper {
    public AdminMemberDTO findById(@Param("custId") String custId);
    public List<Map<String, Object>> findAll(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO);
    public int selectCount(@Param("pageRequestDTO")  PageRequestDTO pageRequestDTO);

    public List<Map<String, Object>> searchAll(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO);
    public int searchCountTotal(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO);

    void update(@Param("custId") String custId, @Param("operation") String operation);
    void update2(@Param("custId") String custId, @Param("rating") String rating);
    void update3(AdminMemberDTO adminMemberDTO);
}
