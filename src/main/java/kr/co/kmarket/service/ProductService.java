package kr.co.kmarket.service;

import kr.co.kmarket.dto.*;

import java.util.List;

public interface ProductService {

    // 키워드 검색
    List<ProductDTO> selectProductsByKeyword(String keyword);

    // 키워드 + 카테고리 검색
    List<ProductDTO> selectProductsByCategory(String keyword, int cate_cd);

    // 기존 검색
    List<ProductDTO> selectProducts(SearchDTO searchDTO, String sort);

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
