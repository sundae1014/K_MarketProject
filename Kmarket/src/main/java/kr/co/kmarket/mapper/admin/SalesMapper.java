package kr.co.kmarket.mapper.admin;

import kr.co.kmarket.dto.PageRequestDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface SalesMapper {

    public List<Map<String, Object>> findAll(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO);
    public int selectCount(@Param("pageRequestDTO")  PageRequestDTO pageRequestDTO);

    public List<Map<String, Object>> findTypeAll(@Param("type") String type, @Param("pageRequestDTO") PageRequestDTO pageRequestDTO);
    public int selectCountTotal(@Param("type") String type, @Param("pageRequestDTO")  PageRequestDTO pageRequestDTO);
}
