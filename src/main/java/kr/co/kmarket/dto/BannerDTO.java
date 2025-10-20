package kr.co.kmarket.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

@Data
public class BannerDTO {
    private int banner_no;
    private String banner_name;
    private String img;
    private String link;
    private String location;
    private Integer banner_order;
    private Integer banner_status;
    private String banner_size;
    private String background_color;
    private String start_time;
    private String end_time;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate start_date;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate end_date;

    private MultipartFile imgFile;


}
