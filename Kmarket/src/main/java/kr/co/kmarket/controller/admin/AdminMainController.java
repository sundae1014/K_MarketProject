package kr.co.kmarket.controller.admin;

import kr.co.kmarket.dto.ChartData;
import kr.co.kmarket.dto.NoticeDTO;
import kr.co.kmarket.dto.QnaDTO;
import kr.co.kmarket.service.admin.TotalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AdminMainController {
    private final TotalService totalService;

    @GetMapping("/admin")
    public String admin(Model model){

        // 차트 그리기
        // 바
        List<String> last5Days = DateUtils.getLastNDates(5, "MM-dd");
        // 주문상태 임시(번호 몇번인지 모름)
        List<ChartData> orderList = totalService.getSalesByStatus(0);
        List<ChartData> paymentList = totalService.getSalesByStatus(1);
        List<ChartData> cancelList  = totalService.getSalesByStatus(5);
        //주문 없는 날짜
        orderList = fillMissingDates(orderList, last5Days);
        paymentList = fillMissingDates(paymentList, last5Days);
        cancelList = fillMissingDates(cancelList, last5Days);

        model.addAttribute("labels", last5Days);
        model.addAttribute("orderData", orderList.stream().map(ChartData::getValue).collect(Collectors.toList()));
        model.addAttribute("paymentData", paymentList.stream().map(ChartData::getValue).collect(Collectors.toList()));
        model.addAttribute("cancelData", cancelList.stream().map(ChartData::getValue).collect(Collectors.toList()));


        // 파이
        List<ChartData> pieData = totalService.getSalesByCategory();
        model.addAttribute("pieData", pieData);
        log.info("pieData={}", pieData);

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

        // 주문상태 임시(번호 몇번인지 모름)
        int cancel = totalService.getStatus(5);
        int bringBack = totalService.getStatus(6);
        int exchange =  totalService.getStatus(7);
        int readyDelivery = totalService.getStatus(2);
        int waitDepost = totalService.getStatus(0);

        Map<String, Integer> statusMap = new HashMap<>();
        statusMap.put("cancel", cancel);
        statusMap.put("bringBack", bringBack);
        statusMap.put("exchange", exchange);
        statusMap.put("readyDelivery", readyDelivery);
        statusMap.put("waitDepost", waitDepost);
        model.addAttribute("status", statusMap);

        // 고객문의 게시판
        List<QnaDTO> cusTableInfo = totalService.getQnaInfo();
        model.addAttribute("cusTableInfo", cusTableInfo);

        List<NoticeDTO> noticeTableInfo = totalService.getNoticeInfo();
        model.addAttribute("noticeTableInfo", noticeTableInfo);

        return "admin/admin";
    }

    public class DateUtils {
        public static List<String> getLastNDates(int n, String pattern) {
            List<String> dates = new ArrayList<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            LocalDate today = LocalDate.now();

            for(int i = n - 1; i >= 0; i--) {
                LocalDate date = today.minusDays(i);
                dates.add(date.format(formatter));
            }

            return dates;
        }
    }

    public static List<ChartData> fillMissingDates(List<ChartData> originalList, List<String> lastNDates) {
        Map<String, Integer> map = new HashMap<>();
        for(ChartData data : originalList) {
            map.put(data.getLabel(), data.getValue());
        }

        List<ChartData> filledList = new ArrayList<>();
        for(String date : lastNDates) {
            int value = map.getOrDefault(date, 0); // 없으면 0
            filledList.add(new ChartData(date, value));
        }

        return filledList;
    }



}
