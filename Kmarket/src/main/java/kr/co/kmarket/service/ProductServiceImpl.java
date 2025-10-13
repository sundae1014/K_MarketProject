package kr.co.kmarket.service;

import kr.co.kmarket.dto.ProductDTO;
import kr.co.kmarket.dto.ProductNoticeDTO;
import kr.co.kmarket.dto.ProductReviewDTO;
import kr.co.kmarket.dto.SearchDTO;
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
    public List<ProductDTO> searchProducts(SearchDTO searchDTO) {
        return mapper.searchProducts(searchDTO);
    }

    @Override
    public List<ProductDTO> searchProducts(SearchDTO search, String sort) {
        search.setSort(sort);
        return mapper.searchProducts(search);
    }

    @Override
    public ProductDTO selectProductByNo(int prodNo) {
        return mapper.selectProductByNo(prodNo);
    }

    @Override
    public ProductNoticeDTO selectProductNoticeByNo(int prodNo) {
        return mapper.selectProductNoticeByNo(prodNo);
    }

    @Override
    public List<ProductReviewDTO> selectProductReviews(int prodNo) {
        return mapper.selectProductReviews(prodNo);
    }
}
