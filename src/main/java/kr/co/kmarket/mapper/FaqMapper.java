package kr.co.kmarket.mapper;

import kr.co.kmarket.dto.FaqDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FaqMapper {
    public FaqDTO findById(@Param("id") int id);
    public List<FaqDTO> findAll();
    public int countAll(@Param("type1") String type1, @Param("type2") String type2);

    public List<FaqDTO> findTypeAll(@Param("type1") String type1, @Param("type2") String type2);

    public void insert(FaqDTO faqDTO);

    public void update(FaqDTO faqDTO);
    public void update2(FaqDTO faqDTO);

    public void delete(@Param("id")  long id);
    public void deleteAll(@Param("list") List<Long> idList);
}
