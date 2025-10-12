package kr.co.kmarket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchDTO {

    // 기본 검색
    private String keyword;
    private String category;
    private Integer cate_cd;

    // 가격 범위 검색
    private Integer minPrice;
    private Integer maxPrice;

    // 정렬 조건
    private String sort;

    // 체크 검색 옵션
    private boolean searchName = true;   // 상품명 (기본값)
    private boolean searchDesc = false;  // 상품설명
    private boolean searchPrice = false; // 가격

}
