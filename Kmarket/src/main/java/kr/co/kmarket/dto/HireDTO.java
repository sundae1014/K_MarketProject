package kr.co.kmarket.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HireDTO {

    private int hire_no;
    private String department;
    private String experience;
    private String hire_type;
    private String title;
    private String author;
    private String status;
    private String create_date;

    private String recruit_period_start;
    private String recruit_period_end;
    private String content;
}
