package kr.co.kmarket.mapper;

import kr.co.kmarket.dto.CouponDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MyCouponMapper {

    // ✅ 특정 사용자 쿠폰 목록 조회
    List<CouponDTO> selectCouponsByUser(@Param("custNumber") int custNumber);

    // ✅ 사용자 쿠폰 총 개수 조회 (페이지네이션용)
    int countCouponsByUser(@Param("custNumber") int custNumber);

    int updateCouponStatus(@Param("couponNo") int couponNo, @Param("custNumber") int custNumber);
}
