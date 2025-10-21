// kr.co.kmarket.controller.MyRestController.java (새 파일)
package kr.co.kmarket.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.kmarket.dto.MemberDTO;
import kr.co.kmarket.dto.OrderDTO;
import kr.co.kmarket.dto.ProductReviewDTO;
import kr.co.kmarket.dto.QnaDTO;
import kr.co.kmarket.service.MyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*; // ⬅️ @RestController 사용
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController // ⬅️ JSON API 전용 컨트롤러
@RequestMapping("/my") // ⬅️ /my 경로를 공유
public class MyRestController {

    private final MyService myService;
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private final DecimalFormat priceFormatter = new DecimalFormat("#,###");

    @GetMapping("/orderDetail")
    public OrderDTO getOrderDetail(@RequestParam("orderNumber") String orderNumber,
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

        // 2. DTO 필드에 세션에서 가져온 ID 및 기본값 설정
        qnaDTO.setUser_id(userId);

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
        qnaDTO.setStatus("waiting");
        // -------------------------------------------------------------

        myService.registerQna(qnaDTO); // 서비스 호출

        resultMap.put("success", true);
        resultMap.put("message", "문의가 등록되었습니다.");

        return resultMap;
    }

    @PostMapping("/confirmPurchase")
    public Map<String, Object> confirmPurchase(@RequestParam("orderNumber") String orderNumber,
                                               HttpSession session) {

        Map<String, Object> resultMap = new HashMap<>();

        // 1. 고객 번호(cust_number)를 세션에서 가져와 보안 검증
        // 🚨 세션에 저장된 키가 "cust_number"라고 가정합니다.
        Object custNumberObj = session.getAttribute("cust_number");

        if (custNumberObj == null) {
            resultMap.put("success", false);
            resultMap.put("message", "로그인이 필요합니다.");
            return resultMap;
        }
        // Integer로 캐스팅
        int custNumber = (Integer) custNumberObj;

        try {
            // 2. Service의 검증 및 업데이트 로직 호출
            int result = myService.updateOrderConfirmation(orderNumber, custNumber);

            if (result > 0) {
                // 성공: Service에서 DB 업데이트가 성공한 경우
                resultMap.put("success", true);
                resultMap.put("message", "구매가 확정되었습니다.");
            } else {
                // 실패: Service에서 stat!=5 일 때 return 0을 보낸 경우
                resultMap.put("success", false);
                resultMap.put("message", "구매 확정은 배송완료 상태에서만 가능합니다.");
            }
        } catch (Exception e) {
            log.error("구매 확정 처리 중 오류 발생 - Order: {}, Cust: {}", orderNumber, custNumber, e);
            resultMap.put("success", false);
            resultMap.put("message", "시스템 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
        }

        return resultMap;
    }

    @PostMapping("/cancelOrder")
    public Map<String, Object> cancelOrder(@RequestParam("orderNumber") String orderNumber,
                                           HttpSession session) {

        Map<String, Object> resultMap = new HashMap<>();
        Object custNumberObj = session.getAttribute("cust_number");

        if (custNumberObj == null) {
            resultMap.put("success", false);
            resultMap.put("message", "로그인이 필요합니다.");
            return resultMap;
        }
        int custNumber = (Integer) custNumberObj;

        try {
            // MyService에 주문 취소 로직을 호출합니다. (STAT을 9로 업데이트)
            int result = myService.updateOrderCancel(orderNumber, custNumber);

            if (result > 0) {
                resultMap.put("success", true);
                resultMap.put("message", "주문이 취소되었습니다.");
            } else {
                resultMap.put("success", false);
                // 1 또는 2가 아닌 상태에서 취소를 시도했을 경우 등
                resultMap.put("message", "취소 가능한 주문 상태가 아닙니다.");
            }
        } catch (Exception e) {
            log.error("주문 취소 처리 중 오류 발생 - Order: {}, Cust: {}", orderNumber, custNumber, e);
            resultMap.put("success", false);
            resultMap.put("message", "시스템 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
        }

        return resultMap;
    }

    @PostMapping("/registerReview")
    public Map<String, Object> registerReview(
            @RequestParam("orderNumber") String orderNumber,
            @RequestParam("prodNo") int prodNo,
            @RequestParam("rating") int rating,
            @RequestParam("reviewContent") String reviewContent,
            @RequestParam(name="images", required=false) List<MultipartFile> images,
            HttpSession session) {

        Map<String, Object> resultMap = new HashMap<>();
        Object custNumberObj = session.getAttribute("cust_number");

        if (custNumberObj == null) {
            resultMap.put("success", false);
            resultMap.put("message", "로그인이 필요합니다.");
            return resultMap;
        }
        int cust_number = (Integer) custNumberObj;

        try {
            ProductReviewDTO reviewDTO = ProductReviewDTO.builder()
                    .order_number(orderNumber)
                    .prod_number(prodNo)
                    .cust_number(cust_number)
                    .rating(rating)
                    .content(reviewContent)
                    .build();

            myService.registerReview(reviewDTO, images);

            resultMap.put("success", true);
            resultMap.put("message", "상품평이 등록되었습니다.");

        } catch (IllegalStateException e) {
            // 💡 [개선] 구매 확정 조건 미달 등 비즈니스 로직 오류는 WARN 레벨로 처리
            log.warn("리뷰 작성 권한/조건 오류 발생 (Cust: {}): {}", cust_number, e.getMessage());
            resultMap.put("success", false);
            resultMap.put("message", e.getMessage());

        } catch (Exception e) {
            // 시스템 레벨의 예상치 못한 오류는 ERROR 레벨로 처리
            log.error("상품평 등록 처리 중 시스템 오류 발생 - Cust: {}", cust_number, e);
            resultMap.put("success", false);
            resultMap.put("message", "시스템 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
        }

        return resultMap;
    }

    @PostMapping("/return")
    public Map<String, Object> requestReturn(@RequestBody OrderDTO orderDTO, HttpSession session) {
        Map<String, Object> resultMap = new HashMap<>();

        // 1. 로그인 검증
        Integer custNumber = (Integer) session.getAttribute("cust_number");
        if (custNumber == null) {
            resultMap.put("success", false);
            resultMap.put("message", "로그인이 필요합니다.");
            return resultMap;
        }

        // 2. DTO에 현재 로그인 사용자 번호 설정 (보안: 요청 데이터 외에 세션 정보 사용)
        orderDTO.setCust_number(custNumber);

        try {
            int result = myService.orderReturn(orderDTO);
            if (result > 0) {
                resultMap.put("success", true);
                resultMap.put("message", "반품 요청이 접수되었습니다.");
            } else if(result == 0) {
                resultMap.put("success", false);
                resultMap.put("message", "배송 완료 후 반품 접수 가능합니다.");
            }else {
                resultMap.put("success", false);
                resultMap.put("message", "반품 요청에 실패했습니다.");
            }
        } catch (Exception e) {
            log.error("반품 요청 중 시스템 오류 발생 - Order: {}", orderDTO.getOrder_number(), e);
            resultMap.put("success", false);
            resultMap.put("message", "시스템 오류가 발생했습니다.");
        }
        return resultMap;
    }

    @PostMapping("/exchange")
    public Map<String, Object> requestExchange(@RequestBody OrderDTO orderDTO, HttpSession session) {
        Map<String, Object> resultMap = new HashMap<>();

        Integer custNumber = (Integer) session.getAttribute("cust_number");
        if (custNumber == null) {
            resultMap.put("success", false);
            resultMap.put("message", "로그인이 필요합니다.");
            return resultMap;
        }

        orderDTO.setCust_number(custNumber);

        try {
            int result = myService.orderExchange(orderDTO);
            if (result > 0) {
                resultMap.put("success", true);
                resultMap.put("message", "교환 요청이 접수되었습니다.");
            } else if(result == 0) {
                resultMap.put("success", false);
                resultMap.put("message", "배송완료 후 교환 접수 가능합니다.");
            } else {
                resultMap.put("success", false);
                resultMap.put("message", "교환 요청에 실패했습니다.");
            }
        } catch (Exception e) {
            log.error("교환 요청 중 시스템 오류 발생 - Order: {}", orderDTO.getOrder_number(), e);
            resultMap.put("success", false);
            resultMap.put("message", "시스템 오류가 발생했습니다.");
        }
        return resultMap;
    }
}