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

    private int noticeNo;
    private int prodNumber;
    private String prodStat;
    private String prodTax;
    private String prodReceipt;
    private String prodBusiness;
    private String prodBrand;
    private String prodOrigin;
    private String prodMfg;
    private String prodCountry;
    private String warning;
    private String mfgDate;
    private String warranty;
    private String asManager;
    private String asPhone;

}
