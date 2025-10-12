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
    public String list(@ModelAttribute SearchDTO search,
                       @RequestParam(defaultValue = "recent") String sort,
                       Model model) {
        List<ProductDTO> products = productService.searchProducts(search, sort);
        model.addAttribute("products", products);
        model.addAttribute("sort", sort);
        model.addAttribute("search", search);
        return "product/prodList";
    }

    @GetMapping("/view")
    public String view(@RequestParam("prodNo") int prodNo, Model model) {
        ProductDTO product = productService.selectProductByNo(prodNo);
        ProductNoticeDTO notice = productService.selectProductNoticeByNo(prodNo);
        List<ProductReviewDTO> reviews = productService.selectProductReviews(prodNo);

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

    // 기본 검색(종합 검색)
    @GetMapping("/search")
    public String search(@ModelAttribute SearchDTO search,
                         @RequestParam(defaultValue = "recent") String sort,
                         Model model) {
        List<ProductDTO> products = productService.searchProducts(search, sort);
        model.addAttribute("products", products);
        model.addAttribute("sort", sort);
        model.addAttribute("search", search);            // ★ 검색어 표시용
        model.addAttribute("total", products.size());    // ★ “총 n건”
        return "product/prodSearch";
    }

    // 키워드 검색
    @GetMapping("/searchKeyword")
    public String searchByKeyword(String keyword, Model model) {
        List<ProductDTO> products = productService.selectProductsByKeyword(keyword);
        model.addAttribute("products", products);
        model.addAttribute("keyword", keyword);
        return "product/prodSearch";
    }

    // 카테고리 + 키워드 검색
    @GetMapping("/searchCategory")
    public String searchByCategory(String keyword, int cate_cd, Model model) {
        List<ProductDTO> products = productService.selectProductsByCategory(keyword, cate_cd);
        model.addAttribute("products", products);
        model.addAttribute("keyword", keyword);
        model.addAttribute("cate_cd", cate_cd);
        return "product/prodSearch";
    }
}
