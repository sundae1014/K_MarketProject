package kr.co.kmarket.controller;

import kr.co.kmarket.dto.*;
import kr.co.kmarket.enums.CategoryEnum;
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

        // 카테고리 이름 처리
        String category1Name = null;
        String category2Name = null;

        if (searchDTO.getCate_cd() != null && !searchDTO.getCate_cd().isEmpty()) {
            String cateCd = searchDTO.getCate_cd();

            // cateCd 길이가 3보다 짧으면 substring 금지
            if (cateCd.length() >= 3) {
                String upperCode = cateCd.substring(0, 3);
                category1Name = switch (upperCode) {
                    case "C01" -> "패션의류/잡화";
                    case "C02" -> "뷰티";
                    case "C03" -> "출산/유아동";
                    case "C04" -> "식품";
                    case "C05" -> "주방용품";
                    case "C06" -> "생활용품";
                    case "C07" -> "홈인테리어";
                    case "C08" -> "가전디지털";
                    case "C09" -> "스포츠/레저";
                    case "C10" -> "문구/오피스";
                    case "C11" -> "헬스/건강식품";
                    case "C12" -> "반려동물";
                    default -> "전체상품";
                };
            } else {
                category1Name = "전체상품";
            }

            category2Name = CategoryEnum.getNameByCode(cateCd);
        } else {
            category2Name = "상품 검색 결과";
        }



        // ✅ 모델에 추가
        model.addAttribute("category1Name", category1Name);
        model.addAttribute("category2Name", category2Name);

        return "product/prodList";
    }

    @GetMapping("/view")
    public String view(@RequestParam("prod_number") int prod_number, Model model) {
        ProductDTO product = productService.selectProductByNo(prod_number);
        ProductNoticeDTO notice = productService.selectProductNoticeByNo(prod_number);
        List<ProductReviewDTO> reviews = productService.selectProductReviews(prod_number);

        // ✅ 평균 평점 추가
        double avgRating = productService.selectAvgRating(prod_number);
        model.addAttribute("avgRating", avgRating);

        model.addAttribute("product", product);
        model.addAttribute("notice", notice);
        model.addAttribute("reviews", reviews);

        List<ProductOptionDTO> options = productService.selectProductOptions(prod_number);
        model.addAttribute("options", options);

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
