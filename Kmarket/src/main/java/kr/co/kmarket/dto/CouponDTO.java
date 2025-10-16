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
    private int couponNo;
    private int couponType;
    private String couponTypename; // ✅ CASE 결과값 매핑용
    private String couponName;
    private String benefit;
    private String benefit2;
    private String usePeriod;
    private String issuer;
    private Date issueDate;
    private int useCount;
    private String status;
    private String manage;
    private int sayoung;
    private int bargup;

    private String couponImage; // 쿠폰 이미지 경로
}
