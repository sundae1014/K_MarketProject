package kr.co.kmarket.controller.admin;

import jakarta.servlet.http.HttpSession;
import kr.co.kmarket.dto.AdminOrderDetailDTO;
import kr.co.kmarket.dto.OrderDTO;
import kr.co.kmarket.dto.PointDTO;
import kr.co.kmarket.service.admin.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/order")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/list")
    public String list(Model model, HttpSession session,
                       @RequestParam(name = "pg", defaultValue = "1") int pg,
                       @RequestParam(name = "searchType", required = false) String searchType,
                       @RequestParam(name = "keyword", required = false) String keyword) {

        // 1. 로그인 체크 (기존 로직 유지)
        Integer custNumber = (Integer) session.getAttribute("cust_number");

        if (custNumber == null) {
            return "redirect:/member/login";
        }

        // 2. 페이지네이션 설정
        int limit = 10;
        int block = 10;

        int total = orderService.selectAllOrderCount(searchType, keyword); // OrderService의 메서드에 searchType, keyword 전달 가정
        int lastPage = (int) Math.ceil(total / (double) limit);
        int start = (pg - 1) * limit;

        int startPage = (pg - 1) / block * block + 1;
        int endPage = startPage + block - 1;
        if (endPage > lastPage) {
            endPage = lastPage;
        }

        List<OrderDTO> allOrders = orderService.selectAllOrdersListPage(start, limit, searchType, keyword);

        Map<String, Object> OrderPage = new HashMap<>(); // list.html은 OrderPage 사용
        OrderPage.put("pg", pg);
        OrderPage.put("start", startPage);
        OrderPage.put("end", endPage);
        OrderPage.put("lastPage", lastPage);
        OrderPage.put("searchType", searchType); // Thymeleaf 오류 방지 및 검색 유지
        OrderPage.put("keyword", keyword);       // Thymeleaf 오류 방지 및 검색 유지

        model.addAttribute("allOrders", allOrders);
        model.addAttribute("OrderPage", OrderPage);

        return "/admin/order/list";
    }

    @GetMapping("/detail/{order_number}")
    @ResponseBody
    public Map<String, Object> getOrderDetail(@PathVariable("order_number") String orderNumber) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 1. 하나의 쿼리로 주문 기본 정보와 상품 리스트를 한 번에 조회
            OrderDTO order = orderService.selectOrderDetail(orderNumber);

            if (order != null) {
                response.put("success", true);
                response.put("order", order);
            } else {
                response.put("success", false);
                response.put("message", "해당 주문번호의 상세 정보가 없습니다.");
            }
        } catch (Exception e) {
            log.error("주문 상세 정보 로드 오류: " + orderNumber, e);
            response.put("success", false);
            response.put("message", "데이터 처리 중 서버 오류가 발생했습니다.");
        }

        return response;
    }

    @GetMapping("/detail-info")
    @ResponseBody
    public OrderDTO getOrderDetailInfo(@RequestParam("orderNumber") String orderNumber) {
        OrderDTO orderInfo = orderService.selectOrderInfoByOrderNumber(orderNumber);
        return orderInfo;
    }

    @PostMapping("/delivery-input")
    @ResponseBody
    public Map<String, Object> deliveryInput(@RequestBody Map<String, String> payload) {
        // JS에서 보낸 폼 데이터 추출
        String orderNumber = payload.get("orderNumber");
        String deliveryCompany = payload.get("deliveryCompany");
        String trackingNumber = payload.get("trackingNumber");

        int result = orderService.updateDelivery(orderNumber, deliveryCompany, trackingNumber);

        Map<String, Object> response = new HashMap<>();
        if (result > 0) {
            response.put("success", true);
        } else {
            response.put("success", false);
            response.put("message", "등록 중 오류가 발생했거나, 주문번호를 찾을 수 없습니다.");
        }
        return response;
    }

    @GetMapping("/delivery")
    public String deliveryList(Model model, HttpSession session, @RequestParam(name = "pg", defaultValue = "1") int pg,
                               @RequestParam(name = "searchType", required = false) String searchType,
                               @RequestParam(name = "keyword", required = false) String keyword){
        Integer custNumber = (Integer) session.getAttribute("cust_number");

        if (custNumber == null) {
            return "redirect:/member/login";
        }

        int limit = 10;
        int block = 10;

        int total = orderService.selectDeliveryOrderCount();
        int lastPage = (int) Math.ceil(total / (double) limit);
        int start = (pg - 1) * limit; // OFFSET

        int startPage = (pg - 1) / block * block + 1;
        int endPage = startPage + block - 1;
        if (endPage > lastPage) {
            endPage = lastPage;
        }

        List<OrderDTO> deliveryOrders = orderService.selectDeliveryOrdersListPage(start, limit);

        Map<String, Object> DeliveryPage = new HashMap<>(); // delivery.html은 DeliveryPage 사용
        DeliveryPage.put("pg", pg);
        DeliveryPage.put("start", startPage);
        DeliveryPage.put("end", endPage);
        DeliveryPage.put("lastPage", lastPage);

        DeliveryPage.put("searchType", searchType);
        DeliveryPage.put("keyword", keyword);

        model.addAttribute("deliveryOrders", deliveryOrders);
        model.addAttribute("DeliveryPage", DeliveryPage);

        return "/admin/order/delivery";
    }

    @GetMapping("/delivery-detail/{orderNumber}")
    @ResponseBody
    public Map<String, Object> deliveryDetail(@PathVariable String orderNumber,
                                              @RequestParam(name = "trackingNumber", required = false) Integer trackingNumber) {

        // 운송장 번호 파라미터를 Service로 함께 전달
        OrderDTO orderDetail = orderService.selectDeliveryOrderDetail(orderNumber, trackingNumber);

        Map<String, Object> response = new HashMap<>();
        if (orderDetail != null) {
            response.put("success", true);
            response.put("order", orderDetail);
        } else {
            response.put("success", false);
        }
        return response;
    }


}
