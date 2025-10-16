package kr.co.kmarket.controller;

import kr.co.kmarket.dto.ProductDTO;
import kr.co.kmarket.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/best-products")
    public List<ProductDTO> getBestProducts() {
        return productService.selectBestProducts();
    }
}
