package kr.co.kmarket.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BasicDTO {
    private int id;
    private String title;
    private String sub_title;
    private String header_logo_main;
    private String header_logo_intro;
    private String header_logo_admin;
    private String footer_logo_main;
    private String footer_logo_intro;
    private String footer_logo_admin;
    private String favicon;
    private String trade_name;
    private String ceo;
    private String business_num;
    private String communication_num;
    private String addr1;
    private String addr2;
    private String hp;
    private String work_time;
    private String email;
    private String transaction;
    private String copyright;

    // 파일 업로드용
    private MultipartFile header_logoFile1;
    private MultipartFile header_logoFile2;
    private MultipartFile header_logoFile3;
    private MultipartFile footer_logoFile1;
    private MultipartFile footer_logoFile2;
    private MultipartFile footer_logoFile3;
    private MultipartFile favicon_File;

}
