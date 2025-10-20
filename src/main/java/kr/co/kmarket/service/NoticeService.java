package kr.co.kmarket.service;

import kr.co.kmarket.dto.NoticeDTO;
import kr.co.kmarket.dto.PageRequestDTO;
import kr.co.kmarket.dto.PageResponseDTO;
import kr.co.kmarket.mapper.NoticeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class NoticeService {
    private final NoticeMapper noticeMapper;

    public NoticeDTO getNotice(int id) {return noticeMapper.findById(id);}
    public List<NoticeDTO> selectNoticeAll() {return noticeMapper.findNoticeAll();}
    public PageResponseDTO selectAll(PageRequestDTO pageRequestDTO) {
        List<NoticeDTO> dtoList = noticeMapper.findAll(pageRequestDTO);
        int total = noticeMapper.selectCount(pageRequestDTO);

        return PageResponseDTO.<NoticeDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }

    public PageResponseDTO selectTypeAll(String nType, PageRequestDTO pageRequestDTO) {
        List<NoticeDTO> dtoList = noticeMapper.findTypeAll(nType, pageRequestDTO);
        log.info("dtoList={}", dtoList);

        int total = noticeMapper.selectCountTotal(nType, pageRequestDTO);
        log.info("total={} ", total);

        return PageResponseDTO.<NoticeDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }

    public void insertNotice(NoticeDTO noticeDTO) {noticeMapper.insert(noticeDTO);}

    public void updateNotice(NoticeDTO noticeDTO) {noticeMapper.update(noticeDTO);}
    public void updateViews(NoticeDTO noticeDTO) {noticeMapper.update2(noticeDTO);};

    public void remove(List<Long> idList) {noticeMapper.delete(idList);}
    public void remove2(long id) {noticeMapper.deleteById(id);}
}
