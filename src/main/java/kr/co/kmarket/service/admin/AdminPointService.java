package kr.co.kmarket.service.admin;

import kr.co.kmarket.dto.PageRequestDTO;
import kr.co.kmarket.dto.PageResponseDTO;
import kr.co.kmarket.dto.PointDTO;
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

    public void remove(List<String> idList) {
        for(String point_id : idList) {
            PointDTO dto = adminPointMapper.findById(point_id);

            if(dto != null) {
                dto.setBalance(dto.getBalance() - dto.getPoint_amount());
                log.info("삭제 후 값 변경중 = {}", dto);
                int cust_number = dto.getCust_number();
                int balance = dto.getBalance();
                adminPointMapper.updatePoint(cust_number, balance);
                adminPointMapper.delete(point_id);
            }
        }
    }
}
