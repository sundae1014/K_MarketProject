package kr.co.kmarket.service.admin;

import kr.co.kmarket.dto.PageRequestDTO;
import kr.co.kmarket.dto.PageResponseDTO;
import kr.co.kmarket.mapper.admin.AdminPointMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminPointService {
    private final AdminPointMapper adminPointMapper;

    public PageResponseDTO selectAll(PageRequestDTO pageRequestDTO) {
        List<Map<String, Object>> dtoList = adminPointMapper.findAll(pageRequestDTO);
        int total = adminPointMapper.selectCount(pageRequestDTO);

        return PageResponseDTO.<Map<String, Object>>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }

    public PageResponseDTO searchAll(PageRequestDTO pageRequestDTO) {
        List<Map<String, Object>> dtoList = adminPointMapper.searchAll(pageRequestDTO);
        int total = adminPointMapper.searchCountTotal(pageRequestDTO);

        return PageResponseDTO.<Map<String, Object>>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }
}
