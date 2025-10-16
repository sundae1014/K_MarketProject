package kr.co.kmarket.mapper;

import kr.co.kmarket.dto.CartDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CartMapper {

    // 장바구니 목록 조회
    List<CartDTO> selectCartList(int cust_number);

    // 장바구니 상품 추가
    int insertCart(CartDTO dto);

    // 장바구니 상품 삭제
    int deleteCart(int cart_number);

    // 장바구니 전체 비우기
    int deleteAllCart(int cust_number);

    // 수량 변경
    int updateQuantity(CartDTO cartDTO);
}