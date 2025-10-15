package kr.co.kmarket.controller;

import kr.co.kmarket.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("hitProducts", productService.selectHitProducts());
        model.addAttribute("recommendProducts", productService.selectRecommendProducts());
        model.addAttribute("newProducts", productService.selectNewProducts());
        model.addAttribute("popularProducts", productService.selectPopularProducts());
        model.addAttribute("discountProducts", productService.selectDiscountProducts());
        model.addAttribute("bestProducts", productService.selectBestProducts());
        return "index";
    }
}
