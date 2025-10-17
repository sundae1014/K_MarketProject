package kr.co.kmarket.service;

import kr.co.kmarket.dto.*;
import kr.co.kmarket.mapper.OrderMapper;
import kr.co.kmarket.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final ProductMapper mapper;

    @Override
    public void insertOrder(OrderDTO orderDTO) {
        orderMapper.insertOrder(orderDTO);
    }

    @Override
    public ProductDTO selectProductById(int prod_number) {
        return mapper.selectProductById(prod_number);
    }

    @Override
    public ProductDTO selectProductDetail(int prod_number) {
        return mapper.selectProductDetail(prod_number);
    }

    @Override
    public List<ProductOptionDTO> selectOptionsByProduct(int prod_number) {
        return mapper.selectOptionsByProduct(prod_number);
    }

    @Override
    public List<CartDTO> selectCartList(List<Integer> cart_numbers) {
        return orderMapper.selectCartList(cart_numbers);
    }

    @Override
    public List<CouponDTO> selectAvailableCoupons(int cust_number) {
        return orderMapper.selectAvailableCoupons(cust_number);
    }

    @Override
    public int selectUserPoint(int cust_number) {
        return orderMapper.selectUserPoint(cust_number);
    }

}