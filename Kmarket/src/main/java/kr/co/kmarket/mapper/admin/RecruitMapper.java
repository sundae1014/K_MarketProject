package kr.co.kmarket.mapper.admin;


import kr.co.kmarket.dto.HireDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RecruitMapper {
    public List<HireDTO> selectAllHireList();

    public void deleteAllByHireNo(List<Integer> hire_no);

    public void insertHire(HireDTO hireDTO);

    public void updateExpiredHires();

    public List<HireDTO> selectSearch(String searchType, String keyword);


}
