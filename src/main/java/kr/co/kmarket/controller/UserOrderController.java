package kr.co.kmarket.controller;

import kr.co.kmarket.dto.OrderDTO;
import kr.co.kmarket.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class UserOrderController {

    private final OrderService orderService;

    // 주문서 작성 페이지
    @GetMapping("/form")
    public String orderForm(Model model) {
        // 나중에 장바구니 데이터나 상품 정보를 넘겨서 주문서에 표시
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