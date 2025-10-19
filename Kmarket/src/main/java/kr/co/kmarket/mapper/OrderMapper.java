package kr.co.kmarket.mapper;

import kr.co.kmarket.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {

    // 주문 테이블
    void insertOrder(OrderDTO orderDTO);

    // 장바구니
    List<CartDTO> selectCartList(List<Integer> cart_numbers);

    // 쿠폰 조회
    List<CouponDTO> selectAvailableCoupons(int cust_number);

    // 포인트 (조회, 삽입, 사용(차감), 적립)
    int selectUserPoint(int cust_number);
    void insertPoint(PointDTO pointDTO);
    void usePoint(int cust_number, String order_number, int usePoint);
    void earnPoint(int cust_number, String order_number, int earnPoint);

}