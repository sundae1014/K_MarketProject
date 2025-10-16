package kr.co.kmarket.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.kmarket.dto.CartDTO;
import kr.co.kmarket.dto.MemberDTO;
import kr.co.kmarket.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class CartController {

    private final CartService cartService;

    // 장바구니 페이지
    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
        MemberDTO member = (MemberDTO) session.getAttribute("member");
        if (member == null) {
            return "redirect:/member/login";
        }

        List<CartDTO> cartList = cartService.getCartList(member.getCust_number());
        model.addAttribute("cartList", cartList);
        return "product/prodCart";
    }

    // 장바구니 추가
    @PostMapping("/cart/add")
    @ResponseBody
    public String addCart(@RequestBody CartDTO dto, HttpSession session) {
        MemberDTO member = (MemberDTO) session.getAttribute("member");
        if (member == null) return "loginRequired";
        dto.setCust_number(member.getCust_number());
        cartService.addCart(dto);
        return "success";
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
    public void updateQuantity(@RequestBody CartDTO dto) {
        cartService.updateQuantity(dto);
    }
}