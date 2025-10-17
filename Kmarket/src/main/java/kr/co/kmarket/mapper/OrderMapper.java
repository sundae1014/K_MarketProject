package kr.co.kmarket.mapper;

import kr.co.kmarket.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {

    void insertOrder(OrderDTO orderDTO);

    // 상품 조회
    ProductDTO selectProductById(int prod_number);

    // 상품 상세 조회
    ProductDTO selectProductDetail(int prod_number);

    // 상품 옵션 조회
    List<ProductOptionDTO> selectOptionsByProduct(int prod_number);

    // 장바구니 조회
    List<CartDTO> selectCartList(List<Integer> cart_numbers);

    // 사용 가능한 쿠폰 조회
    List<CouponDTO> selectAvailableCoupons(int cust_number);

    // 유저 포인트 조회
    int selectUserPoint(int cust_number);
}