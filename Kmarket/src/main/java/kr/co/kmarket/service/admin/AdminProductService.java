package kr.co.kmarket.service.admin;

import kr.co.kmarket.dto.*;
import kr.co.kmarket.mapper.admin.AdminProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminProductService {
    private final AdminProductMapper adminProductMapper;

    public PageResponseDTO selectAll(PageRequestDTO pageRequestDTO) {
        List<ProductDTO> dtoList = adminProductMapper.findAll(pageRequestDTO);
        int total = adminProductMapper.selectCount(pageRequestDTO);

        return PageResponseDTO.<ProductDTO>builder().pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }
    public List<CategoryDTO> selectCategory(String upCateCd) {return adminProductMapper.findCategory(upCateCd);}

    public void insert(
            ProductDTO productDTO,
            ProductNoticeDTO productNoticeDTO,
            ProductMangementDTO productMangementDTO) {
        adminProductMapper.insertProduct(productDTO);
        adminProductMapper.insertProductNotice(productNoticeDTO);
        adminProductMapper.insertProductManagement(productMangementDTO);
    }
}
