package kr.co.kmarket.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.co.kmarket.dto.*;
import kr.co.kmarket.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.List;

@Slf4j
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
    public String completeOrder(@ModelAttribute OrderDTO orderDTO, HttpSession session) {
        System.out.println("주문 데이터 확인 (변환 후): " + orderDTO);

        // 🧩 누락된 값 기본 세팅
        if (orderDTO.getDelivery() == null) orderDTO.setDelivery(0);
        if (orderDTO.getDiscount() == null) orderDTO.setDiscount(0);
        if (orderDTO.getPrice() == null) orderDTO.setPrice(0);
        if (orderDTO.getUsePoint() == null) orderDTO.setUsePoint(0);

        // ✅ salePrice 계산 (할인 적용)
        if (orderDTO.getSalePrice() == null || orderDTO.getSalePrice() == 0) {
            int salePrice = orderDTO.getPrice() - orderDTO.getDiscount();
            orderDTO.setSalePrice(salePrice);
        }

        // 1️⃣ 주문 메인 저장
        String order_number = orderService.insertOrder(orderDTO);
        orderDTO.setOrder_number(order_number);

        // 2️⃣ 주문 상세 저장 (장바구니 기반으로)
        @SuppressWarnings("unchecked")
        List<CartDTO> orderItems = (List<CartDTO>) session.getAttribute("orderItems");

        if (orderItems != null && !orderItems.isEmpty()) {
            for (CartDTO item : orderItems) {
                OrderDTO detail = new OrderDTO();
                detail.setOrder_number(order_number);
                detail.setProd_number(item.getProd_number());
                detail.setPiece(item.getQuantity()); // ✅ 실제 구매 수량 반영
                detail.setPrice(item.getPrice() * item.getQuantity()); // ✅ 해당 품목 총 금액
                orderService.insertOrderDetail(detail);
            }
        } else {
            // ✅ 바로구매일 경우 대비
            OrderDTO detail = new OrderDTO();
            detail.setOrder_number(order_number);
            detail.setProd_number(orderDTO.getProd_number());
            detail.setPiece(orderDTO.getPiece());
            detail.setPrice(orderDTO.getPrice());
            orderService.insertOrderDetail(detail);
        }

        // 3️⃣ 포인트 처리
        Integer used = orderDTO.getUsePoint();
        if (used != null && used > 0) {
            orderService.usePoint(orderDTO.getCust_number(), order_number, used);
        }

        int earn = (int) (orderDTO.getPrice() * 0.01);
        orderService.earnPoint(orderDTO.getCust_number(), order_number, earn);

        // ✅ 세션 정리 (다시 새 주문시 충돌 방지)
        session.removeAttribute("orderItems");

        // 4️⃣ 완료 페이지 이동
        return "redirect:/order/complete?order_number=" + order_number;
    }


    @GetMapping("/complete")
    public String showComplete(@RequestParam("order_number") String order_number, Model model) {
        OrderDTO orderDTO = orderService.selectOrderByNumber(order_number);
        List<OrderDTO> details = orderService.selectOrderDetails(order_number);
        log.info("orderDTO ={}", orderDTO);
        log.info("details ={}", details);
        int totalPiece = details.stream()
                .mapToInt(OrderDTO::getPiece)
                .sum();

        orderDTO.setPiece(totalPiece);

        // 💡 null-safe 처리
        if (orderDTO.getDelivery() == null) orderDTO.setDelivery(0);
        if (orderDTO.getUsePoint() == null) orderDTO.setUsePoint(0);
        if (orderDTO.getCouponDiscount() == null) orderDTO.setCouponDiscount(0);
        if (orderDTO.getSalePrice() == null) orderDTO.setSalePrice(0);

        // ✅ 총 상품 수량 계산 (상품이 여러 개일 때 합산)
        int totalPieces = 0;
        if (details != null) {
            for (OrderDTO d : details) {
                totalPieces += (d.getPiece() != null ? d.getPiece() : 0);
            }
        }

        // ✅ 최종 결제 금액 계산
        int total = orderDTO.getSalePrice()
                + orderDTO.getDelivery()
                - orderDTO.getCouponDiscount()
                - orderDTO.getUsePoint();
        orderDTO.setPrice(total);

        // ✅ 모델 데이터 전달
        model.addAttribute("order", orderDTO);
        model.addAttribute("details", details);
        model.addAttribute("totalPieces", totalPieces); // 🧩 추가된 부분

        return "product/prodComplete";
    }
}