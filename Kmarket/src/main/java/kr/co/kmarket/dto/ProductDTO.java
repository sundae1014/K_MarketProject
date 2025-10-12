package kr.co.kmarket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {

    private int prodNumber;         // 상품 번호
    private int cateCd;             // 카테고리 코드
    private String prodName;        // 상품 이름
    private String description;     // 상품 설명
    private String manufacture;     // 제조사
    private BigDecimal price;       // 가격(정상가)
    private int discount;           // 할인율
    private BigDecimal salePrice;   // 할인가
    private int deliveryFee;        // 배송비
    private String img1;            // 상품 메인 이미지1 (대표)
    private String img2;            // 상품 메인 이미지2
    private String img3;            // 상품 메인 이미지3
    private String detailImg;       // 상품 상세 이미지
    private String prodStat;        // 상품 상태
    private String prodTax;         // 부가세 여부
    private String prodBusiness;    // 사업자 구분
    private String prodNation;      // 원산지
    private int hit;                // 조회수
    private BigDecimal proScore;        // 평점
    private LocalDateTime regDate;  // 등록일

}
