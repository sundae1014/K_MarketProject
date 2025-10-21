package kr.co.kmarket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductMangementDTO {
    private int prod_number;
    private int soldOut;
    private int restock;
    private int stock;
}
