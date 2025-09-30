package kr.co.kmarket.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/order")
public class OrderController {

    @GetMapping("/list")
    public String list(){
        return "admin/order/list";
    }

    @GetMapping("/delivery")
    public String delivery(){
        return "admin/order/delivery";
    }
}
