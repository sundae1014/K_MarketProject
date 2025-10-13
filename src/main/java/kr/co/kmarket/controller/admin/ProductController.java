package kr.co.kmarket.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin/product")
@Controller
public class ProductController {

    @GetMapping("/list")
    public String list() {return "admin/product/productList";}

    @GetMapping("/register")
    public String register() {return "admin/product/product_regist";}
}
