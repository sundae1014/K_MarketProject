package kr.co.kmarket.mapper;

import kr.co.kmarket.dto.CartDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CartMapper {

    List<CartDTO> selectCartList(int cust_number);
    void insertCart(CartDTO cartDTO);
    void deleteCart(int cart_number);
    void deleteAllCart(int cust_number);
    void updateQuantity(CartDTO cartDTO);
}
