package kr.co.kmarket.dto;


import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoryDTO {
    private int story_no;
    private String title;
    private String author;
    private String content;
    private String img_path;
    private String reg_date;
    private int cate;
    private String subtitle;
}
