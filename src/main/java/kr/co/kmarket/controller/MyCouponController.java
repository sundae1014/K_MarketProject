package kr.co.kmarket.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.kmarket.dto.CouponDTO;
import kr.co.kmarket.dto.MemberDTO;
import kr.co.kmarket.service.MyCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/my/coupon")
public class MyCouponController {

    private final MyCouponService couponService;

    @GetMapping("/coupon")
    public String myCoupons(HttpSession session, Model model) {
        MemberDTO member = (MemberDTO) session.getAttribute("member");

        if (member == null) {
            return "redirect:/member/login";
        }

        int custNumber = member.getCust_number();

        List<CouponDTO> coupons = couponService.getUserCoupons(custNumber);

        model.addAttribute("coupons", coupons);

        return "my/coupon";
    }
}
