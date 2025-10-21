package kr.co.kmarket.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminOrderDetailDTO {

    // ORDER 테이블 필드
    private String order_number;    // 주문 번호 (PK)
    private int cust_number;        // 주문자 회원 번호 (FK)
    private int payment;            // 결제 수단
    private int stat;               // 주문 상태
    private int piece;              // 총 상품 갯수
    private int finalPrice;         // ⬅️ ORDER.PRICE (최종 결제 금액)
    private int usePoint;           // 사용 포인트
    private Date oDate;             // 주문 일자

    // ORDER 테이블 - 수령인 정보 (ORDER.NAME, ORDER.HP, ...)
    private String recipName;       // ⬅️ ORDER.NAME (수령인 이름)
    private String recipHp;         // ⬅️ ORDER.HP (수령인 연락처)
    private String recipZip;
    private String recipAddr1;
    private String recipAddr2;
    private String req;             // 배송 요청사항

    // MEMBER 테이블 - 주문자 정보
    private String custId;          // 주문자 ID
    private String ordName;         // ⬅️ MEMBER.NAME (주문자 이름)
    private String ordHp;           // ⬅️ MEMBER.HP (주문자 연락처)

    // 주문 상품 정보 목록 (List<AdminOrderDetailDTO> 내부에 List<ProductInfoDTO>를 사용하거나,
    // 아래 필드를 반복 조회 결과에 매핑)
    // MyBatis List 조회 시 사용 (Mapper에서 ProductDetailResultMap으로 매핑)
    private int prod_number;        // 상품 번호
    private int itemPiece;          // ⬅️ ORDERDETAIL.PIECE (해당 상품의 구매 수량)
    private int price;              // ⬅️ ORDERDETAIL.PRICE (단일 상품의 원래 가격)
    private int salePrice;          // ⬅️ ORDERDETAIL.SALEPRICE (단일 상품의 할인가)
    private int discount;           // ⬅️ ORDERDETAIL.DISCOUNT (할인율)

    // PRODUCT 테이블 필드
    private String prod_name;
    private String manufacture;
    private String img1;            // ⬅️ IMG_1 (상품 이미지)
    private int deliveryFee;       // ⬅️ DELIVERY_FEE (배송비)

    // 뷰 출력용 필드 (선택 사항, Controller/JS에서 포맷팅 가능)
    private String oDateString;
}