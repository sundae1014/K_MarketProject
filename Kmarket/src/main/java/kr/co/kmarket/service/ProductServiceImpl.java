package kr.co.kmarket.service;

import jakarta.annotation.PostConstruct;
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

    @PostConstruct
    public void testDB() {
        String result = mapper.testConnection();
        System.out.println("🟢 DB 테스트 결과: " + result);
    }

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
        return mapper.selectProductReviews(prod_number);
    }

}
