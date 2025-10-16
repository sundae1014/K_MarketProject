package kr.co.kmarket.mapper.admin;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BasicMapper {

    public String selectRecentVersion();
}
