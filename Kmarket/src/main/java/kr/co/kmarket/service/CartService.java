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

    public List<CartDTO> getCartList(int custNumber) {
        return cartMapper.selectCartList(custNumber);
    }

    public void insertCart(CartDTO cartDTO) {
        cartMapper.insertCart(cartDTO);
    }

    public void deleteCart(int cartNumber) {
        cartMapper.deleteCart(cartNumber);
    }

    public void updateQuantity(CartDTO cartDTO) {
        cartMapper.updateQuantity(cartDTO);
    }
}
