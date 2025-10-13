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

    private int custNumber;
    private int reviewNo;
    private LocalDateTime rDate;
    private String answer;
    private LocalDateTime aDate;
    private int rating;
    private int prodNumber;
    private String content;
    private String imgPath;

}
