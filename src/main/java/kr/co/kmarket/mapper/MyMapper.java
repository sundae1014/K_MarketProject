package kr.co.kmarket.mapper;

import kr.co.kmarket.dto.OrderDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MyMapper {

    public List<OrderDTO> selectRecentOrders(@Param("custNumber") int custNumber);

    public int countNotConfirmedOrders(@Param("custNumber") int custNumber);

    public OrderDTO selectOrderDetailByCustomer(@Param("custNumber") int custNumber,
                                                @Param("orderNumber") int orderNumber);

}
