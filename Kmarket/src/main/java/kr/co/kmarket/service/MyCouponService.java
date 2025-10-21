package kr.co.kmarket.service;

import kr.co.kmarket.dto.CouponDTO;
import kr.co.kmarket.mapper.MyCouponMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyCouponService {

    private final MyCouponMapper myCouponMapper;

    // ✅ 특정 고객의 쿠폰 목록 조회
    public List<CouponDTO> getUserCoupons(int custNumber) {
        return myCouponMapper.selectCouponsByUser(custNumber);
    }

    // ✅ 쿠폰 개수 조회 (페이지네이션 용)
    public int countUserCoupons(int custNumber) {
        return myCouponMapper.countCouponsByUser(custNumber);
    }

    public List<CouponDTO> selectCouponsByUser(int custNumber) {
        return myCouponMapper.selectCouponsByUser(custNumber);
    }
}
