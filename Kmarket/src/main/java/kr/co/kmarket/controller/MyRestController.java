// kr.co.kmarket.controller.MyRestController.java (새 파일)
package kr.co.kmarket.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.kmarket.dto.OrderDTO;
import kr.co.kmarket.service.MyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*; // ⬅️ @RestController 사용
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

@RequiredArgsConstructor
@RestController // ⬅️ JSON API 전용 컨트롤러
@RequestMapping("/my") // ⬅️ /my 경로를 공유
public class MyRestController {

    private final MyService myService;
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private final DecimalFormat priceFormatter = new DecimalFormat("#,###");

    // 🚨 JS가 요청하는 경로와 일치해야 합니다.
    @GetMapping("/orderDetail")
    public OrderDTO getOrderDetail(@RequestParam("orderNumber") int orderNumber,
                                   HttpSession session) throws UnsupportedEncodingException {

        Integer custNumber = (Integer) session.getAttribute("cust_number");

        if (custNumber == null) {
            // 실제로는 401 Unauthorized 응답을 반환해야 합니다.
            return null;
        }

        OrderDTO order = myService.getOrderDetailByCustomer(custNumber, orderNumber);

        if (order != null) {
            // 가격 및 날짜 문자열 포맷팅
            if (order.getPrice() != 0) {
                order.setPriceString(priceFormatter.format(order.getPrice()) + "원");
                order.setSalePriceString(priceFormatter.format(order.getSalePrice()) + "원");
                order.setDiscountString("-" + priceFormatter.format(order.getDiscount()) + "원");
            } else {
                order.setPriceString("0원");
                order.setSalePriceString("0원");
                order.setDiscountString("0원");
            }

            if (order.getODate() != null) {
                order.setDateString(dateFormatter.format(order.getODate()));
            } else {
                order.setDateString("날짜 없음");
            }

            // 이미지 경로 인코딩 처리
            String imgPath = order.getImg1();
            if (imgPath != null && !imgPath.isEmpty()) {
                int lastSlash = imgPath.lastIndexOf("/");
                String folderPath = imgPath.substring(0, lastSlash + 1);
                String fileName = imgPath.substring(lastSlash + 1);

                String encodedFileName = URLEncoder.encode(fileName, "UTF-8");
                order.setEncodedImg1(folderPath + encodedFileName);
            }
        }

        return order; // OrderDTO 객체가 JSON으로 자동 변환되어 응답됩니다.
    }
}