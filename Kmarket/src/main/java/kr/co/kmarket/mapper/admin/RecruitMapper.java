package kr.co.kmarket.mapper.admin;


import kr.co.kmarket.dto.HireDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RecruitMapper {
    public List<HireDTO> selectAllHireList();
}
