package kr.co.kmarket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageResponseDTO<T> {
    private List<T> dtoList;

    private String cate;
    private int pg;
    private int size;
    private int total;
    private int startNo;
    private int start, end;
    private boolean prev, next;

    private String searchType;
    private String keyword;

    @Builder
    public PageResponseDTO(PageRequestDTO pageRequestDTO, List<T> dtoList, int total){
        this.cate = pageRequestDTO.getCate();
        this.pg = pageRequestDTO.getPg();
        this.size = pageRequestDTO.getSize();
        this.total = total;
        this.dtoList = dtoList;

        this.startNo = total - ((pg - 1) * size);

        int blockSize;
        if (!dtoList.isEmpty() && dtoList.get(0) instanceof VersionDTO) {
            blockSize = 4;
        } else if(!dtoList.isEmpty() && dtoList.get(0) instanceof MemberDTO) {
            blockSize = 5;
        }
        else {
            blockSize = 5;
        }

        this.end = (int) (Math.ceil(this.pg / (double) blockSize)) * blockSize;
        this.start = this.end - (blockSize - 1);

        int last = (int) (Math.ceil(total / (double) size));
        this.end = Math.min(this.end, last);

//        this.prev = this.start > 1;
//        this.next = total > this.end * this.size;

        this.prev = this.start > 1;
        this.next = this.end < last;

        this.searchType = pageRequestDTO.getSearchType();
        this.keyword = pageRequestDTO.getKeyword();

    }
}
