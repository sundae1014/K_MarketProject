package kr.co.kmarket.service.admin;

import kr.co.kmarket.dto.*;
import kr.co.kmarket.mapper.admin.AdminProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminProductService {
    @Value("${file.upload.path}")
    private String fileUploadPath;
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
            ProductMangementDTO productMangementDTO,
            MultipartFile file1,
            MultipartFile file2,
            MultipartFile file3,
            MultipartFile detailFile) {
        int maxProdNum = adminProductMapper.findMaxProdNumber();
        productDTO.setProd_number(maxProdNum + 1);
        productDTO.setPro_score(BigDecimal.valueOf(5));
        productDTO.setHit(0);
        log.info("서비스 삽입 = {}",  productDTO);

        saveFile(productDTO, file1, file2, file3, detailFile);

        adminProductMapper.insertProduct(productDTO);

//        adminProductMapper.insertProductNotice(productNoticeDTO);
//        adminProductMapper.insertProductManagement(productMangementDTO);
    }
    private void saveFile(ProductDTO productDTO,
                          MultipartFile img1,
                          MultipartFile img2,
                          MultipartFile img3,
                          MultipartFile detailImg) {
        String path = fileUploadPath + "/product/";
        File uploadDir = new File(path);

        if (!uploadDir.exists()) {
            if (!uploadDir.mkdirs()) {
                log.error("파일 업로드 디렉토리 생성 실패: {}", path);
                throw new RuntimeException("파일 업로드 디렉토리 생성에 실패했습니다.");
            }
        }

        // img_1 저장
        if (img1 != null && !img1.isEmpty()) {
            String savedName = saveFileToDirectory(img1, path);
            productDTO.setImg_1("/images/product/" + savedName);
        }

        // img_2 저장
        if (img2 != null && !img2.isEmpty()) {
            String savedName = saveFileToDirectory(img2, path);
            productDTO.setImg_2("/images/product/" + savedName);
        }

        // img_3 저장
        if (img3 != null && !img3.isEmpty()) {
            String savedName = saveFileToDirectory(img3, path);
            productDTO.setImg_3("/images/product/" + savedName);
        }

        // detail_img 저장
        if (detailImg != null && !detailImg.isEmpty()) {
            String savedName = saveFileToDirectory(detailImg, path);
            productDTO.setDetail_img("/images/product/" + savedName);
        }
    }
    // 파일 저장 로직을 별도 메서드로 분리 (중복 제거)
    private String saveFileToDirectory(MultipartFile file, String path) {
        String oriName = file.getOriginalFilename();

        try {
            File saveFile = new File(path, oriName);
            file.transferTo(saveFile);
            log.info("파일 저장 성공: {}", oriName);
            return oriName;
        } catch (IOException e) {
            log.error("파일 저장 오류: {}", oriName, e);
            throw new RuntimeException("파일 저장 중 오류가 발생했습니다.", e);
        }
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
