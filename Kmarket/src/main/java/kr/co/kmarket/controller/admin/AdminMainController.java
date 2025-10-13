package kr.co.kmarket.controller.admin;

import kr.co.kmarket.service.admin.TotalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AdminMainController {
    private final TotalService totalService;

    @GetMapping("/admin")
    public String admin(Model model){
        // 회원 가입 수
        int regYesterday = totalService.getMemberRegYesterday();
        int regToday = totalService.getMemberRegToday();

        int regTotal = regYesterday +  regToday;

        log.info("regYesterday={}, regToday={}, regTotal={}", regYesterday, regToday, regTotal);

        Map<String, Integer> regMap = new HashMap<>();
        regMap.put("yesterday", regYesterday);
        regMap.put("today", regToday);
        regMap.put("total", regTotal);

        model.addAttribute("reg", regMap);

        // 총 주문금액
        int priceYesterday = totalService.getPriceYesterday();
        int priceToday = totalService.getPriceToday();

        int priceTotal = priceYesterday +  priceToday;

        log.info("priceYesterday={}, priceToday={}, priceTotal={}", priceYesterday, priceToday, priceTotal);

        Map<String, Integer> priceMap = new HashMap<>();
        priceMap.put("yesterday", priceYesterday);
        priceMap.put("today", priceToday);
        priceMap.put("total", priceTotal);

        model.addAttribute("price", priceMap);

        // 주문건수
        int orderYesterday = totalService.getOrderYesterday();
        int orderToday = totalService.getOrderToday();

        int orderTotal = orderYesterday +  orderToday;

        log.info("orderYesterday={}, orderToday={}, orderTotal={}", orderYesterday, orderToday, orderTotal);

        Map<String, Integer> orderMap = new HashMap<>();
        orderMap.put("yesterday", orderYesterday);
        orderMap.put("today", orderToday);
        orderMap.put("total", orderTotal);

        model.addAttribute("order", orderMap);

        // 문의 게시물
        int postYesterday = totalService.getPostYesterday();
        int postToday = totalService.getPostToday();

        int postTotal = postYesterday +  postToday;

        log.info("postYesterday={}, postToday={}, orderTotal={}", postYesterday, postToday, postTotal);

        Map<String, Integer> postMap = new HashMap<>();
        postMap.put("yesterday", postYesterday);
        postMap.put("today", postToday);
        postMap.put("total", postTotal);

        model.addAttribute("post", postMap);

        // 상태(임시)
        int cancel = totalService.getStatus(1);
        int bringBack = totalService.getStatus(2);
        int exchange =  totalService.getStatus(3);
        int readyDelivery = totalService.getStatus(4);
        int waitDepost = totalService.getStatus(5);

        Map<String, Integer> statusMap = new HashMap<>();
        statusMap.put("cancel", cancel);
        statusMap.put("bringBack", bringBack);
        statusMap.put("exchange", exchange);
        statusMap.put("readyDelivery", readyDelivery);
        statusMap.put("waitDepost", waitDepost);
        model.addAttribute("status", statusMap);

        return "admin/admin";
    }

}
