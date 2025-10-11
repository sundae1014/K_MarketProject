package kr.co.kmarket.mapper.admin;

import kr.co.kmarket.dto.MemberDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShopMapper {

    public List<MemberDTO> findAll();
    void update(@Param("custNumber") Long custNumber, @Param("operation") String operation);
}
