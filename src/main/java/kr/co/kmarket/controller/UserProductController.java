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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

        model.addAttribute("category1Name", category1Name);
        model.addAttribute("category2Name", category2Name);

        return "product/prodList";
    }

    @GetMapping("/view")
    public String view(@RequestParam("prod_number") int prod_number,
                       @RequestParam(value = "page", defaultValue = "1") int page,
                       Model model) {

        ProductDTO product = productService.selectProductByNo(prod_number);
        ProductNoticeDTO notice = productService.selectProductNoticeByNo(prod_number);

        // 리뷰 관련 부분
        int total = productService.countProductReviews(prod_number);
        int pageSize = 5;
        int offset = (page - 1) * pageSize;
        List<ProductReviewDTO> reviews = productService.selectProductReviews(prod_number);
        double avgRating = productService.selectAvgRating(prod_number);

        int reviewCount = total;
        int totalPages = (int) Math.ceil((double) total / pageSize);

        model.addAttribute("product", product);
        model.addAttribute("notice", notice);
        model.addAttribute("reviews", reviews);
        model.addAttribute("avgRating", avgRating);
        model.addAttribute("reviewCount", reviewCount);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);

        List<ProductOptionDTO> options = productService.selectProductOptions(prod_number);
        model.addAttribute("options", options);

        // 배송 예정일 계산 (오늘 기준 + 2일)
        LocalDate today = LocalDate.now();
        LocalDate arrivalDate = today.plusDays(2);

        // 요일 한글 변환
        DayOfWeek dayOfWeek = arrivalDate.getDayOfWeek();
        String dayKorean = switch (dayOfWeek) {
            case MONDAY -> "월";
            case TUESDAY -> "화";
            case WEDNESDAY -> "수";
            case THURSDAY -> "목";
            case FRIDAY -> "금";
            case SATURDAY -> "토";
            case SUNDAY -> "일";
        };

        // 날짜 포맷
        String formattedDate = arrivalDate.format(DateTimeFormatter.ofPattern("MM/dd"));

        // 예: “모레(금) 10/17 도착 예정”
        String deliveryEstimate = String.format("모레(%s) %s 도착 예정", dayKorean, formattedDate);

        model.addAttribute("deliveryEstimate", deliveryEstimate);

        return "product/prodView";
    }


    /*@GetMapping("/cart")
    public String cart(){
        return "product/prodCart";
    }*/

}
