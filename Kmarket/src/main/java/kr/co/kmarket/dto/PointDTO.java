package kr.co.kmarket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PointDTO {

    private int point_id;           // 포인트 고유번호(PK)
    private int cust_number;        // 회원번호(FK)
    private String order_number;    // 주문번호(FK)
    private int prod_number;        // 상품번호(FK)
    private int cart_number;        // 장바구니번호(FK)
    private int point_type;         // 포인트 구분
    private int point_amount;       // 포인트 변동 금액
    private String description;     // 포인트 발생 사유
    private int balance;            // 잔여 포인트
    private LocalDateTime expiry_date;       // 만료일
    private LocalDateTime created_at;        // 발생일

}
