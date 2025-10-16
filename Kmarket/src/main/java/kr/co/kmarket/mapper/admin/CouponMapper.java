package kr.co.kmarket.mapper.admin;

import kr.co.kmarket.dto.CouponDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CouponMapper {
    int insertCoupon(CouponDTO couponDTO);
    List<CouponDTO> selectCoupons();

    //검색 기능
    List<CouponDTO> selectSearchCoupons(@Param("type") String type,
                                        @Param("keyword") String keyword);

    //페이지네이션
    List<CouponDTO> selectCouponsPage(@Param("type") String type,
                                      @Param("keyword") String keyword,
                                      @Param("offset") int offset,
                                      @Param("size") int size);

    int countCoupons(@Param("type") String type,
                     @Param("keyword") String keyword);

}


