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
}
