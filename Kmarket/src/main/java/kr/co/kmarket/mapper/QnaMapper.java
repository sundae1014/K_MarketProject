package kr.co.kmarket.mapper;

import kr.co.kmarket.dto.PageRequestDTO;
import kr.co.kmarket.dto.QnaDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QnaMapper {
    public QnaDTO findById(@Param("id") int id);

    public List<QnaDTO> findAll(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO);
    public int selectCount(@Param("pageRequestDTO")  PageRequestDTO pageRequestDTO);

    public List<QnaDTO> findTypeAll(@Param("type1") String type1, @Param("pageRequestDTO") PageRequestDTO pageRequestDTO);
    public int selectCountTotal(@Param("type1") String type1, @Param("pageRequestDTO")  PageRequestDTO pageRequestDTO);


    public List<QnaDTO> findTypeAll2(@Param("type1") String type1, @Param("type2") String type2, @Param("pageRequestDTO") PageRequestDTO pageRequestDTO);
    public int selectCountTotal2(@Param("type1") String type1, @Param("type2") String type2 , @Param("pageRequestDTO")  PageRequestDTO pageRequestDTO);


    public void insert(QnaDTO qnaDTO); //질문
    public void update(QnaDTO qnaDTO); //답변

    public void delete(@Param("list") List<Long> idList);
}
