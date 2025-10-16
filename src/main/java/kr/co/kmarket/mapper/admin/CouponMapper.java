package kr.co.kmarket.mapper.admin;

import kr.co.kmarket.dto.CouponDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CouponMapper {
    int insertCoupon(CouponDTO couponDTO);
    List<CouponDTO> selectCoupons();

    List<CouponDTO> selectSearchCoupons(@Param("type") String type,
                                        @Param("keyword") String keyword);
}


