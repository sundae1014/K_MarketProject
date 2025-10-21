package kr.co.kmarket.mapper.admin;

import kr.co.kmarket.dto.PageRequestDTO;
import kr.co.kmarket.dto.PointDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdminPointMapper {
    public PointDTO findById(@Param("point_id") String point_id);
    public List<Map<String, Object>> findAll(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO);
    public int selectCount(@Param("pageRequestDTO")  PageRequestDTO pageRequestDTO);

    public List<Map<String, Object>> searchAll(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO);
    public int searchCountTotal(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO);

    void updatePoint(@Param("cust_number") int cust_number, @Param("balance") int balance);
    void delete(@Param("point_id") String point_id);
}
