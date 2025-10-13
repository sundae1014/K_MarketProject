package kr.co.kmarket.controller;

import kr.co.kmarket.dto.ProductDTO;
import kr.co.kmarket.dto.ProductNoticeDTO;
import kr.co.kmarket.dto.ProductReviewDTO;
import kr.co.kmarket.dto.SearchDTO;
import kr.co.kmarket.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/product")
public class UserProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/list")
    public String list(@ModelAttribute SearchDTO searchDTO,
                       @RequestParam(defaultValue = "recent") String sort,
                       Model model) {

        List<ProductDTO> products = productService.selectProducts(searchDTO, sort);
        int totalCount = productService.countProducts(searchDTO);

        model.addAttribute("products", products);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("search", searchDTO);
        model.addAttribute("sort", sort);

        return "product/prodList";
    }

    @GetMapping("/view")
    public String view(@RequestParam("prod_number") int prod_number, Model model) {
        ProductDTO product = productService.selectProductByNo(prod_number);
        ProductNoticeDTO notice = productService.selectProductNoticeByNo(prod_number);
        List<ProductReviewDTO> reviews = productService.selectProductReviews(prod_number);

        model.addAttribute("product", product);
        model.addAttribute("notice", notice);
        model.addAttribute("reviews", reviews);
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
}
