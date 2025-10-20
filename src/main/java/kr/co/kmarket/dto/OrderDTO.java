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

    // 회원 정보 조회
    private int cust_number;        // 회원 정보(FK)
    private String order_number;    // 주문 번호(PK)

    // 배송지 정보 조회
    private String name;            // 받는 사람 이름
    private String hp;              // 받는 사람 전화번호
    private String zip;             // 우편번호
    private String addr;            // 기본 주소
    private String addr2;           // 상세 주소
    private String req;             // 배송 요청사항

    // 결제/포인트 관련
    private int payment;            // 결제 수단
    private int piece;              // 상품 갯수
    private int price;              // 총 결제 가격
    private String priceString;     // ⬅️ 최종 결제 금액 포맷 문자열
    private int usePoint;           // 사용 포인트

    // 주문/반품 관련
    private Date oDate;             // 주문 일자
    private String dateString;      // 날짜 포맷팅 문자열
    private int stat;               // 주문 상태
    private String etc;             // 기타 정보
    private int exchange;           // 반품 여부
    private String exchange_reason; // 반품 사유

    private String img1;
    private String manufacture;
    private String prod_name;
    private int prod_number;
    private String encodedImg1;
    private int discount;
    private String discountString;    // ⬅️ [추가] 할인액 포맷 문자열
    private int salePrice;
    private String salePriceString;   // ⬅️ [추가] 판매가 포맷 문자열

}