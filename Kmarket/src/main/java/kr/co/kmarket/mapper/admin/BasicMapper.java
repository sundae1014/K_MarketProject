package kr.co.kmarket.mapper.admin;

import kr.co.kmarket.dto.BasicDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BasicMapper {

    public String selectRecentVersion();

    public void updateBasic(BasicDTO basicDTO);

    public BasicDTO selectBasic();
}
