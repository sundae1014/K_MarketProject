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
    List<ProductDTO> selectProducts(SearchDTO searchDTO, String sort);

    // 총 상품 개수 조회
    int countProducts(SearchDTO searchDTO);

    ProductDTO selectProductByNo(int prod_number);

    ProductNoticeDTO selectProductNoticeByNo(int prod_number);

    List<ProductReviewDTO> selectProductReviews(int prod_number);
}
