package kr.co.kmarket.service.admin;

import kr.co.kmarket.dto.*;
import kr.co.kmarket.mapper.admin.AdminProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminProductService {
    private final AdminProductMapper adminProductMapper;

    public PageResponseDTO selectAll(PageRequestDTO pageRequestDTO) {
        List<Map<String, Object>> dtoList = adminProductMapper.findAll(pageRequestDTO);
        int total = adminProductMapper.selectCount(pageRequestDTO);

        return PageResponseDTO.<Map<String, Object>>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }

    public PageResponseDTO searchAll(PageRequestDTO pageRequestDTO) {
        List<Map<String, Object>> dtoList = adminProductMapper.searchAll(pageRequestDTO);
        int total = adminProductMapper.searchCount(pageRequestDTO);

        return PageResponseDTO.<Map<String, Object>>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }

    public List<CategoryDTO> selectCategory(String upCateCd) {return adminProductMapper.findCategory(upCateCd);}

    public void insert(
            ProductDTO productDTO,
            ProductNoticeDTO productNoticeDTO,
            ProductMangementDTO productMangementDTO) {
        productDTO.setProd_number(10050);
        productDTO.setPro_score(BigDecimal.valueOf(5));
        productDTO.setImg_1("/images/product/" + productDTO.getImg_1());
        productDTO.setImg_1("/images/product/" + productDTO.getDetail_img());
        log.info("서비스 삽입 = {}",  productDTO);
        adminProductMapper.insertProduct(productDTO);

//        adminProductMapper.insertProductNotice(productNoticeDTO);
//        adminProductMapper.insertProductManagement(productMangementDTO);
    }

    @Transactional
    public void remove(long prod_number) {
        adminProductMapper.deletePoint(prod_number);
        adminProductMapper.deleteProductManagement(prod_number);
        adminProductMapper.deleteProduct(prod_number);
    }

    @Transactional
    public void removeAll(List<Long> idList) {
        for(Long prod_number : idList) {
            adminProductMapper.deletePoint(prod_number);
            adminProductMapper.deleteProductManagement(prod_number);
            adminProductMapper.deleteProduct(prod_number);
        }
    }
}
