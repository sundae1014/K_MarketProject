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
    private int orderNumber;
    private Date oDate;
    private String dateString;
    private int stat;
    private String name;
    private String hp;
    private String req;
    private String addr;
    private String addr2;
    private int payment;
    private String etc;
    private String exchange;
    private String exchange_reason;
    private int piece;
    private int price;
    private String priceString;
    private String img1;
    private String manufacture;
    private String prod_name;
    private String encodedImg1;
    private int discount;
    private int salePrice;
}
