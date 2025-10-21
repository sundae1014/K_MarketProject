package kr.co.kmarket.service;

import kr.co.kmarket.dto.*;
import kr.co.kmarket.mapper.MyCouponMapper;
import kr.co.kmarket.mapper.OrderMapper;
import kr.co.kmarket.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class  OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final ProductMapper mapper;
    private final MyCouponMapper myCouponMapper;

    @Override
    public String insertOrder(OrderDTO orderDTO) {

        orderMapper.insertOrder(orderDTO);
        return orderDTO.getOrder_number();
    }

    @Override
    public void insertOrderDetail(OrderDTO orderDTO) {
        orderMapper.insertOrderDetail(orderDTO);
    }

    @Override
    public OrderDTO selectOrderByNumber(String order_number) {
        return orderMapper.selectOrderByNumber(order_number);
    }

    @Override
    public List<OrderDTO> selectOrderDetails(String order_number) {
        return orderMapper.selectOrderDetails(order_number);
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
    public void useCoupon(int couponNo, int custNumber) {
        myCouponMapper.updateCouponStatus(couponNo, custNumber);
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

    @Override
    public OrderDTO selectOrderComplete(String orderNumber) {
        return orderMapper.selectOrderComplete(orderNumber);
    }

}