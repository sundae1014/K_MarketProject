package kr.co.kmarket.service.admin;

import kr.co.kmarket.dto.MemberDTO;
import kr.co.kmarket.dto.PageRequestDTO;
import kr.co.kmarket.dto.PageResponseDTO;
import kr.co.kmarket.mapper.admin.ShopMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ShopService {
    private final ShopMapper shopMapper;

    public PageResponseDTO selectAll(PageRequestDTO pageRequestDTO) { //페이지네이션 및 전체 불러오기
        List<MemberDTO> dtoList = shopMapper.findAll(pageRequestDTO);
        int total = shopMapper.selectCountTotal(pageRequestDTO);

        return PageResponseDTO.<MemberDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }
    public int selectCountTotal(PageRequestDTO pageRequestDTO) { //페이지네이션
        return shopMapper.selectCountTotal(pageRequestDTO);
    }

    public PageResponseDTO searchAll(PageRequestDTO pageRequestDTO) { //검색을 통한 불러오기
        List<MemberDTO> dtoList = shopMapper.searchAll(pageRequestDTO); //검색 테스트
        int total = shopMapper.searchCount(pageRequestDTO);//검색 테스트

        return PageResponseDTO.<MemberDTO>builder()//검색 테스트
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }
    public int searchCount(PageRequestDTO pageRequestDTO) {return shopMapper.searchCount(pageRequestDTO);}

    public void updateOperation(Long custNumber, String operation) {
        shopMapper.update(custNumber, operation);
    }

    public void remove(List<Long> idList) {shopMapper.delete(idList);}
}
