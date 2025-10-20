package kr.co.kmarket.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.kmarket.dto.*;
import kr.co.kmarket.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class UserOrderController {

    private final OrderService orderService;

    // 주문서 작성 페이지
    @GetMapping("/form")
    public String orderForm(HttpSession session,
                            @RequestParam(required = false) Integer prod_number,
                            @RequestParam(required = false, defaultValue = "1") Integer quantity,
                            @RequestParam(required = false) String cartNumbers,
                            Model model) {

        MemberDTO memberDTO = (MemberDTO) session.getAttribute("member");
        if (memberDTO == null) return "redirect:/member/login";

        // 구매자 정보
        model.addAttribute("buyer", memberDTO);

        // Case 1: 장바구니에서 넘어온 경우
        if (cartNumbers != null && !cartNumbers.isEmpty()) {
            String[] numbers = cartNumbers.split(",");
            List<CartDTO> orderItems = orderService.selectCartItemsByNumbers(numbers);
            model.addAttribute("orderItems", orderItems);

            // 포인트, 쿠폰
            model.addAttribute("userPoint", orderService.selectUserPoint(memberDTO.getCust_number()));
            model.addAttribute("coupons", orderService.selectAvailableCoupons(memberDTO.getCust_number()));

            return "product/prodOrder";
        }

        // Case 2: 상품 상세 페이지 → 바로구매
        if (prod_number != null) {
            ProductDTO productDTO = orderService.selectProductDetail(prod_number);
            model.addAttribute("product", productDTO);
            model.addAttribute("quantity", quantity);
            model.addAttribute("options", orderService.selectOptionsByProduct(prod_number));
            model.addAttribute("coupons", orderService.selectAvailableCoupons(memberDTO.getCust_number()));
            model.addAttribute("userPoint", orderService.selectUserPoint(memberDTO.getCust_number()));
            return "product/prodOrder";
        }

        return "redirect:/product/cart";
    }

    // 주문 처리 (결제 완료 버튼 누른 후)
    @PostMapping("/complete")
    public String completeOrder(@ModelAttribute OrderDTO orderDTO) {

        // 1️⃣ 주문 생성
        orderDTO.setPayment(1);
        String order_number = orderService.insertOrder(orderDTO);

        // 2️⃣ 포인트 차감
        if (orderDTO.getUsePoint() > 0) {
            orderService.usePoint(orderDTO.getCust_number(), order_number, orderDTO.getUsePoint());
        }

        // 3️⃣ 포인트 적립 (1%)
        int earn = (int)(orderDTO.getPrice() * 0.01);
        orderService.earnPoint(orderDTO.getCust_number(), order_number, earn);

        // 완료 페이지 이동
        return "product/prodComplete";
    }
}