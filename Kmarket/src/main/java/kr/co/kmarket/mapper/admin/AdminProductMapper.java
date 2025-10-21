package kr.co.kmarket.mapper.admin;

import kr.co.kmarket.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminProductMapper {
    public List<CategoryDTO> findCategory(String upCateCd);
    public List<ProductDTO> findAll(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO);
    public int selectCount(@Param("pageRequestDTO")  PageRequestDTO pageRequestDTO);

    public void insertProduct(ProductDTO productDTO);
    public void insertProductNotice(ProductNoticeDTO productNoticeDTO);
    public void insertProductManagement(ProductMangementDTO productMangementDTO);
}
