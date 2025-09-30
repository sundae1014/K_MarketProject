package kr.co.cupang.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProdViewController {

    @GetMapping(("/prodView"))
    public String prodView() {
        return "prodView";
    }
}
