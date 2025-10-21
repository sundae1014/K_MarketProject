package kr.co.kmarket.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.kmarket.dto.MemberDTO;
import kr.co.kmarket.service.admin.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user/coupon")
public class UserCouponController {

    private final CouponService couponService;

    @PostMapping("/issued")
    @ResponseBody
    public Map<String, Object> issueCoupon(@RequestParam("couponNo") int couponNo,
                                           HttpSession session) {

        Map<String, Object> result = new HashMap<>();

        // ✅ 로그인 확인 (세션 확인)
        MemberDTO member = (MemberDTO) session.getAttribute("member");
        if (member == null) {
            result.put("status", "loginRequired");
            return result;
        }

        // ✅ 회원 번호 가져오기
        int custNumber = member.getCust_number();

        // ✅ 쿠폰 발급 처리
        int issued = couponService.issueCoupon(couponNo, custNumber);

        if (issued == 1) {
            result.put("status", "success");    // 발급 성공
        } else {
            result.put("status", "duplicate");  // 중복 발급
        }

        return result;
    }
}
