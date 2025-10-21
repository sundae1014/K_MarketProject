package kr.co.kmarket.mapper.admin;

import kr.co.kmarket.dto.CategoryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CategoryMapper {
    public List<CategoryDTO> selectAllCate();
    String getNextCateCd(@Param("up_cate_cd") String up_cate_cd);
    public void insertCate(CategoryDTO categoryDTO);
    Integer isTopCategory(@Param("cate_cd") String cate_cd);
    void deleteSubCategories(@Param("up_cate_cd") String up_cate_cd);
    void deleteCategory(@Param("cate_cd") String cate_cd);
    public void updateCate(CategoryDTO categoryDTO);
    public String selectMaxCateCd(@Param("cate_cd") String cate_cd);
    public String selectMaxSubCateCd(@Param("up_cate_cd") String up_cate_cd);
    public void reorderTopCategories();
    public void reorderSubCategories();
    int updateCategoryOrder(@Param("cate_cd") String cate_cd, @Param("cate_order") Integer cate_order);
    void updateCategoryOrdersFast(@Param("list") List<Map<String, Object>> orderList);
}
