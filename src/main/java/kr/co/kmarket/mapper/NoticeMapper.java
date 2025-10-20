package kr.co.kmarket.mapper;

import kr.co.kmarket.dto.NoticeDTO;
import kr.co.kmarket.dto.PageRequestDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NoticeMapper {
    public NoticeDTO findById(@Param("id") int id);
    public List<NoticeDTO> findNoticeAll();
    public List<NoticeDTO> findAll(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO);
    public int selectCount(@Param("pageRequestDTO")  PageRequestDTO pageRequestDTO);

    public List<NoticeDTO> findTypeAll(@Param("nType") String nType, @Param("pageRequestDTO") PageRequestDTO pageRequestDTO);
    public int selectCountTotal(@Param("nType") String nType, @Param("pageRequestDTO")  PageRequestDTO pageRequestDTO);

    public void insert(NoticeDTO noticeDTO);

    public void update(NoticeDTO noticeDTO);
    public void update2(NoticeDTO noticeDTO);

    public void delete(@Param("list") List<Long> idList);
    public void deleteById(@Param("id")  long id);
}
