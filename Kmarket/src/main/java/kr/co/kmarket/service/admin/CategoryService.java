package kr.co.kmarket.service.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import kr.co.kmarket.dto.CategoryDTO;
import kr.co.kmarket.mapper.admin.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryMapper categoryMapper;
    private final ObjectMapper objectMapper;

    public List<CategoryDTO> getAllCategories() {
        return categoryMapper.selectAllCate();
    }

    @Transactional
    public void updateCategories(CategoryDTO categoryDTO, String deletedCateCd, String orderDataJson) throws JsonProcessingException {


        if (deletedCateCd != null && !deletedCateCd.isEmpty()) {
            String[] delArr = deletedCateCd.split(",");
            for (String cd : delArr) {
                Integer count = categoryMapper.isTopCategory(cd);
                boolean isTop = (count != null && count > 0);

                if (isTop) categoryMapper.deleteSubCategories(cd);
                categoryMapper.deleteCategory(cd);
            }
        }


        if (categoryDTO != null && categoryDTO.getCate_name() != null && !categoryDTO.getCate_name().isEmpty()) {
            if (categoryDTO.getUp_cate_cd() == null || categoryDTO.getUp_cate_cd().isEmpty()) {
                String maxCateCd = categoryMapper.selectMaxCateCd("C");
                String newCateCd = (maxCateCd == null) ? "C01" : String.format("C%02d", Integer.parseInt(maxCateCd.substring(1)) + 1);
                categoryDTO.setCate_cd(newCateCd);
                categoryDTO.setUp_cate_cd(null);
            } else {
                String upCateCd = categoryDTO.getUp_cate_cd();
                String maxSubCateCd = categoryMapper.selectMaxSubCateCd(upCateCd);
                String newCateCd = (maxSubCateCd == null) ? upCateCd + "01"
                        : upCateCd + String.format("%02d", Integer.parseInt(maxSubCateCd.substring(3, 5)) + 1);
                categoryDTO.setCate_cd(newCateCd);
            }
            categoryMapper.insertCate(categoryDTO);
        }


        if (orderDataJson != null && !orderDataJson.isEmpty()) {
            List<Map<String, Object>> orderList = objectMapper.readValue(orderDataJson, List.class);
            if(!orderList.isEmpty()) categoryMapper.updateCategoryOrdersFast(orderList);
        }
    }

}
