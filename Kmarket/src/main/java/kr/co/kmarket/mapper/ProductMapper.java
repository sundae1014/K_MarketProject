package kr.co.kmarket.mapper;

import kr.co.kmarket.dto.*;
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

    // 상품 리뷰 조회
    List<ProductReviewDTO> selectProductReviews(int prodNo);

    // 상품 옵션 조회
    List<ProductOptionDTO> selectProductOptions(int prod_number);

    // 평균 리뷰 점수
    double selectAvgRating(int prod_number);
    int countProductReviews(int prod_number);
    List<ProductReviewDTO> selectPagedReviews(int prod_number, int offset, int pageSize);

    // 메인 - 테마별 상품 조회
    List<ProductDTO> selectHitProducts();
    List<ProductDTO> selectRecommendProducts();
    List<ProductDTO> selectNewProducts();
    List<ProductDTO> selectPopularProducts();
    List<ProductDTO> selectDiscountProducts();
    List<ProductDTO> selectBestProducts();

}
