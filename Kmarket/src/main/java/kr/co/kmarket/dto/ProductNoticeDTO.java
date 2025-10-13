package kr.co.kmarket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductNoticeDTO {

    private int notice_no;          // 상품정보 제공고시 번호
    private int prod_number;        // 상품번호
    private String prod_stat;       // 상품 상태
    private String prod_tax;        // 부가세 면세 여부
    private String prod_receipt;    // 영수증 발행
    private String prod_business;   // 사업자 구분
    private String prod_brand;      // 브랜드
    private String prod_origin;     // 원산지
    private String prod_mfg;        // 제조사/수입국
    private String prod_country;    // 제조국
    private String warning;         // 취급시 주의사항
    private String mfg_date;        // 제조연월
    private String warranty;        // 품질 보증 기준
    private String as_manager;      // A/S 책임자
    private String as_phone;        // 소비자상담 전화번호

}
