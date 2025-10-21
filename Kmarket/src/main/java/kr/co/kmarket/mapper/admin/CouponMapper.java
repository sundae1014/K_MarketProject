package kr.co.kmarket.mapper.admin;

import kr.co.kmarket.dto.CouponDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CouponMapper {

    // ✅ 쿠폰 등록
    int insertCoupon(CouponDTO couponDTO);

    // ✅ 목록 + 검색 + 페이징
    List<CouponDTO> selectCouponsPage(@Param("type") String type,
                                      @Param("keyword") String keyword,
                                      @Param("offset") int offset,
                                      @Param("size") int size);

    // ✅ 전체 개수 + 검색 카운트
    int countCoupons(@Param("type") String type,
                     @Param("keyword") String keyword);

}
