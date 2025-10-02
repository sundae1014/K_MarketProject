package kr.co.cupang.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProdSearchController {

    @GetMapping(("/prodSearch"))
    public String prodSearch() {
        return "prodSearch";
    }
}
