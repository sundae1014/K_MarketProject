package kr.co.kmarket.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDTO {

    private int cust_number;
    private String name;
    private String custid;
    private String pw;
    private String hp;
    private String email;
    private int gender;


    private int point;
    private int auth;

    @Builder.Default
    private int useterm = 1;

    @Builder.Default
    private int stat = 1;

    private String company_name;
    private String business_number;
    private String ecommerce_number;
    private String fax_number;
    private String zip;
    private String addr1;
    private String addr2;
    private String birth;
    private String operation;

    private String Date;

}
