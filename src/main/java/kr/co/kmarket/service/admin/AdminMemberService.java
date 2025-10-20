package kr.co.kmarket.service.admin;

import kr.co.kmarket.dto.AdminMemberDTO;
import kr.co.kmarket.dto.MemberDTO;
import kr.co.kmarket.dto.PageRequestDTO;
import kr.co.kmarket.dto.PageResponseDTO;
import kr.co.kmarket.mapper.admin.AdminMemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminMemberService {
    private final AdminMemberMapper adminMemberMapper;

    public AdminMemberDTO getView(String custId) {return adminMemberMapper.findById(custId);}

    public PageResponseDTO selectAll(PageRequestDTO pageRequestDTO) {
        List<Map<String, Object>> dtoList = adminMemberMapper.findAll(pageRequestDTO);
        int total = adminMemberMapper.selectCount(pageRequestDTO);

        return PageResponseDTO.<Map<String, Object>>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }

    public PageResponseDTO searchAll(PageRequestDTO pageRequestDTO) {
        List<Map<String, Object>> dtoList = adminMemberMapper.searchAll(pageRequestDTO);
        int total = adminMemberMapper.searchCountTotal(pageRequestDTO);

        return PageResponseDTO.<Map<String, Object>>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }

    public void updateOperation(String custId, String operation) {
        log.info("custId: {}, operation: {} ", custId, operation);
        adminMemberMapper.update(custId, operation);
    }

    public void updateRating(String custId, String rating) {
        log.info("custId: {}, rating: {} ", custId, rating);
        adminMemberMapper.update2(custId, rating);
    }

    public void updateView(AdminMemberDTO adminMemberDTO) {
        log.info("테스트1: {}",  adminMemberDTO);
        adminMemberMapper.update3(adminMemberDTO);
    }
}
