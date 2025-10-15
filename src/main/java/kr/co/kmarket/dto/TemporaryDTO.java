package kr.co.kmarket.dto;

<<<<<<<< HEAD:src/main/java/kr/co/kmarket/dto/QnaDTO.java
import com.fasterxml.jackson.annotation.JsonFormat;
========
>>>>>>>> b54f8b79bc4fa45e775c31cedc2d89e22e322691:src/main/java/kr/co/kmarket/dto/TemporaryDTO.java
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
<<<<<<<< HEAD:src/main/java/kr/co/kmarket/dto/QnaDTO.java
========
import org.hibernate.annotations.CreationTimestamp;
>>>>>>>> b54f8b79bc4fa45e775c31cedc2d89e22e322691:src/main/java/kr/co/kmarket/dto/TemporaryDTO.java

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
<<<<<<<< HEAD:src/main/java/kr/co/kmarket/dto/QnaDTO.java
public class QnaDTO {
========
public class TemporaryDTO { //임시 고객센터 문의글 dto
>>>>>>>> b54f8b79bc4fa45e775c31cedc2d89e22e322691:src/main/java/kr/co/kmarket/dto/TemporaryDTO.java
    private int id;
    private String type1;
    private String type2;
    private String title;
    private String quest;
    private String answer;
    private String user_id;
    private String admin_id;

<<<<<<<< HEAD:src/main/java/kr/co/kmarket/dto/QnaDTO.java
    @JsonFormat(pattern = "yy.MM.dd", timezone = "Asia/Seoul")
    private LocalDateTime reg_date;
    private String status;
}

========
    @CreationTimestamp
    private LocalDateTime reg_date;
    private String status;
}
>>>>>>>> b54f8b79bc4fa45e775c31cedc2d89e22e322691:src/main/java/kr/co/kmarket/dto/TemporaryDTO.java
