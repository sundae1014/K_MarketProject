package kr.co.kmarket.mapper.admin;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface TotalMapper {

    public int selectCountRegToday();

    public int selectCountRegYesterday();

    public int selectPriceToday();

    public int selectPriceYesterday();

    public int selectOrderToday();

    public int selectOrderYesterday();

    public int selectPostToday();

    public int selectPostYesterday();

    public int selectStatus(int stat);
}
