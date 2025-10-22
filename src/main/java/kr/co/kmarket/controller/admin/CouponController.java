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
            model.addAttribute("coupons", service.searchCoupons(type, keyword, page, size));
            model.addAttribute("totalPages", service.getSearchTotalPages(type, keyword, size));
        } else {
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
    public String issued(@RequestParam(defaultValue = "1") int page,
                         Model model) {

        int size = 10; // 페이지당 표시할 개수

        model.addAttribute("issuedCoupons", service.getIssuedCouponsByPage(page, size));

        model.addAttribute("totalPages", service.getIssuedTotalPages(size));

        model.addAttribute("currentPage", page);

        return "admin/coupon/couponStat";
    }

    // ✅ 쿠폰 발급 처리
    @PostMapping("/issue")
    @ResponseBody
    public Map<String, Object> issueCoupon(@RequestParam("couponno") int couponNo,
                                           HttpSession session) {

        Map<String, Object> result = new HashMap<>();

        Integer custNumber = (Integer) session.getAttribute("user");
        if (custNumber == null) {
            result.put("status", "loginRequired");
            return result;
        }

        int issued = service.issueCoupon(couponNo, custNumber);

        if (issued == 1) {
            result.put("status", "success");
        } else {
            result.put("status", "duplicate");
        }
        return result;
    }

    // ✅ 쿠폰 종료 (발급 중지)
    @PostMapping("/stop")
    @ResponseBody
    public Map<String, Object> stopCoupon(@RequestParam("couponNo") int couponNo) {
        Map<String, Object> result = new HashMap<>();

        try {
            int updated = service.stopCoupon(couponNo);
            if (updated > 0) {
                result.put("status", "success");
            } else {
                result.put("status", "fail");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", "error");
        }

        return result;
    }

    @GetMapping("/detail")
    @ResponseBody
    public CouponDTO getCouponDetail(@RequestParam("couponNo") int couponNo) {
        return service.getCouponDetail(couponNo); // ✅ Service 통해 조회
    }
}
