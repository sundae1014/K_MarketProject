package kr.co.kmarket.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin/shop")
@Controller
public class ShopController {

    @GetMapping("/list")
    public String list() {return "admin/shop/admin_shopList";}

    @GetMapping("/sales")
    public String sales() {return "admin/shop/admin_salesStatus";}
}
