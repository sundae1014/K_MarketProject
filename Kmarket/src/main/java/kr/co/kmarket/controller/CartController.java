package kr.co.kmarket.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.kmarket.dto.CartDTO;
import kr.co.kmarket.dto.MemberDTO;
import kr.co.kmarket.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class CartController {

    private final CartService cartService;

    // 장바구니 페이지
    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
        MemberDTO memberDTO = (MemberDTO) session.getAttribute("member");
        if (memberDTO == null) {
            return "redirect:/member/login";
        }

        List<CartDTO> cartList = cartService.getCartList(memberDTO.getCust_number());
        model.addAttribute("cartList", cartList);
        return "product/prodCart";
    }

    // 장바구니 추가
    @PostMapping("/cart/add")
    @ResponseBody
    public String addCart(@RequestBody CartDTO cartDTO, HttpSession session) {
        MemberDTO memberDTO = (MemberDTO) session.getAttribute("member");
        cartDTO.setCust_number(memberDTO.getCust_number());
        cartService.insertCart(cartDTO);
        return "success";
    }

    @GetMapping("/cart/list")
    @ResponseBody
    public List<CartDTO> getCartList(HttpSession session) {
        MemberDTO memberDTO = (MemberDTO) session.getAttribute("member");
        if (memberDTO == null) {
            // 로그인 안 된 경우에는 비어있는 배열 반환
            return Collections.emptyList();
        }

        return cartService.getCartList(memberDTO.getCust_number());
    }

    // 선택 상품 삭제
    @DeleteMapping("/cart/{cart_number}")
    @ResponseBody
    public String deleteCart(@PathVariable int cart_number) {
        cartService.deleteCart(cart_number);
        return "deleted";
    }

    @PatchMapping("/cart/updateQty")
    @ResponseBody
    public void updateQuantity(@RequestBody CartDTO cartDTO) {
        cartService.updateQuantity(cartDTO);
    }
}