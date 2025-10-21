package kr.co.kmarket.mapper.admin;

import kr.co.kmarket.dto.ChartData;
import kr.co.kmarket.dto.NoticeDTO;
import kr.co.kmarket.dto.QnaDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TotalMapper {

    public List<ChartData> selectSalesByCategory();

    public List<ChartData> selectSalesByStatus(int stat);

    public int selectCountRegToday();

    public int selectCountRegYesterday();

    public int selectPriceToday();

    public int selectPriceYesterday();

    public int selectOrderToday();

    public int selectOrderYesterday();

    public int selectPostToday();

    public int selectPostYesterday();

    public int selectStatus(int stat);

    public List<QnaDTO> selectQnq();

    public List<NoticeDTO> selectNotice();
}
