package kr.co.kmarket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TemporaryDTO { //임시 고객센터 문의글 dto
    private int id;
    private String type1;
    private String type2;
    private String title;
    private String quest;
    private String answer;
    private String user_id;
    private String admin_id;

    @CreationTimestamp
    private LocalDateTime reg_date;
    private String status;
}
