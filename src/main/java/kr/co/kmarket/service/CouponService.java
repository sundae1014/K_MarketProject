/*package com.example.demo.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.COUPONEntity;
import com.example.demo.Repository.CouponRepository;


@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    // 전체 쿠폰 조회
    public List<COUPONEntity> getAllCoupons() {
        return couponRepository.findAll();
    }

    // 특정 쿠폰 조회
    public COUPONEntity getCouponById(int couponNo) {
        return couponRepository.findById(couponNo).orElse(null);
    }

    // 쿠폰 추가
    public COUPONEntity addCoupon(COUPONEntity coupon) {
        return couponRepository.save(coupon);
    }

    // 쿠폰 삭제
    public void deleteCoupon(int couponNo) {
        couponRepository.deleteById(couponNo);
    }

    // 쿠폰 수정
    public COUPONEntity updateCoupon(int couponNo, COUPONEntity coupon) {
        coupon.setCouponNo(couponNo); // PK 설정
        return couponRepository.save(coupon);
    }
}
*/