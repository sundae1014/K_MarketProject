package kr.co.kmarket.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QnaDTO {

    private String id;
    private String type1; // 문의자 타입
    private String type2; // 문의 유형
    private String title; // 제목
    private String quest; // 문의내용
    private String user_id; // custId
    private Date reg_date; // 작성시간
    private String status; // 문의상태 기본:검토중
}
