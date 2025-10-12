package kr.co.kmarket.service;

import kr.co.kmarket.dto.ProductDTO;
import kr.co.kmarket.dto.ProductNoticeDTO;
import kr.co.kmarket.dto.ProductReviewDTO;
import kr.co.kmarket.dto.SearchDTO;

import java.util.List;

public interface ProductService {

    // 키워드 검색
    List<ProductDTO> selectProductsByKeyword(String keyword);

    // 키워드 + 카테고리 검색
    List<ProductDTO> selectProductsByCategory(String keyword, int cate_cd);

    // 기존 검색
    List<ProductDTO> searchProducts(SearchDTO searchDTO);

    // 정렬 기능 추가된 오버로드 메서드
    List<ProductDTO> searchProducts(SearchDTO search, String sort);

    ProductDTO selectProductByNo(int prodNo);

    ProductNoticeDTO selectProductNoticeByNo(int prodNo);

    List<ProductReviewDTO> selectProductReviews(int prodNo);
}
