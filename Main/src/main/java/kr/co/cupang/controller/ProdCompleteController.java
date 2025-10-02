package kr.co.cupang.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProdCompleteController {

    @GetMapping(("/prodComplete"))
    public String prodComplete() {
        return "prodComplete";
    }
}
