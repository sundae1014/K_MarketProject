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

        // 3. 전체 개수 및 마지막 페이지 계산 (검색 조건 적용)
        int total = orderService.selectAllOrderCount(searchType, keyword);

        int lastPage = (int) Math.ceil(total / (double) limit);
        int start = (pg - 1) * limit; // OFFSET

        // 4. 페이지 블록 계산
        int startPage = (pg - 1) / block * block + 1;
        int endPage = startPage + block - 1;
        if (endPage > lastPage) {
            endPage = lastPage;
        }

        // 5. 주문 목록 조회 (검색 조건 적용)
        List<OrderDTO> allOrders = orderService.selectAllOrdersListPage(start, limit, searchType, keyword);

        // 6. View로 전달할 페이지 정보 구성
        Map<String, Object> OrderPage = new HashMap<>();
        OrderPage.put("pg", pg);
        OrderPage.put("start", startPage);
        OrderPage.put("end", endPage);
        OrderPage.put("lastPage", lastPage);

        OrderPage.put("searchType", searchType);
        OrderPage.put("keyword", keyword);

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

    @GetMapping("/delivery")
    public String delivery(){
        return "admin/order/delivery";
    }
}
