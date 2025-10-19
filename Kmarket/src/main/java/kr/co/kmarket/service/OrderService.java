package kr.co.kmarket.service;

import kr.co.kmarket.dto.*;

import java.util.List;

public interface OrderService {

    // 주문 테이블
    String insertOrder(OrderDTO orderDTO);

    // 상품 조회
    ProductDTO selectProductById(int prod_number);
    ProductDTO selectProductDetail(int prod_number);
    List<ProductOptionDTO> selectOptionsByProduct(int prod_number);
    
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