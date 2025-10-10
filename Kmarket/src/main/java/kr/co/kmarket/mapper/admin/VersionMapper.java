package kr.co.kmarket.mapper.admin;

import kr.co.kmarket.dto.PageRequestDTO;
import kr.co.kmarket.dto.VersionDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VersionMapper {

    public List<VersionDTO> findAll(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO); //페이지네이션 테스트
    public int selectCountTotal(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO); //페이지네이션 테스트

    public VersionDTO findById(String versionId);
    public void insert(VersionDTO versionDTO);
    public void delete(@Param("list") List<Long> idList);
}
