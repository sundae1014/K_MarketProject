package kr.co.kmarket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDTO {

    private int cart_number;
    private int cust_number;
    private int prod_number;
    private Date c_date;
    private int quantity;
    private double product_amount;
    private double discount_amount;
    private double delivery_fee;
    private double total_order_amount;
    private int reward_points;
    private String opt_name;
    private String is_selected;
    private String is_ordered;

    // Product join용 (출력용)
    private String prod_name;
    private int price;
    private int discount;
    private String img_1;
}
