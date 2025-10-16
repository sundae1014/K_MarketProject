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

    // 쿠폰 등록
    public void insertCoupon(CouponDTO dto) {
        mapper.insertCoupon(dto);
    }

    // 쿠폰 목록 조회
    public List<CouponDTO> getCoupons() {
        List<CouponDTO> list = mapper.selectCoupons();
        for (CouponDTO dto : list) {
            dto.setCouponTypename(convertTypeToName(dto.getCouponType())); // 변환 추가
        }
        return list;
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
    public List<CouponDTO> searchCoupons(String type, String keyword) {
        return mapper.selectSearchCoupons(type, keyword);
    }

    // 쿠폰 개수 세기
    public int countCoupons(String type, String keyword) {
        return mapper.countCoupons(type, keyword);
    }

    // 페이지 단위 쿠폰 조회
    public List<CouponDTO> getCouponsPage(String type, String keyword, int offset, int size) {
        return mapper.selectCouponsPage(type, keyword, offset, size);
    }
}