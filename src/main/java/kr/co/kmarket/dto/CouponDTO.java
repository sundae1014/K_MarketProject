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

    private int coupon_no;
    private int coupon_type;
    private String coupon_name;
    private String benefit;
    private String benefit2;
    private String use_period;
    private String issuer;
    private Date issue_date;
    private int use_count;
    private String status;
    private String manage;
    private int sayoung;
    private int bargup;
    private Date issue;
}
