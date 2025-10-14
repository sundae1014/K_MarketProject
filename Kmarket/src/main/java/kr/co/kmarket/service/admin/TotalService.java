package kr.co.kmarket.service.admin;

import kr.co.kmarket.dto.ChartData;
import kr.co.kmarket.dto.QnaDTO;
import kr.co.kmarket.mapper.admin.TotalMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class TotalService {
    private final TotalMapper totalMapper;

    public List<ChartData> getSalesByCategory(){
        return totalMapper.selectSalesByCategory();
    };

    public List<ChartData> getSalesByStatus(int stat){
        return totalMapper.selectSalesByStatus(stat);
    }

    public int getMemberRegToday(){
        return totalMapper.selectCountRegToday();
    };
    public int getMemberRegYesterday(){
        return totalMapper.selectCountRegYesterday();
    };

    public int getPriceToday(){
        return totalMapper.selectPriceToday();
    };
    public int getPriceYesterday(){
        return totalMapper.selectPriceYesterday();
    };

    public int getOrderToday(){
        return totalMapper.selectOrderToday();
    };

    public int getOrderYesterday(){
        return totalMapper.selectOrderYesterday();
    };

    public int getPostToday(){
        return totalMapper.selectPostToday();
    };

    public int getPostYesterday(){
        return totalMapper.selectPostYesterday();
    };

    public int getStatus(int stat){
        return totalMapper.selectStatus(stat);
    }

    public List<QnaDTO> getQnaInfo(){
        return totalMapper.selectQnq();
    }

}
