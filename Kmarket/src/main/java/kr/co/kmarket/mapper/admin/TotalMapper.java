package kr.co.kmarket.mapper.admin;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TotalMapper {

    public int selectCountRegToday();

    public int selectCountRegYesterday();

}
