package kr.co.kmarket.controller;

import java.util.List;

import kr.co.kmarket.Entity.COUPONEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/coupon")
public class CouponController {

    @Autowired
    private com.example.demo.Service.CouponService couponService;

    // 전체 쿠폰 조회
    @GetMapping
    public List<COUPONEntity> getAllCoupons() {
        return couponService.getAllCoupons();
    }

    // 특정 쿠폰 조회
    @GetMapping("/{id}")
    public COUPONEntity getCoupon(@PathVariable int id) {
        return couponService.getCouponById(id);
    }

    //  쿠폰 추가
    @PostMapping
    public COUPONEntity addCoupon(@RequestBody COUPONEntity coupon) {
        return couponService.addCoupon(coupon);
    }

    //  쿠폰 삭제
    @DeleteMapping("/{id}")
    public void deleteCoupon(@PathVariable int id) {
        couponService.deleteCoupon(id);
    }

    //  쿠폰 수정)
    @PutMapping("/{id}")
    public COUPONEntity updateCoupon(@PathVariable int id, @RequestBody COUPONEntity coupon) {
        coupon.setCouponNo(id); 
        return couponService.addCoupon(coupon); // save()는 PK 있으면 update, 없으면 insert
    }
}
