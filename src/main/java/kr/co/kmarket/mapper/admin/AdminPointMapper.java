package kr.co.kmarket.mapper.admin;

import kr.co.kmarket.dto.PageRequestDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdminPointMapper {
    public List<Map<String, Object>> findAll(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO);
    public int selectCount(@Param("pageRequestDTO")  PageRequestDTO pageRequestDTO);

    public List<Map<String, Object>> searchAll(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO);
    public int searchCountTotal(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO);
}
