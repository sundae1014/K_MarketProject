package kr.co.kmarket.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.kmarket.dto.OrderDTO;
import kr.co.kmarket.service.MyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/my")
public class MyController {

    private final MyService myService;

    @ModelAttribute
    public void addCommonAttributes(Model model, HttpSession session) {
        Integer custNumber = (Integer) session.getAttribute("cust_number");
        if (custNumber != null) {
            int notConfirmedCount = myService.getNotConfirmedOrderCount(custNumber);
            model.addAttribute("notConfirmedCount", notConfirmedCount);
        }
    }

    @GetMapping("/orderDetail") // ⚠ 절대 "/my/orderDetail"로 쓰지 말 것!
    public String getOrderDetail(@RequestParam("orderNumber") int orderNumber,
                                 HttpSession session,
                                 Model model) {

        Integer custNumber = (Integer) session.getAttribute("cust_number");
        if (custNumber == null) {
            return "redirect:/user/login";
        }

        OrderDTO order = myService.getOrderDetailByCustomer(custNumber, orderNumber);
        if (order == null) {
            return "fragments/error :: notFound";
        }

        model.addAttribute("order", order);
        return "my/modal/order-detail-fragment :: fragment";
    }


    @GetMapping("/coupon")
    public String coupon(){
        return "my/coupon";
    }

    @GetMapping("/home")
    public String home(Model model, HttpSession session) throws UnsupportedEncodingException {
        Integer custNumber = (Integer) session.getAttribute("cust_number");
        if (custNumber == null) {
            return "redirect:/member/login";
        }
        List<OrderDTO> recentOrders = myService.getRecentOrders(custNumber);

        DecimalFormat priceFormatter = new DecimalFormat("#,###");
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        for (OrderDTO order : recentOrders) {
            if (order.getPrice() != 0) {
                order.setPriceString(priceFormatter.format(order.getPrice()) + "원");
            }

            if (order.getODate() != null) {
                order.setDateString(dateFormatter.format(order.getODate()));
            } else {
                order.setDateString("날짜 없음");
            }

            // 이미지 경로 인코딩 처리
            String imgPath = order.getImg1();  // ex: /images/product/사조 참치 살코기 안심따개, 135g, 4개_1.jpg
            if (imgPath != null && !imgPath.isEmpty()) {
                int lastSlash = imgPath.lastIndexOf("/");
                String folderPath = imgPath.substring(0, lastSlash + 1);  // /images/product/
                String fileName = imgPath.substring(lastSlash + 1);       // 사조 참치 살코기 안심따개, 135g, 4개_1.jpg

                String encodedFileName = URLEncoder.encode(fileName, "UTF-8");
                order.setEncodedImg1(folderPath + encodedFileName);
            }
        }

        model.addAttribute("recentOrders", recentOrders);
        return "my/home";
    }

    @GetMapping("/option")
    public String option(){
        return "my/option";
    }

    @GetMapping("/order")
    public String order(){
        return "my/order";
    }

    @GetMapping("/point")
    public String point(){
        return "my/point";
    }

    @GetMapping("/qna")
    public String qna(){
        return "my/qna";
    }

    @GetMapping("/review")
    public String review(){
        return "my/review";
    }

}
