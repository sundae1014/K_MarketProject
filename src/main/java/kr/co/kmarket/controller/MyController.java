package kr.co.kmarket.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.kmarket.dto.*;
import kr.co.kmarket.service.MyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
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
            int waitingQna = myService.selectWaitingQna(custNumber);
            int couponCount = myService.selectCountCoupon();
            int totalPoint = myService.selectAllPoints(custNumber);
            model.addAttribute("notConfirmedCount", notConfirmedCount);
            model.addAttribute("waitingQna", waitingQna);
            model.addAttribute("couponCount", couponCount);
            model.addAttribute("totalPoint", totalPoint);
        }
    }

    @GetMapping("/point")
    public String point(Model model, HttpSession session, @RequestParam(name = "pg", defaultValue = "1") int pg){
        Integer custNumber = (Integer) session.getAttribute("cust_number");

        if (custNumber == null) {
            return "redirect:/member/login";
        }

        // 1. 기본 설정
        int limit = 10;
        int block = 10;

        // 2. 전체 게시물 수 및 페이지 계산
        int total = myService.selectPointCountByCustNumber(custNumber);
        int lastPage = (int) Math.ceil(total / (double) limit);
        int start = (pg - 1) * limit; // OFFSET

        // 3. 페이징 블록 계산
        int startPage = (pg - 1) / block * block + 1;
        int endPage = startPage + block - 1;
        if (endPage > lastPage) {
            endPage = lastPage;
        }

        // 4. 포인트 목록 조회
        List<PointDTO> points = myService.selectPointsListPage(custNumber, start, limit);

        // 5. View로 전달할 페이지 정보 구성 (QnaPage 형식과 동일)
        Map<String, Object> PointPage = new HashMap<>();
        PointPage.put("pg", pg);
        PointPage.put("start", startPage);
        PointPage.put("end", endPage);
        PointPage.put("lastPage", lastPage);

        model.addAttribute("points", points);
        model.addAttribute("PointPage", PointPage);

        return "my/point";
    }

    @GetMapping("/home")
    public String home(Model model, HttpSession session) throws UnsupportedEncodingException {
        Integer custNumber = (Integer) session.getAttribute("cust_number");
        String user_id = (String) session.getAttribute("user_id");
        if (custNumber == null) {
            return "redirect:/member/login";
        }

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
                try { // try-catch 블록 추가
                    int lastSlash = imgPath.lastIndexOf("/");
                    String folderPath = imgPath.substring(0, lastSlash + 1);
                    String fileName = imgPath.substring(lastSlash + 1);

                    // 예외 처리
                    String encodedFileName = URLEncoder.encode(fileName, "UTF-8");

                    order.setEncodedImg1(folderPath + encodedFileName);
                } catch (UnsupportedEncodingException e) {
                    // UTF-8은 Java에서 항상 지원되므로, 발생 시 RuntimeException으로 처리하는 것이 일반적입니다.
                    throw new RuntimeException("파일 이름 URL 인코딩 중 오류 발생: " + order.getImg1(), e);
                }
            }
        }

        model.addAttribute("recentOrders", recentOrders);

        List<ProductReviewDTO> recentReviews = myService.getRecentReviews(custNumber);
        model.addAttribute("recentReviews", recentReviews);

        List<QnaDTO> recentQnas = myService.getRecentQnas(user_id);
        model.addAttribute("recentQnas", recentQnas);

        MemberDTO userOptions = myService.selectUserOptions(custNumber);
        model.addAttribute("userOptions", userOptions);

        List<PointDTO> recentPoints = myService.selectPoints(custNumber);
        model.addAttribute("recentPoints", recentPoints);

        return "my/home";
    }

    @GetMapping("/option")
    public String option(Model model, HttpSession session) throws UnsupportedEncodingException{
        Integer custNumber = (Integer) session.getAttribute("cust_number");

        MemberDTO userInfo = myService.selectUserInfo(custNumber);

        String maskedUid = "";
        String uid = userInfo.getCustid();
        if (uid != null && uid.length() > 4) {
            // ID 길이에서 4를 뺀 위치까지는 유지하고, 나머지는 *로 대체
            maskedUid = uid.substring(0, uid.length() - 4) + "****";
        } else if (uid != null) {
            // ID 길이가 4자 이하이면 전부 * 처리
            maskedUid = "*".repeat(uid.length());
        }
        model.addAttribute("maskedUid", maskedUid);

        String maskedName = "";
        String name = userInfo.getName();
        if (name != null) {
            if (name.length() == 2) {
                // 2글자 이름: 홍*
                maskedName = name.charAt(0) + "*";
            } else if (name.length() >= 3) {
                // 3글자 이상 이름: 홍*동
                int length = name.length();
                maskedName = name.charAt(0)
                        + "*"
                        + name.substring(length - 1);
            } else {
                maskedName = name;
            }
        }
        model.addAttribute("maskedName", maskedName);

        if (userInfo != null && userInfo.getEmail() != null) {
            String email = userInfo.getEmail();
            if (email.contains("@")) {
                String[] parts = email.split("@");
                model.addAttribute("emailId", parts[0]);
                model.addAttribute("emailDomain", parts[1]);
            }
        }

        if (userInfo != null && userInfo.getHp() != null) {
            String hp = userInfo.getHp().replaceAll("[^0-9]", ""); // 숫자만 추출 (예: 01012345678)

            if (hp.length() == 11) { // 일반적인 010-XXXX-XXXX 패턴
                model.addAttribute("hp1", hp.substring(0, 3)); // 010
                model.addAttribute("hp2", hp.substring(3, 7)); // 1234
                model.addAttribute("hp3", hp.substring(7));    // 5678
            }
        }

        model.addAttribute("userInfo", userInfo);

        return "my/option";
    }

    @GetMapping("/order")
    public String order(Model model, HttpSession session, @RequestParam(name = "pg", defaultValue = "1") int pg) {

        Integer custNumber = (Integer) session.getAttribute("cust_number");

        if (custNumber == null) {
            // 세션에 cust_number가 없으면 로그인 페이지로 리다이렉트
            return "redirect:/member/login";
        }

        // 1. 기본 설정 (페이지당 게시물 수, 페이지 블록 크기)
        int limit = 10;
        int block = 10;

        // 2. 전체 게시물 수 및 페이지 계산
        // Service 호출하여 전체 주문 상세 아이템(ROW) 개수 조회
        int total = myService.selectOrderCountByCustNumber(custNumber);
        int lastPage = (int) Math.ceil(total / (double) limit);
        int start = (pg - 1) * limit; // MyBatis의 #{start} (OFFSET)

        // 3. 페이징 블록 계산
        int startPage = (pg - 1) / block * block + 1;
        int endPage = startPage + block - 1;
        if (endPage > lastPage) {
            endPage = lastPage;
        }

        // 4. 주문 내역 목록 조회 (주문 상세 아이템 기준)
        List<OrderDTO> orders = myService.selectOrdersListPage(custNumber, start, limit);

        for (OrderDTO order : orders) {
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
                try { // try-catch 블록 추가
                    int lastSlash = imgPath.lastIndexOf("/");
                    String folderPath = imgPath.substring(0, lastSlash + 1);
                    String fileName = imgPath.substring(lastSlash + 1);

                    // 예외 처리
                    String encodedFileName = URLEncoder.encode(fileName, "UTF-8");

                    order.setEncodedImg1(folderPath + encodedFileName);
                } catch (UnsupportedEncodingException e) {
                    // UTF-8은 Java에서 항상 지원되므로, 발생 시 RuntimeException으로 처리하는 것이 일반적입니다.
                    throw new RuntimeException("파일 이름 URL 인코딩 중 오류 발생: " + order.getImg1(), e);
                }
            }
        }

        // 5. View로 전달할 페이지 정보 구성
        Map<String, Object> OrderPage = new HashMap<>();
        OrderPage.put("pg", pg);
        OrderPage.put("start", startPage);
        OrderPage.put("end", endPage);
        OrderPage.put("last", lastPage);
        OrderPage.put("total", total);

        // 6. View로 데이터 전달
        model.addAttribute("orders", orders); // 목록 데이터 (주문 상세 DTO 리스트)
        model.addAttribute("OrderPage", OrderPage); // 페이지 정보

        return "my/order";
    }

    @GetMapping("/coupon")
    public String coupon(Model model, HttpSession session,
                         @RequestParam(defaultValue = "1") int pg) {

        Integer custNumber = (Integer) session.getAttribute("cust_number");

        if (custNumber == null) {
            log.warn("쿠폰 페이지 접근 실패: 로그인하지 않은 사용자");
            return "redirect:/member/login";
        }

        final int pageSize = 10; // 페이지당 표시할 쿠폰 수
        final int blockSize = 10; // 페이지 블록 수

        int total = myService.selectCouponCountByCustNumber(custNumber);

        int currentPage = pg;
        int lastPage = (int) Math.ceil((double) total / pageSize);

        if (currentPage < 1) currentPage = 1;
        if (currentPage > lastPage && lastPage > 0) currentPage = lastPage;
        if (lastPage == 0) currentPage = 1;

        int start = (currentPage - 1) * pageSize; // OFFSET 값

        int startPage = (currentPage - 1) / blockSize * blockSize + 1;
        int endPage = startPage + blockSize - 1;

        if (endPage > lastPage) {
            endPage = lastPage;
        }

        List<CouponDTO> coupons = myService.selectCouponsListPage(custNumber, start, pageSize);

        // 5. View로 전달할 페이지 정보 구성: QnA 페이지와 동일한 Map 형식 사용
        Map<String, Object> CouponPage = new HashMap<>(); // ⬅️ 변수명, 타입 변경
        CouponPage.put("pg", currentPage);
        CouponPage.put("start", startPage);
        CouponPage.put("end", endPage);
        CouponPage.put("lastPage", lastPage); // ⬅️ lastPage 추가 (QnA 형식)

        model.addAttribute("coupons", coupons);
        model.addAttribute("CouponPage", CouponPage); // ⬅️ 모델 속성명 변경

        return "my/coupon";
    }

    @GetMapping("/qna")
    public String qna(@RequestParam(defaultValue = "1") int pg, Model model, HttpSession session){
        String userId = (String) session.getAttribute("user_id"); // 세션에서 user_id를 가져옴

        if (userId == null) {
            return "redirect:/member/login";
        }

        // 🚨 1. 기본 설정 (페이지당 게시물 수, 페이지 블록 크기)
        int limit = 10;
        int block = 10;

        // 🚨 2. 전체 게시물 수 및 페이지 계산
        int total = myService.selectQnaCountByUserId(userId); // MyService 호출하여 전체 개수 조회
        int lastPage = (int) Math.ceil(total / (double) limit);
        int start = (pg - 1) * limit; // MyBatis의 #{start} (OFFSET)

        // 🚨 3. 페이징 블록 계산
        int startPage = (pg - 1) / block * block + 1;
        int endPage = startPage + block - 1;
        if (endPage > lastPage) {
            endPage = lastPage;
        }

        // 4. QnA 목록 조회
        // 🚨 MyService에 selectQnaListPage(userId, start, limit) 메서드가 있어야 합니다.
        List<QnaDTO> qnas = myService.selectQnaListPage(userId, start, limit);

        // 5. View로 전달할 페이지 정보 구성
        Map<String, Object> QnaPage = new HashMap<>();
        QnaPage.put("pg", pg);
        QnaPage.put("start", startPage);
        QnaPage.put("end", endPage);
        QnaPage.put("lastPage", lastPage); // 마지막 페이지를 View에 전달

        model.addAttribute("qnas", qnas);
        model.addAttribute("QnaPage", QnaPage);

        // Thymeleaf th:if/unless 구조를 위해 검색 조건은 null로 설정 (검색 기능이 없으므로)
        model.addAttribute("searchType", null);
        model.addAttribute("keyword", null);

        return "my/qna";
    }

    @GetMapping("/review")
    public String review(Model model, HttpSession session, @RequestParam(name = "pg", defaultValue = "1") int pg) {

        Integer custNumber = (Integer) session.getAttribute("cust_number");

        if (custNumber == null) {
            // 세션에 cust_number가 없으면 로그인 페이지로 리다이렉트
            return "redirect:/member/login";
        }

        // 1. 기본 설정 (페이지당 게시물 수, 페이지 블록 크기)
        int limit = 10;
        int block = 10;

        // 2. 전체 게시물 수 및 페이지 계산
        int total = myService.selectReviewCountByCustNumber(custNumber); // Service 호출하여 전체 개수 조회
        int lastPage = (int) Math.ceil(total / (double) limit);
        int start = (pg - 1) * limit; // MyBatis의 #{start} (OFFSET)

        // 3. 페이징 블록 계산
        int startPage = (pg - 1) / block * block + 1;
        int endPage = startPage + block - 1;
        if (endPage > lastPage) {
            endPage = lastPage;
        }

        // 4. 리뷰 목록 조회
        List<ProductReviewDTO> reviews = myService.selectReviewsListPage(custNumber, start, limit); // Service 호출

        // 5. View로 전달할 페이지 정보 구성
        Map<String, Object> ReviewPage = new HashMap<>();
        ReviewPage.put("pg", pg);
        ReviewPage.put("start", startPage);
        ReviewPage.put("end", endPage);
        ReviewPage.put("last", lastPage);
        ReviewPage.put("total", total);

        // 6. View로 데이터 전달
        model.addAttribute("reviews", reviews); // 목록 데이터
        model.addAttribute("ReviewPage", ReviewPage); // 페이지 정보

        return "my/review";
    }

}
