package kr.co.kmarket.mapper.admin;

import kr.co.kmarket.dto.CouponDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface CouponMapper {

    int insertCoupon(CouponDTO couponDTO);

    List<CouponDTO> selectCouponsByPage(@Param("offset") int offset,
                                        @Param("size") int size);

    int countCoupons();

    List<CouponDTO> searchCouponsByPage(@Param("type") String type,
                                        @Param("keyword") String keyword,
                                        @Param("offset") int offset,
                                        @Param("size") int size);

    int countSearchCoupons(@Param("type") String type,
                           @Param("keyword") String keyword);

    List<CouponDTO> selectCoupons();

    int countIssuedCoupon(@Param("couponNo") int couponNo, @Param("custNumber") int custNumber);

    int insertCouponHistory(@Param("couponNo") int couponNo, @Param("custNumber") int custNumber);

    int updateCouponIssueCount(@Param("couponNo") int couponNo);

    CouponDTO selectCouponByNo(int couponNo);

    void insertCouponHistoryWithExpire(@Param("couponNo") int couponNo,
                                       @Param("custNumber") int custNumber,
                                       @Param("issueDate") Date issueDate,
                                       @Param("expireDate") Date expireDate);

    int stopCoupon(int couponNo);
    List<CouponDTO> selectIssuedCoupons();
    List<CouponDTO> selectAvailableCoupons(int custNumber);
    int updateCouponStatusToStop(int couponNo);

    List<CouponDTO> selectIssuedCouponsByPage(@Param("offset") int offset, @Param("size") int size);
    int countIssuedCoupons();
}
