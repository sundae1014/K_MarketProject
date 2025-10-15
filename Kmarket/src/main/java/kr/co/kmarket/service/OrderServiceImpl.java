package kr.co.kmarket.service;

import kr.co.kmarket.dto.OrderDTO;
import kr.co.kmarket.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public void insertOrder(OrderDTO orderDTO) {
        orderMapper.insertOrder(orderDTO);
    }
}