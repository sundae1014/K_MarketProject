package kr.co.kmarket.service.admin;

import kr.co.kmarket.dto.CouponDTO;
import kr.co.kmarket.mapper.admin.CouponMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponMapper mapper;

    public void insertCoupon(CouponDTO dto) {
        mapper.insertCoupon(dto);
    }

    public List<CouponDTO> getCoupons() {
        return mapper.selectCoupons();
    }
}
