package kr.co.kmarket.mapper;

import kr.co.kmarket.dto.PolicyDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PolicyMapper {

    public List<PolicyDTO> selectAllByType(@Param("type") String type);
    public void update(PolicyDTO policyDTO);
}
