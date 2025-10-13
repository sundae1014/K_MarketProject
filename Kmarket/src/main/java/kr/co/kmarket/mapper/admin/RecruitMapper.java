package kr.co.kmarket.mapper.admin;


import kr.co.kmarket.dto.HireDTO;
import kr.co.kmarket.dto.PageRequestDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RecruitMapper {
    public List<HireDTO> selectAllHireList();

    public void deleteAllByHireNo(List<Integer> hire_no);

    public void insertHire(HireDTO hireDTO);

    public void updateExpiredHires();

    public List<HireDTO> selectSearch(String searchType, String keyword);

    List<HireDTO> selectHireListPage(PageRequestDTO pageRequestDTO);

    public int countHires();

    List<HireDTO> selectSearchPage(@Param("searchType") String searchType,
                                   @Param("keyword") String keyword,
                                   @Param("offset") int offset,
                                   @Param("size") int size);

    public int  countSearchHires(String searchType, String keyword);

    public HireDTO selectHireNo(@Param("hire_no") int hire_no);
}
