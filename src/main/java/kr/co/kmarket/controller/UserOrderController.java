package kr.co.kmarket.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.kmarket.dto.*;
import kr.co.kmarket.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class UserOrderController {

    private final OrderService orderService;

    // 주문서 작성 페이지
    @GetMapping("/form")
    public String orderForm(
            HttpSession session,
            @RequestParam(required = false) Integer prod_number,
            @RequestParam(required = false, defaultValue = "1") Integer quantity,
            Model model) {

        MemberDTO member = (MemberDTO) session.getAttribute("member");
        if (member == null) return "redirect:/member/login";

        // 구매자 정보
        model.addAttribute("buyer", member);

        // 상품 정보 (PRODUCT + OPTION + COUPON + POINT)
        ProductDTO product = orderService.selectProductDetail(prod_number);
        model.addAttribute("product", product);
        model.addAttribute("quantity", quantity);

        // 옵션 정보 (PRODUCT_OPTION)
        List<ProductOptionDTO> options = orderService.selectOptionsByProduct(prod_number);
        model.addAttribute("options", options);

        // 쿠폰 정보 (회원 전용)
        List<CouponDTO> coupons = orderService.selectAvailableCoupons(member.getCust_number());
        model.addAttribute("coupons", coupons);

        // 포인트 정보
        int userPoint = orderService.selectUserPoint(member.getCust_number());
        model.addAttribute("userPoint", userPoint);

        return "product/prodOrder";
    }

    // 주문 처리 (결제 완료 버튼 누른 후)
    @PostMapping("/complete")
    public String orderComplete(OrderDTO orderDTO, Model model) {
        orderService.insertOrder(orderDTO);
        model.addAttribute("order", orderDTO);
        return "product/prodComplete";
    }
}