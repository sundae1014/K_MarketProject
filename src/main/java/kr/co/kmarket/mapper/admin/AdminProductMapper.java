package kr.co.kmarket.mapper.admin;

import kr.co.kmarket.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdminProductMapper {
    public int findMaxProdNumber();
    public int findByProdNumber(int prod_number);
    public List<CategoryDTO> findCategory(String upCateCd);
    public List<Map<String, Object>> findAll(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO);
    public int selectCount(@Param("pageRequestDTO")  PageRequestDTO pageRequestDTO);

    public List<Map<String, Object>> searchAll(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO);
    public int searchCount(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO);

    public void insertProduct(ProductDTO productDTO);
//    public void insertProductNotice(ProductNoticeDTO productNoticeDTO);
//    public void insertProductManagement(ProductMangementDTO productMangementDTO);

    public void deletePoint(@Param("prod_number")  long prod_number);
    public void deleteProductManagement(@Param("prod_number")  long prod_number);
    public void deleteProduct(@Param("prod_number")  long prod_number);
}
