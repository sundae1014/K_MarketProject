package kr.co.kmarket.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.kmarket.dto.OrderDTO;
import kr.co.kmarket.dto.ProductReviewDTO;
import kr.co.kmarket.dto.QnaDTO;
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
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private final DecimalFormat priceFormatter = new DecimalFormat("#,###");

    @ModelAttribute
    public void addCommonAttributes(Model model, HttpSession session) {
        Integer custNumber = (Integer) session.getAttribute("cust_number");
        if (custNumber != null) {
            int notConfirmedCount = myService.getNotConfirmedOrderCount(custNumber);
            model.addAttribute("notConfirmedCount", notConfirmedCount);
        }
    }

    @GetMapping("/coupon")
    public String coupon(){
        return "my/coupon";
    }

    @GetMapping("/home")
    public String home(Model model, HttpSession session) throws UnsupportedEncodingException {
        Integer custNumber = (Integer) session.getAttribute("cust_number");
        String user_id = (String) session.getAttribute("user_id");
        if (custNumber == null) {
            return "redirect:/member/login";
        }
        // ... (home 뷰를 위한 나머지 로직 유지) ...
        List<OrderDTO> recentOrders = myService.getRecentOrders(custNumber);

        for (OrderDTO order : recentOrders) {
            if (order.getPrice() != 0) {
                order.setPriceString(priceFormatter.format(order.getPrice()) + "원");
            }
            if (order.getODate() != null) {
                order.setDateString(dateFormatter.format(order.getODate()));
            } else {
                order.setDateString("날짜 없음");
            }
            String imgPath = order.getImg1();
            if (imgPath != null && !imgPath.isEmpty()) {
                int lastSlash = imgPath.lastIndexOf("/");
                String folderPath = imgPath.substring(0, lastSlash + 1);
                String fileName = imgPath.substring(lastSlash + 1);
                String encodedFileName = URLEncoder.encode(fileName, "UTF-8");
                order.setEncodedImg1(folderPath + encodedFileName);
            }
        }

        model.addAttribute("recentOrders", recentOrders);

        List<ProductReviewDTO> recentReviews = myService.getRecentReviews(custNumber);
        model.addAttribute("recentReviews", recentReviews);

        List<QnaDTO> recentQnas = myService.getRecentQnas(user_id);
        model.addAttribute("recentQnas", recentQnas);

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
