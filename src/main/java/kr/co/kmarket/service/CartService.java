package kr.co.kmarket.service;

import kr.co.kmarket.dto.CartDTO;
import kr.co.kmarket.mapper.CartMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartMapper cartMapper;

    // 장바구니 목록 조회
    public List<CartDTO> getCartList(int cust_number) {
        return cartMapper.selectCartList(cust_number);
    }

    // 장바구니 상품 추가
    public int addCart(CartDTO dto) {
        return cartMapper.insertCart(dto);
    }

    // 장바구니 상품 삭제
    public int deleteCart(int cart_number) {
        return cartMapper.deleteCart(cart_number);
    }

    // 장바구니 전체 비우기
    public int clearCart(int cust_number) {
        return cartMapper.deleteAllCart(cust_number);
    }

    // 수량 변경
    public int updateQuantity(CartDTO dto) {
        return cartMapper.updateQuantity(dto);
    }
}