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

        MemberDTO memberDTO = (MemberDTO) session.getAttribute("member");
        if (memberDTO == null) return "redirect:/member/login";

        // 구매자 정보
        model.addAttribute("buyer", memberDTO);

        // 상품 정보 (PRODUCT + OPTION + COUPON + POINT)
        ProductDTO product = orderService.selectProductDetail(prod_number);
        model.addAttribute("product", product);
        model.addAttribute("quantity", quantity);

        // 옵션 정보 (PRODUCT_OPTION)
        List<ProductOptionDTO> options = orderService.selectOptionsByProduct(prod_number);
        model.addAttribute("options", options);

        // 사용 가능한 쿠폰 조회
        List<CouponDTO> coupons = orderService.selectAvailableCoupons(memberDTO.getCust_number());
        model.addAttribute("coupons", coupons);

        // 포인트 정보
        int userPoint = orderService.selectUserPoint(memberDTO.getCust_number());
        model.addAttribute("userPoint", userPoint);

        return "product/prodOrder";
    }

    // 주문 처리 (결제 완료 버튼 누른 후)
    @PostMapping("/complete")
    public String completeOrder(@RequestParam int cust_number,
                                @RequestParam int usePoint,
                                @RequestParam int totalPrice) {

        // 1️⃣ 주문 정보 세팅
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setCust_number(cust_number);
        orderDTO.setOrder_number(null); // 시퀀스 <selectKey>로 채워짐
        orderDTO.setPayment(1); // 예: 1=신용카드, 실제 데이터에 맞게 조정
        orderDTO.setPrice(totalPrice);

        // 2️⃣ 주문 생성 → Mapper의 <selectKey>로 order_number 자동 생성됨
        String order_number = orderService.insertOrder(orderDTO);

        // 3️⃣ 포인트 사용 (차감)
        if (usePoint > 0) {
            orderService.usePoint(cust_number, order_number, usePoint);
        }

        // 4️⃣ 포인트 적립 (1%)
        int earn = (int)(totalPrice * 0.01);
        orderService.earnPoint(cust_number, order_number, earn);

        // 5️⃣ 완료 페이지로 이동
        return "product/prodComplete";
    }
}