package kr.co.kmarket.mapper.admin;

import kr.co.kmarket.dto.VersionDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VersionMapper {

    public List<VersionDTO> findAll();
    public void insert(VersionDTO versionDTO);
    public void delete(@Param("list") List<Long> idList);
}
