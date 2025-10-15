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
    private String category;   // 사용자 입력용(선택형)
    private String cate_cd;   // 실제 DB 필드용

    // 가격 범위 검색
    private Integer minPrice;
    private Integer maxPrice;

    // 정렬 조건 (기본값 "recent")
    private String sort = "recent";

    // 체크 검색 옵션
    private boolean searchName = true;   // 상품명 (기본값)
    private boolean searchDesc = false;  // 상품설명
    private boolean searchPrice = false; // 가격

    // 안전하게 널 방지용 Getter 추가
    public String getSort() {
        return (sort == null || sort.isBlank()) ? "recent" : sort;
    }

    public String getCate_cd() {
        return (cate_cd == null || cate_cd.isBlank()) ? "0" : cate_cd;
    }

    // keyword가 공백/NULL일 때, 안전하게 처리 가능
    public String getKeyword() {
        return (keyword == null) ? "" : keyword.trim();
    }

}
