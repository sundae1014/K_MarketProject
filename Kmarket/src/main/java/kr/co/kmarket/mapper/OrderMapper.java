package kr.co.kmarket.mapper;

import kr.co.kmarket.dto.*;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;

@Mapper
public interface OrderMapper {

    // 주문 테이블
    int insertOrder(OrderDTO orderDTO);  // ✅ 수정
    void insertOrderDetail(OrderDTO orderDTO);
    OrderDTO selectOrderByNumber(String order_number);
    List<OrderDTO> selectOrderDetails(String order_number);

    // 장바구니
    List<CartDTO> selectCartList(List<Integer> cart_numbers);
    List<CartDTO> selectCartItemsByNumbers(String[] cart_number);

    // 쿠폰 조회
    List<CouponDTO> selectAvailableCoupons(int cust_number);

    // 포인트 (조회 + 추가 기록)
    int selectUserPoint(int cust_number);
    void insertPoint(PointDTO pointDTO);
}
