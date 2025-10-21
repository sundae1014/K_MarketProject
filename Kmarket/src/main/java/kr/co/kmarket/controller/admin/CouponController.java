package kr.co.kmarket.controller.admin;

import jakarta.servlet.http.HttpSession;
import kr.co.kmarket.dto.CouponDTO;
import kr.co.kmarket.service.admin.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin/coupon")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService service;

    // ✅ 쿠폰 목록 + 검색 + 페이징
    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(required = false) String type,
                       @RequestParam(required = false) String keyword,
                       Model model) {

        int size = 10; // 페이지당 10개

        if (keyword != null && !keyword.isEmpty()) {
            // ✅ 검색 결과 조회
            model.addAttribute("coupons", service.searchCoupons(type, keyword, page, size));
            model.addAttribute("totalPages", service.getSearchTotalPages(type, keyword, size));
        } else {
            // ✅ 일반 목록 조회
            model.addAttribute("coupons", service.getCouponsByPage(page, size));
            model.addAttribute("totalPages", service.getTotalPages(size));
        }

        model.addAttribute("currentPage", page);
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);

        return "admin/coupon/couponList";
    }

    // ✅ 쿠폰 등록
    @PostMapping("/register")
    @ResponseBody
    public String register(@RequestBody CouponDTO coupon) {
        try {
            coupon.setUseCount(0);
            coupon.setStatus("미사용");
            coupon.setSayoung(0);
            coupon.setBargup(0);
            coupon.setIssueDate(new Date(System.currentTimeMillis()));
            service.insertCoupon(coupon);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    // ✅ 쿠폰 발급 현황 페이지 이동
    @GetMapping("/issued")
    public String issued() {
        return "admin/coupon/couponStat";
    }

    @PostMapping("/issue")
    @ResponseBody
    public Map<String, Object> issueCoupon(@RequestParam("couponno") int couponNo,
                                           HttpSession session) {

        Map<String, Object> result = new HashMap<>();

        // ✅ 로그인 확인
        Integer custNumber = (Integer) session.getAttribute("user");
        if (custNumber == null) {
            result.put("status", "loginRequired");
            return result;
        }

        // ✅ 서비스 호출
        int issued = service.issueCoupon(couponNo, custNumber);

        if (issued == 1) {
            result.put("status", "success"); // 발급 성공
        } else {
            result.put("status", "duplicate"); // 이미 발급됨
        }
        return result;
    }
}
