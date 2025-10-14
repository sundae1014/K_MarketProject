package kr.co.kmarket.controller.admin;

import kr.co.kmarket.dto.CouponDTO;
import kr.co.kmarket.service.admin.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/coupon")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService service;

    // 쿠폰 목록 페이지
    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("coupons", service.getCoupons());
        return "/admin/coupon/couponList"; // Thymeleaf list.html
    }

    // 모달 등록 Ajax
    @PostMapping("/register")
    @ResponseBody
    public String register(@RequestBody CouponDTO dto) {
        service.insertCoupon(dto);
        return "success";
    }
}

