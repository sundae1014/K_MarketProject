package kr.co.kmarket.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {

    private int cust_number;
    private int ordernumber;
    private String o_date;
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
}
