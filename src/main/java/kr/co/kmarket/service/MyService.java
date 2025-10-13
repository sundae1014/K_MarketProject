package kr.co.kmarket.service;

import kr.co.kmarket.dto.OrderDTO;
import kr.co.kmarket.mapper.MyMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MyService {

    private final MyMapper myMapper;

    public List<OrderDTO> getRecentOrders(int custNumber) {
        return myMapper.selectRecentOrders(custNumber);
    }

    public int getNotConfirmedOrderCount(int custNumber) {
        return myMapper.countNotConfirmedOrders(custNumber);
    }

    public OrderDTO getOrderDetailByCustomer(int custNumber, int orderNumber) {
        return myMapper.selectOrderDetailByCustomer(custNumber, orderNumber);
    }
}
