package kr.co.kmarket.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.kmarket.dto.*;
import kr.co.kmarket.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class UserOrderController {

    private final OrderService orderService;

    // ✅ 주문서 작성 페이지
    @GetMapping("/form")
    public String orderForm(HttpSession session,
                            @RequestParam(required = false) Integer prod_number,
                            @RequestParam(required = false, defaultValue = "1") Integer quantity,
                            @RequestParam(required = false) String cartNumbers,
                            Model model) {

        MemberDTO memberDTO = (MemberDTO) session.getAttribute("member");
        if (memberDTO == null) return "redirect:/member/login";

        // ✅ 구매자 정보
        model.addAttribute("buyer", memberDTO);

        List<CartDTO> orderItems = new ArrayList<>();

        // ✅ Case 1: 장바구니 주문
        if (cartNumbers != null && !cartNumbers.isEmpty()) {
            String[] numbers = cartNumbers.split(",");
            orderItems = orderService.selectCartItemsByNumbers(numbers);
        }
        // ✅ Case 2: 바로구매 주문
        else if (prod_number != null) {
            ProductDTO p = orderService.selectProductDetail(prod_number);

            CartDTO temp = new CartDTO();
            temp.setProd_number(p.getProd_number());
            temp.setProd_name(p.getProd_name());
            temp.setImg_1(p.getImg_1());
            temp.setDiscount(p.getDiscount());
            temp.setPrice(p.getPrice());
            temp.setQuantity(quantity);

            List<ProductOptionDTO> opts = orderService.selectOptionsByProduct(prod_number);
            if (opts != null && !opts.isEmpty()) {
                temp.setOpt_name(opts.get(0).getOpt_name());
                temp.setOpt_price(opts.get(0).getOpt_price());
            } else {
                temp.setOpt_name("free");
                temp.setOpt_price(0);
            }

            orderItems.add(temp);
        }

        // ✅ 총 상품 금액 계산 (basePrice)
        int basePrice = 0;
        for (CartDTO item : orderItems) {
            int salePrice = item.getPrice(); // 기본가격

            // 할인 적용
            if (item.getDiscount() > 0) {
                salePrice = (int) (salePrice * (1 - (item.getDiscount() / 100.0)));
            }

            // 옵션 가격 추가
            salePrice += item.getOpt_price();

            basePrice += salePrice * item.getQuantity();
        }

        // ✅ 공통 데이터
        model.addAttribute("orderItems", orderItems);
        model.addAttribute("userPoint", orderService.selectUserPoint(memberDTO.getCust_number()));
        model.addAttribute("coupons", orderService.selectAvailableCoupons(memberDTO.getCust_number()));
        model.addAttribute("basePrice", basePrice); // ✅ 추가됨 (NaN 해결 핵심)

        return "product/prodOrder";
    }

    // ✅ 주문 처리 (결제 완료)
    @PostMapping("/complete")
    public String completeOrder(@ModelAttribute OrderDTO orderDTO) {

        // 1. 주문 저장
        String order_number = orderService.insertOrder(orderDTO);

        // 2. 주문 상세 저장
        orderDTO.setOrder_number(order_number);
        orderService.insertOrderDetail(orderDTO);

        // 3. 쿠폰 사용 처리
        if (orderDTO.getCouponNo() > 0) {
            orderService.useCoupon(orderDTO.getCouponNo(), orderDTO.getCust_number());
        }

        // 4. 포인트 차감 및 적립
        if (orderDTO.getUsePoint() > 0) {
            orderService.usePoint(orderDTO.getCust_number(), order_number, orderDTO.getUsePoint());
        }
        int earn = (int) (orderDTO.getPrice() * 0.01);
        orderService.earnPoint(orderDTO.getCust_number(), order_number, earn);

        // 5. 주문 완료 페이지 이동
        return "redirect:/order/complete?order_number=" + order_number;
    }

    // ✅ 주문 완료 페이지
    @GetMapping("/complete")
    public String showComplete(@RequestParam("order_number") String order_number, Model model) {
        OrderDTO order = orderService.selectOrderByNumber(order_number);
        List<OrderDTO> details = orderService.selectOrderDetails(order_number);

        model.addAttribute("order", order);
        model.addAttribute("details", details);

        return "product/prodComplete";
    }
}
