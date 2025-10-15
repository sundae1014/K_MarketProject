package kr.co.kmarket.mapper;

import kr.co.kmarket.dto.FaqDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FaqMapper {
    public void insert(FaqDTO faqDTO);
}
