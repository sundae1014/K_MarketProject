package kr.co.kmarket.service;

import kr.co.kmarket.dto.PageRequestDTO;
import kr.co.kmarket.dto.PageResponseDTO;
import kr.co.kmarket.dto.QnaDTO;
import kr.co.kmarket.mapper.QnaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class QnaService {
    private final QnaMapper qnaMapper;

    public QnaDTO getQna(int id) {return qnaMapper.findById(id);}

    public PageResponseDTO selectAll(PageRequestDTO pageRequestDTO) {
        List<QnaDTO> dtoList = qnaMapper.findAll(pageRequestDTO);

        int total = qnaMapper.selectCount(pageRequestDTO);

        return PageResponseDTO.<QnaDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }

    public PageResponseDTO selectTypeAll(String type1, PageRequestDTO pageRequestDTO) {
        List<QnaDTO> dtoList = qnaMapper.findTypeAll(type1, pageRequestDTO);
        log.info("dtoList={}", dtoList);
        int total = qnaMapper.selectCountTotal(type1, pageRequestDTO);
        log.info("total={} ", total);
        return PageResponseDTO.<QnaDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }
    public int selectCountTotal(String type1, PageRequestDTO pageRequestDTO) {
        return qnaMapper.selectCountTotal(type1, pageRequestDTO);
    }

    public PageResponseDTO selectTypeAll2(String type1, String type2, PageRequestDTO pageRequestDTO) {
        List<QnaDTO> dtoList = qnaMapper.findTypeAll2(type1, type2, pageRequestDTO);
        log.info("dtoList={}", dtoList);
        int total = qnaMapper.selectCountTotal2(type1, type2, pageRequestDTO);
        log.info("total={} ", total);
        return PageResponseDTO.<QnaDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }
    public int selectCountTotal2(String type1, String type2, PageRequestDTO pageRequestDTO) {
        return qnaMapper.selectCountTotal2(type1, type2, pageRequestDTO);
    }

    public void insertQna(QnaDTO qnaDTO) {qnaMapper.insert(qnaDTO);}
    public void insertAnswer(QnaDTO qnaDTO) {qnaMapper.update(qnaDTO);}
    public void remove(List<Long> idList) {qnaMapper.delete(idList);}
    public void remove2(long id) {qnaMapper.deleteById(id);}
}
