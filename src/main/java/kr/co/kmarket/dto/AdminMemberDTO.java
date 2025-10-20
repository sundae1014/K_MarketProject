package kr.co.kmarket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminMemberDTO {
    private String custId;
    private String name;
    private String gender;
    private String rating;
    private String operation;
    private String email;
    private String hp;
    private String zip;
    private String addr1;
    private String addr2;
    private String Date;
    private String updated_at;
    private String etc;
}
