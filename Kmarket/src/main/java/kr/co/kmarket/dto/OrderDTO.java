// kr.co.kmarket.dto.OrderDTO.java
package kr.co.kmarket.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {

    private int cust_number;
    private String order_number;    // 문자열 변경
    private Date oDate;
    private String dateString;      // 날짜 포맷팅 문자열
    private int stat;
    private String name;
    private String hp;
    private String req;
    private String addr;
    private String addr2;
    private int payment;
    private String etc;
    private int exchange;
    private String exchange_reason;

    private int piece;
    private int price;
    private String priceString;       // ⬅️ 최종 결제 금액 포맷 문자열
    private String img1;
    private String manufacture;
    private String prod_name;
    private int prod_number;
    private String encodedImg1;
    private int discount;
    private int salePrice;

    private String salePriceString;   // ⬅️ [추가] 판매가 포맷 문자열
    private String discountString;    // ⬅️ [추가] 할인액 포맷 문자열


}