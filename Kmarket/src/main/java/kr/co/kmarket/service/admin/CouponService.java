package kr.co.kmarket.service.admin;

import jakarta.transaction.Transactional;
import kr.co.kmarket.dto.CouponDTO;
import kr.co.kmarket.mapper.admin.CouponMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponMapper mapper;

    // ✅ 쿠폰 등록
    public void insertCoupon(CouponDTO dto) {
        mapper.insertCoupon(dto);
    }


    // ✅ 타입 숫자를 문자로 변환

    // 쿠폰 목록 조회
    public List<CouponDTO> getCoupons() {
        List<CouponDTO> list = mapper.selectCoupons();
        for (CouponDTO dto : list) {
            dto.setCouponTypename(convertTypeToName(dto.getCouponType())); // 기존 타입명 변환
            dto.setCouponImage(convertTypeToImage(dto.getCouponType()));    // ✅ 이미지 매핑 추가
        }
        return list;
    }

    private String convertTypeToImage(int type) {
        return switch (type) {
            case 1 -> "/images/product/coupon-product.png";
            case 2 -> "/images/product/coupon-price.png";
            case 3 -> "/images/product/coupon-delivery.png";
            case 4 -> "/images/product/coupon-vip.png";
            case 5 -> "/images/product/coupon-first.png";
            default -> "/images/product/coupon-default.png";
        };
    }

    // 숫자 → 문자열로 변환
    private String convertTypeToName(int type) {
        return switch (type) {
            case 1 -> "개별상품 할인";
            case 2 -> "주문금액 할인";
            case 3 -> "배송비 무료";
            case 4 -> "회원등급 전용";
            case 5 -> "첫구매 전용";
            default -> "기타";
        };
    }

    // ✅ 페이지네이션 목록 조회
    public List<CouponDTO> getCouponsByPage(int page, int size) {
        int offset = (page - 1) * size;
        List<CouponDTO> list = mapper.selectCouponsByPage(offset, size);
        list.forEach(dto -> dto.setCouponTypename(convertTypeToName(dto.getCouponType())));
        return list;
    }

    // ✅ 전체 페이지 수
    public int getTotalPages(int size) {
        int totalCount = mapper.countCoupons();
        return (int) Math.ceil((double) totalCount / size);
    }

    // ✅ 검색 + 페이지네이션
    public List<CouponDTO> searchCoupons(String type, String keyword, int page, int size) {
        int offset = (page - 1) * size;
        List<CouponDTO> list = mapper.searchCouponsByPage(type, keyword, offset, size);
        list.forEach(dto -> dto.setCouponTypename(convertTypeToName(dto.getCouponType())));
        return list;
    }

    // ✅ 검색 결과 페이지 수
    public int getSearchTotalPages(String type, String keyword, int size) {
        int totalCount = mapper.countSearchCoupons(type, keyword);
        return (int) Math.ceil((double) totalCount / size);
    }



    @Transactional
    public int issueCoupon(int couponNo, int custNumber) {
        // 1. 이미 발급받은 쿠폰인지 체크
        int count = mapper.countIssuedCoupon(couponNo, custNumber);
        if (count > 0) {
            return 0; // 이미 발급됨
        }

        // ✅ 2. 쿠폰 기본 정보 조회
        CouponDTO coupon = mapper.selectCouponByNo(couponNo);
        if (coupon == null) {
            throw new IllegalArgumentException("쿠폰이 존재하지 않습니다: " + couponNo);
        }

        // ✅ 3. 발급일 + 유효기간 계산 (문자열 → 숫자 변환)
        LocalDate issueDate = LocalDate.now();
        String period = coupon.getUsePeriod();

        LocalDate expireDate;

        if (period.contains("~")) {
            // 예: "2025-10-22 ~ 2025-10-28"
            String[] dates = period.split("~");
            expireDate = LocalDate.parse(dates[1].trim()); // 끝 날짜만 저장
        } else {
            // 숫자 입력된 경우 대비 (예: "7")
            long usePeriod = Long.parseLong(period);
            expireDate = issueDate.plusDays(usePeriod);
        }

// ✅ 4. 발급 기록 저장
        mapper.insertCouponHistoryWithExpire(
                couponNo,
                custNumber,
                Date.valueOf(issueDate),
                Date.valueOf(expireDate)
        );
        // ✅ 5. 발급 수 증가
        mapper.updateCouponIssueCount(couponNo);

        return 1;
    }
    public List<CouponDTO> getIssuedCoupons() {
        List<CouponDTO> list = mapper.selectIssuedCoupons();
        for (CouponDTO dto : list) {
            dto.setCouponTypename(convertTypeToName(dto.getCouponType()));
        }
        return list;
    }
    public List<CouponDTO> getAvailableCoupons(int custNumber) {
        List<CouponDTO> list = mapper.selectAvailableCoupons(custNumber);

        // 쿠폰 타입명 변환 (프론트 출력용)
        list.forEach(dto -> dto.setCouponTypename(convertTypeToName(dto.getCouponType())));

        return list;
    }
    public int stopCoupon(int couponNo) {
        return mapper.updateCouponStatusToStop(couponNo);
    }

    public CouponDTO getCouponDetail(int couponNo) {
        CouponDTO dto = mapper.selectCouponByNo(couponNo);
        if (dto != null) {
            dto.setCouponTypename(convertTypeToName(dto.getCouponType())); // 타입명 변환
        }
        return dto;
    }

    public List<CouponDTO> getIssuedCouponsByPage(int page, int size) {
        int offset = (page - 1) * size;
        return mapper.selectIssuedCouponsByPage(offset, size);
    }

    public int getIssuedTotalPages(int size) {
        int totalCount = mapper.countIssuedCoupons();
        return (int) Math.ceil((double) totalCount / size);
    }
}
