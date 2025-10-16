package kr.co.kmarket.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    private int prod_number;        // 상품 번호
    private String cate_cd;         // 카테고리 코드
    private String prod_name;       // 상품 이름
    private String description;     // 상품 설명
    private String manufacture;     // 제조사
    private int price;              // 가격(정상가)
    private int discount;           // 할인율
    private int salePrice;          // 할인가
    private int delivery_fee;       // 배송비
    private String img_1;           // 상품 메인 이미지1 (대표)
    private String img_2;           // 상품 메인 이미지2
    private String img_3;           // 상품 메인 이미지3
    private String detail_img;      // 상품 상세 이미지
    private String prod_stat;       // 상품 상태
    private String prod_tax;        // 부가세 여부
    private String prod_business;   // 사업자 구분
    private String prod_nation;     // 원산지
    private int hit;                // 조회수
    private BigDecimal pro_score;   // 평점

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reg_date; // 등록일

}
