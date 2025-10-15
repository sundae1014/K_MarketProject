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
public class FaqDTO {
    private int id;
    private String type1;
    private String type2;
    private String title;
    private String content;
    private int views;

    @JsonFormat(pattern = "yy.MM.dd", timezone = "Asia/Seoul")
    private LocalDateTime reg_date;
}
