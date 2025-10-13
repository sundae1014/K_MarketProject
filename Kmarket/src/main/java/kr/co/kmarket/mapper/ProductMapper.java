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
    List<ProductDTO> selectProductsByKeyword(String keyword);

    // 키워드 + 카테고리 검색
    List<ProductDTO> selectProductsByCategory(@Param("keyword") String keyword,  @Param("cate_cd") int cate_cd);

    // SearchDTO 기반 검색
    List<ProductDTO> selectProducts(SearchDTO searchDTO);

    // 총 상품 개수 조회
    int countProducts(SearchDTO searchDTO);

    ProductDTO selectProductByNo(int prodNo);

    ProductNoticeDTO selectProductNoticeByNo(int prodNo);

    List<ProductReviewDTO> selectProductReviews(int prodNo);

}
