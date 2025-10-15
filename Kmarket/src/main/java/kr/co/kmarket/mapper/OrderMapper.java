package kr.co.kmarket.mapper;

import kr.co.kmarket.dto.OrderDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {

    void insertOrder(OrderDTO orderDTO);

}
