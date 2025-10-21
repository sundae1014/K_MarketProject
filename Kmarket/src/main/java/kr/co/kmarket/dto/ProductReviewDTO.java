package kr.co.kmarket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

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
    private String r_img1;          // 리뷰 첨부 이미지1
    private String r_img2;          // 리뷰 첨부 이미지2
    private String r_img3;          // 리뷰 첨부 이미지3

    private String custid;          // 리뷰 작성자 아이디

    // 파일 업로드를 위한 임시 필드 (DB에 저장 안 함)
    private List<MultipartFile> images;

    private String order_number;

    private String prod_name;

    private String user_id;
}
