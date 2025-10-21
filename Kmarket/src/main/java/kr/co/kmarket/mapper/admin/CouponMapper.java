package kr.co.kmarket.mapper.admin;

import kr.co.kmarket.dto.CouponDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface CouponMapper {

    // ✅ 쿠폰 등록
    int insertCoupon(CouponDTO couponDTO);

    // ✅ 전체 목록 조회 (검색 없음 + 페이징)
    List<CouponDTO> selectCouponsByPage(@Param("offset") int offset,
                                        @Param("size") int size);

    // ✅ 전체 개수 조회 (검색 없음)
    int countCoupons();

    // ✅ 검색 + 페이징 목록 조회
    List<CouponDTO> searchCouponsByPage(@Param("type") String type,
                                        @Param("keyword") String keyword,
                                        @Param("offset") int offset,
                                        @Param("size") int size);

    // ✅ 검색된 전체 개수 조회
    int countSearchCoupons(@Param("type") String type,
                           @Param("keyword") String keyword);

    // ✅ 전체 조회 (없애도 됨)
    List<CouponDTO> selectCoupons();

    // 유저가 이미 쿠폰 발급받았는지 확인
    int countIssuedCoupon(@Param("couponNo") int couponNo, @Param("custNumber") int custNumber);

    // 쿠폰 발급 INSERT
    int insertCouponHistory(@Param("couponNo") int couponNo, @Param("custNumber") int custNumber);

    // 쿠폰 발급 수 증가
    int updateCouponIssueCount(@Param("couponNo") int couponNo);

    // ✅ 쿠폰 번호로 쿠폰 1개 조회
    CouponDTO selectCouponByNo(int couponNo);

    // ✅ 유효기간 포함 발급 기록 저장
    void insertCouponHistoryWithExpire(@Param("couponNo") int couponNo,
                                       @Param("custNumber") int custNumber,
                                       @Param("issueDate") Date issueDate,
                                       @Param("expireDate") Date expireDate);
}
