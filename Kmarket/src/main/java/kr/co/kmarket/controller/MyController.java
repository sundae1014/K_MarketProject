package kr.co.kmarket.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/my")
public class MyController {

    @GetMapping("/coupon")
    public String coupon(){
        return "my/coupon";
    }

    @GetMapping("/home")
    public String home(){
        return "my/home";
    }

    @GetMapping("/option")
    public String option(){
        return "my/option";
    }

    @GetMapping("/order")
    public String order(){


        return "my/order";
    }

    @GetMapping("/point")
    public String point(){
        return "my/point";
    }

    @GetMapping("/qna")
    public String qna(){
        return "my/qna";
    }

    @GetMapping("/review")
    public String review(){
        return "my/review";
    }

}
