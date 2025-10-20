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
    public String insertOrder(OrderDTO orderDTO) {
        orderMapper.insertOrder(orderDTO);

        // <selectKey>로 미리 ORDER_NUMBER를 채워두면 이게 자동 세팅됨
        return orderDTO.getOrder_number();
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
    public List<CartDTO> selectCartItemsByNumbers(String[] cart_number) {
        return orderMapper.selectCartItemsByNumbers(cart_number);
    }


    @Override
    public List<CouponDTO> selectAvailableCoupons(int cust_number) {
        return orderMapper.selectAvailableCoupons(cust_number);
    }

    @Override
    public int selectUserPoint(int cust_number) {
        return orderMapper.selectUserPoint(cust_number);
    }

    @Override
    public void insertPoint(PointDTO pointDTO) {
        orderMapper.insertPoint(pointDTO);
    }

    // 포인트 사용 (차감)
    @Override
    public void usePoint(int cust_number, String order_number, int usePoint) {
        PointDTO pointDTO = new PointDTO();
        pointDTO.setCust_number(cust_number);
        pointDTO.setOrder_number(order_number);
        pointDTO.setPoint_type(2);            // 사용
        pointDTO.setPoint_amount(-usePoint);  // 음수로 차감
        pointDTO.setDescription("상품 결제 시 포인트 사용");
        orderMapper.insertPoint(pointDTO);
    }

    // 포인트 적립
    @Override
    public void earnPoint(int cust_number, String order_number, int earnPoint) {
        PointDTO pointDTO = new PointDTO();
        pointDTO.setCust_number(cust_number);
        pointDTO.setOrder_number(order_number);
        pointDTO.setPoint_type(1); // 1: 적립
        pointDTO.setPoint_amount(earnPoint);
        pointDTO.setDescription("상품 결제 포인트 적립");
        orderMapper.insertPoint(pointDTO);
    }

}