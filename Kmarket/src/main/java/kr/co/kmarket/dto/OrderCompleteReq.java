package kr.co.kmarket.dto;

import lombok.Data;

@Data
public class OrderCompleteReq {

    private int cust_number;
    private int usePoint;
    private int totalPrice;

}
