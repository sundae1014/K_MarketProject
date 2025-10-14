// kr.co.kmarket.controller.MyRestController.java (새 파일)
package kr.co.kmarket.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.kmarket.dto.MemberDTO;
import kr.co.kmarket.dto.OrderDTO;
import kr.co.kmarket.dto.QnaDTO;
import kr.co.kmarket.service.MyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*; // ⬅️ @RestController 사용
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Slf4j
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
                String finalPath = "/kmarket" + folderPath + encodedFileName;

                order.setEncodedImg1(finalPath);
            }
        }

        return order; // OrderDTO 객체가 JSON으로 자동 변환되어 응답됩니다.
    }

    @GetMapping("/sellerDetail")
    public MemberDTO getSellerDetail(@RequestParam("manufacture") String manufacture) { // ⬅️ MemberDTO 사용
        return myService.getSellerByManufacture(manufacture);
    }

    @PostMapping("/qna")
    public Map<String, Object> registerQna(QnaDTO qnaDTO, HttpSession session) {
        Map<String, Object> resultMap = new HashMap<>();

        // 1. 세션에서 사용자 ID (String)를 로드합니다.
        String userId = (String) session.getAttribute("user_id");
        // ... (로그인 확인 로직 생략)

        // 2. DTO 필드에 세션에서 가져온 ID 및 기본값 설정
        qnaDTO.setUser_id(userId);

        // 🚨 [수정]: type2 값에 따라 type1을 동적으로 결정하는 로직 (기본값 없음)
        String type2 = qnaDTO.getType2();
        String type1Value = null; // 초기값을 null로 설정

        switch (type2) {
            case "상품":
                type1Value = "주문/결제";
                break;
            case "배송":
                type1Value = "배송";
                break;
            case "반품/취소":
            case "교환":
            case "기타":
                type1Value = "취소/반품/교환";
                break;
        }

        if (type1Value == null) {
            resultMap.put("success", false);
            resultMap.put("message", "유효하지 않은 문의 유형(type2)입니다.");
            log.warn("QnA 등록 실패: 유효하지 않은 type2 값='{}'", type2);
            return resultMap;
        }

        qnaDTO.setType1(type1Value); // 동적으로 결정된 type1 값 설정

        // 3. STATUS는 DB가 VARCHAR이므로 문자열 설정
        qnaDTO.setStatus("검토중");
        // -------------------------------------------------------------

        myService.registerQna(qnaDTO); // 서비스 호출

        resultMap.put("success", true);
        resultMap.put("message", "문의가 성공적으로 등록되었습니다.");

        return resultMap;
    }
}