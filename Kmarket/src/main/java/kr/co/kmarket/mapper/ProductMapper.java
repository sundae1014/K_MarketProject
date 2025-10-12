package kr.co.kmarket.mapper;

import kr.co.kmarket.dto.ProductDTO;
import kr.co.kmarket.dto.ProductNoticeDTO;
import kr.co.kmarket.dto.ProductReviewDTO;
import kr.co.kmarket.dto.SearchDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper {

    // 키워드 검색
    public List<ProductDTO> selectProductsByKeyword(String keyword);

    // 키워드 + 카테고리 검색
    public List<ProductDTO> selectProductsByCategory(@Param("keyword") String keyword,  @Param("cate_cd") int cate_cd);

    // SearchDTO 기반 검색
    public List<ProductDTO> searchProducts(SearchDTO searchDTO);

    ProductDTO selectProductByNo(int prodNo);

    ProductNoticeDTO selectProductNoticeByNo(int prodNo);

    List<ProductReviewDTO> selectProductReviews(int prodNo);

}
