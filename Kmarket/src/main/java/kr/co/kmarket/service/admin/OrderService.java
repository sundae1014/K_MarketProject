package kr.co.kmarket.service.admin;

import kr.co.kmarket.dto.AdminOrderDetailDTO;
import kr.co.kmarket.dto.OrderDTO;
import kr.co.kmarket.mapper.admin.AdminOrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {

    private final AdminOrderMapper orderMapper;

    public List<OrderDTO> selectAllOrdersListPage(int start, int limit, String searchType, String keyword) {
        return orderMapper.selectAllOrdersListPage(start, limit, searchType, keyword);
    }

    public int selectAllOrderCount(String searchType, String keyword){
        return orderMapper.selectAllOrderCount(searchType, keyword);
    }

    public OrderDTO selectOrderDetail(String orderNumber) {
        return orderMapper.selectOrderDetailCombined(orderNumber);
    }

    public int updateDelivery(String order_number, String deliveryCompany, String trackingNumber) {
        return orderMapper.updateDeliveryInfo(order_number, deliveryCompany, trackingNumber);
    }

    public OrderDTO selectOrderInfoByOrderNumber(String order_number) {
        return orderMapper.selectOrderInfoByOrderNumber(order_number);
    }

    public List<OrderDTO> selectDeliveryOrdersListPage(int start, int limit) {
        return orderMapper.selectDeliveryOrdersListPage(start, limit);
    }

    public int selectDeliveryOrderCount(){
        return orderMapper.selectDeliveryOrderCount();
    }


    public OrderDTO selectDeliveryOrderDetail(String orderNumber, Integer trackingNumber) {
        // Controller에서 전달받은 orderNumber (String)와 trackingNumber (Integer)를 Mapper로 전달
        return orderMapper.selectDeliveryOrderDetail(orderNumber, trackingNumber);
    }
}

