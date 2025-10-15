package kr.co.kmarket.controller.admin;

import kr.co.kmarket.dto.CouponDTO;
import kr.co.kmarket.service.admin.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;


@Controller
@RequestMapping("/admin/coupon")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService service;

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("coupons", service.getCoupons());
        return "admin/coupon/couponList";
    }

    @PostMapping("/register")
    @ResponseBody
    public String register(@RequestBody CouponDTO coupon) {
        try {
            coupon.setUseCount(0);;
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
}

