package kr.co.kmarket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CouponDTO {

    private int couponNo;           // 쿠폰 번호
    private int couponType;         // 쿠폰 종류
    private String couponTypename;  // ✅ CASE 결과값 매핑용
    private String couponName;      // 쿠폰 이름
    private String benefit;         // 혜택
    private String benefit2;        // 적용 조건
    private String usePeriod;       // 사용기간
    private String issuer;          // 발급자
    private Date issueDate;         // 유효기간
    private int useCount;           // 사용수
    private String status;          // 쿠폰 상태
    private String manage;          // 관리
    private int sayoung;            // 사용수
    private int bargup;             // 발급수

    private int cust_number;        // 고객 고유 번호 (MEMBER 테이블)

    private String couponImage;     // 쿠폰 이미지 경로
}
