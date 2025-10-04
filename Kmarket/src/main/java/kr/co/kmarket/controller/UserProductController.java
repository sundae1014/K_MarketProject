package kr.co.kmarket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product")
public class UserProductController {

    @GetMapping("/list")
    public String list(){
        return "product/prodList";
    }

    @GetMapping("/view")
    public String view(){
        return "product/prodView";
    }

    @GetMapping("/cart")
    public String cart(){
        return "product/prodCart";
    }

    @GetMapping("/order")
    public String order(){
        return "product/prodOrder";
    }

    @GetMapping("/complete")
    public String complete(){
        return "product/prodComplete";
    }

    @GetMapping("/search")
    public String search(){
        return "product/prodSearch";
    }
}
