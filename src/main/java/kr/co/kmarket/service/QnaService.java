package kr.co.kmarket.service;

import kr.co.kmarket.dto.PageRequestDTO;
import kr.co.kmarket.dto.PageResponseDTO;
import kr.co.kmarket.dto.QnaDTO;
import kr.co.kmarket.mapper.QnaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class QnaService {
    private final QnaMapper qnaMapper;

    public QnaDTO getQna(int id) {return qnaMapper.findById(id);}

    public PageResponseDTO selectTypeAll(String type1, PageRequestDTO pageRequestDTO) {
        List<QnaDTO> dtoList = qnaMapper.findTypeAll(type1, pageRequestDTO);

        int total = qnaMapper.selectCountTotal(pageRequestDTO);

        return PageResponseDTO.<QnaDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }

    public PageResponseDTO selectAll(PageRequestDTO pageRequestDTO) {
        List<QnaDTO> dtoList = qnaMapper.findAll(pageRequestDTO);

        int total = qnaMapper.selectCountTotal(pageRequestDTO);

        return PageResponseDTO.<QnaDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }
    public int selectCountTotal(PageRequestDTO pageRequestDTO) {
        return qnaMapper.selectCountTotal(pageRequestDTO);
    }

    public void insertQna(QnaDTO qnaDTO) {qnaMapper.insert(qnaDTO);}
}
