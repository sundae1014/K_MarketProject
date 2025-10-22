package kr.co.kmarket.mapper.admin;

import kr.co.kmarket.dto.AdminOrderDetailDTO;
import kr.co.kmarket.dto.OrderDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminOrderMapper {

    public List<OrderDTO> selectAllOrdersListPage(@Param("start") int start,
                                                  @Param("limit") int limit,
                                                  @Param("searchType") String searchType,
                                                  @Param("keyword") String keyword);

    public int selectAllOrderCount(@Param("searchType") String searchType,
                                   @Param("keyword") String keyword);

    public OrderDTO selectOrderDetailCombined(@Param("order_number") String orderNumber);


    public int updateDeliveryInfo(@Param("order_number") String order_number,
                                  @Param("deliveryCompany") String deliveryCompany,
                                  @Param("trackingNumber") String trackingNumber,
                                  @Param("stat") int stat); // ⬅️ stat 파라미터 추가

    public OrderDTO selectOrderInfoByOrderNumber(String order_number);

    public List<OrderDTO> selectDeliveryOrdersListPage(@Param("start") int start,
                                                       @Param("limit") int limit);

    public int selectDeliveryOrderCount();

    public OrderDTO selectDeliveryOrderDetail(
            @Param("orderNumber") String orderNumber,
            @Param("trackingNumber") String trackingNumber
    );

    public int updateOrderStat(@Param("order_number") String order_number,
                               @Param("stat") int stat);

}
