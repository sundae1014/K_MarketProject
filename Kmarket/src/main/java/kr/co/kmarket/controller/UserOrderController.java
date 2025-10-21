package kr.co.kmarket.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.co.kmarket.dto.*;
import kr.co.kmarket.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.List;

@Controller
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

        List<CartDTO> orderItems = new ArrayList<>();

        // Case 1: 장바구니 주문
        if (cartNumbers != null && !cartNumbers.isEmpty()) {
            String[] numbers = cartNumbers.split(",");
            orderItems = orderService.selectCartItemsByNumbers(numbers);
        }

        // Case 2: 바로구매 주문
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

        // ✅ 공통 데이터
        model.addAttribute("orderItems", orderItems);
        model.addAttribute("userPoint", orderService.selectUserPoint(memberDTO.getCust_number()));
        model.addAttribute("coupons", orderService.selectAvailableCoupons(memberDTO.getCust_number()));

        return "product/prodOrder";
    }

    @Autowired
    public UserOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Integer.class, "payment", new PropertyEditorSupport() {

            @Override
            public void setAsText(String text) {
                int code = switch (text) {
                    case "신용카드" -> 1;
                    case "체크카드" -> 2;
                    case "계좌이체" -> 3;
                    case "무통장입금" -> 4;
                    case "휴대폰결제" -> 5;
                    case "카카오페이" -> 6;
                    default -> 0;
                };
                setValue(code);
            }
        });
    }

    // 주문 처리 (결제 완료 버튼 누른 후)
    @PostMapping("/complete")
    public String completeOrder(@ModelAttribute OrderDTO orderDTO) {
        System.out.println("주문 데이터 확인 (변환 후): " + orderDTO);

        // 누락된 값들 기본 세팅
        if (orderDTO.getDelivery() == null) orderDTO.setDelivery(0);
        if (orderDTO.getDiscount() == null) orderDTO.setDiscount(0);
        if (orderDTO.getPrice() == null) orderDTO.setPrice(0);
        if (orderDTO.getUsePoint() == null) orderDTO.setUsePoint(0);
        if (orderDTO.getPiece() == 0) orderDTO.setPiece(1);

        // salePrice 계산 (할인 적용)
        if (orderDTO.getSalePrice() == null || orderDTO.getSalePrice() == 0) {
            int salePrice = orderDTO.getPrice() - orderDTO.getDiscount();
            orderDTO.setSalePrice(salePrice);
        }

        // 1️⃣ 주문 메인 저장
        String order_number = orderService.insertOrder(orderDTO);
        orderDTO.setOrder_number(order_number);

        // 2️⃣ 주문 상세 저장
        orderService.insertOrderDetail(orderDTO);

        // 3️⃣ 포인트 처리
        Integer used = orderDTO.getUsePoint();
        if (used != null && used > 0) {
            orderService.usePoint(orderDTO.getCust_number(), order_number, used);
        }
        int earn = (int) (orderDTO.getPrice() * 0.01);
        orderService.earnPoint(orderDTO.getCust_number(), order_number, earn);

        // 4️⃣ 완료 페이지 이동
        return "redirect:/order/complete?order_number=" + order_number;
    }

    @GetMapping("/complete")
    public String showComplete(@RequestParam("order_number") String order_number, Model model) {

        // ✅ 1. 주문 기본 정보 + 포인트 정보 포함해서 가져오기
        OrderDTO orderDTO = orderService.selectOrderByNumber(order_number);

        // ✅ 2. 주문 상세(상품 리스트)
        List<OrderDTO> details = orderService.selectOrderDetails(order_number);

        // ✅ 3. null-safe 기본값 세팅
        if (orderDTO.getPrice() == null) orderDTO.setPrice(0);
        if (orderDTO.getUsePoint() == null) orderDTO.setUsePoint(0);
        if (orderDTO.getSalePrice() == null) orderDTO.setSalePrice(0);
        if (orderDTO.getSavePoint() == null) orderDTO.setSavePoint(0);

        // ✅ 4. 모델 등록
        model.addAttribute("order", orderDTO);
        model.addAttribute("details", details);

        return "product/prodComplete";
    }

}