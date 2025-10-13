package kr.co.kmarket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductReviewDTO {

    private int cust_number;        // 리뷰 작성자
    private int review_no;          // 리뷰 번호
    private LocalDateTime r_date;   // 리뷰 작성 시간
    private String answer;          // 답변
    private LocalDateTime a_date;   // 답변 일자
    private int rating;             // 리뷰 별점
    private int prod_number;        // 상품 번호
    private String content;         // 리뷰 본문 내용
    private String r_img;           // 리뷰 첨부 이미지

}
