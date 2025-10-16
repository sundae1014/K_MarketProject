package kr.co.kmarket.controller.admin;

import kr.co.kmarket.dto.CouponDTO;
import kr.co.kmarket.service.admin.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@Controller
@RequestMapping("admin/coupon")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService service;

    // ✅ 페이지네이션 + 전체 목록 통합
    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(required = false) String type,
                       @RequestParam(required = false) String keyword,
                       Model model) {

        int pageSize = 10; // 페이지당 10개
        int totalCount = service.countCoupons(type, keyword);
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);
        int offset = (page - 1) * pageSize;

        List<CouponDTO> coupons = service.getCouponsPage(type, keyword, offset, pageSize);

        model.addAttribute("coupons", coupons);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("paramType", type);
        model.addAttribute("paramKeyword", keyword);

        return "admin/coupon/couponList";
    }

    // ✅ 등록 기능
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

    // ✅ 검색 기능 (Ajax)
    @GetMapping("/search")
    @ResponseBody
    public List<CouponDTO> searchCoupons(
            @RequestParam String type,
            @RequestParam String keyword) {
        return service.searchCoupons(type, keyword);
    }

    @GetMapping("/issued")
    public String issuedPage() {
        return "admin/coupon/couponStat";
    }
}
