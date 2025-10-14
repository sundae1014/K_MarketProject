package kr.co.kmarket.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QnaDTO {
    private int id;
    private String type1;
    private String type2;
    private String title;
    private String quest;
    private String answer;
    private String user_id;
    private String admin_id;

    @JsonFormat(pattern = "yy.MM.dd", timezone = "Asia/Seoul")
    private LocalDateTime reg_date;
    private String status;
}

