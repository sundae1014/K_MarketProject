package kr.co.kmarket.mapper.admin;

import kr.co.kmarket.dto.MemberDTO;
import kr.co.kmarket.dto.PageRequestDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShopMapper {

    public List<MemberDTO> findAll(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO); //페이지네이션 테스트
    public int selectCountTotal(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO); //페이지네이션 테스트

    public List<MemberDTO> searchAll(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO); //검색 테스트
    public int searchCount(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO); //검색 테스트

    void update(@Param("custNumber") Long custNumber, @Param("operation") String operation);
    void delete(@Param("list") List<Long> idList);
}
