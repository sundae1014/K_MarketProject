package kr.co.kmarket.service;

import kr.co.kmarket.dto.*;
import kr.co.kmarket.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper mapper;

    @Override
    public List<ProductDTO> selectProductsByKeyword(String keyword) {
        return mapper.selectProductsByKeyword(keyword);
    }

    @Override
    public List<ProductDTO> selectProductsByCategory(String keyword, int cate_cd) {
        return mapper.selectProductsByCategory(keyword, cate_cd);
    }

    @Override
    public List<ProductDTO> selectProducts(SearchDTO searchDTO, String sort) {
        searchDTO.setSort(sort);
        List<ProductDTO> list = mapper.selectProducts(searchDTO);
        for (ProductDTO p : list) {
            System.out.println("이미지 확인 >>> " + p.getProd_number() + " / " + p.getImg_1());
        }
        return list;
    }

    @Override
    public int countProducts(SearchDTO searchDTO) {
        return mapper.countProducts(searchDTO);
    }

    @Override
    public ProductDTO selectProductByNo(int prod_number) {
        return mapper.selectProductByNo(prod_number);
    }

    @Override
    public ProductNoticeDTO selectProductNoticeByNo(int prod_number) {
        return mapper.selectProductNoticeByNo(prod_number);
    }

    @Override
    public List<ProductReviewDTO> selectProductReviews(int prod_number) {
        List<ProductReviewDTO> list = mapper.selectProductReviews(prod_number);
        for (ProductReviewDTO r : list) {
            System.out.println("리뷰 이미지 경로 확인 >>> " + r.getR_img1());
        }
        return list;
    }

    @Override
    public List<ProductOptionDTO> selectProductOptions(int prod_number) {
        return mapper.selectProductOptions(prod_number);
    }

    @Override
    public double selectAvgRating(int prod_number) {
        return mapper.selectAvgRating(prod_number);
    }

    @Override
    public int countProductReviews(int prod_number) {
        return mapper.countProductReviews(prod_number);
    }

    @Override
    public List<ProductReviewDTO> selectPagedReviews(int prod_number, int offset, int pageSize) {
        return mapper.selectPagedReviews(prod_number, offset, pageSize);
    }

    // 메인 - 테마별 상품 조회
    @Override public List<ProductDTO> selectHitProducts(){ return mapper.selectHitProducts(); }
    @Override public List<ProductDTO> selectRecommendProducts(){ return mapper.selectRecommendProducts(); }
    @Override public List<ProductDTO> selectNewProducts(){ return mapper.selectNewProducts(); }
    @Override public List<ProductDTO> selectPopularProducts(){ return mapper.selectPopularProducts(); }
    @Override public List<ProductDTO> selectDiscountProducts(){ return mapper.selectDiscountProducts(); }
    @Override public List<ProductDTO> selectBestProducts(){ return mapper.selectBestProducts(); }
}
