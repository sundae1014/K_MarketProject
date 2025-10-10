package kr.co.kmarket.service.admin;


import kr.co.kmarket.dto.HireDTO;
import kr.co.kmarket.mapper.admin.RecruitMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RecruitService {
    private final RecruitMapper recruitMapper;
    public List<HireDTO> getAllHires(){
        return recruitMapper.selectAllHireList();
    }
    public void deleteAllByHireNo(List<Integer> hire_no){
        recruitMapper.deleteAllByHireNo(hire_no);
    }

    public void insertHire(HireDTO hireDTO){
        recruitMapper.insertHire(hireDTO);

    }

    public void updateExpiredHires(){
        recruitMapper.updateExpiredHires();
    }

    public List<HireDTO> selectSearch(String searchType, String keyword){
        return recruitMapper.selectSearch(searchType, keyword);
    };

}
