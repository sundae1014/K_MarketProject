package kr.co.kmarket.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/coupon")
public class CouponController {

    @GetMapping("/list")
    public String list(){
        return "admin/coupon/couponList";
    }

    @GetMapping("/issued")
    public String issued(){
        return "admin/coupon/couponStat";
    }
}
