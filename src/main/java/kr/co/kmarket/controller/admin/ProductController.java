package kr.co.kmarket.controller.admin;

import kr.co.kmarket.dto.*;
import kr.co.kmarket.service.admin.AdminProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/product")
@Controller
public class ProductController {
    private final AdminProductService adminProductService;

    @GetMapping("/list")
    public String list(Model model, PageRequestDTO pageRequestDTO) {
        PageResponseDTO pageResponseDTO = adminProductService.selectAll(pageRequestDTO);
        log.info("pageResponseDTO={}", pageResponseDTO);

        model.addAttribute("pageResponseDTO", pageResponseDTO);

        return "admin/product/productList";
    }

    @GetMapping("/register")
    public String register() {
        return "admin/product/product_regist";
    }

    @GetMapping("/register/category")
    @ResponseBody
    public List<CategoryDTO> getCategory(@RequestParam("upCateCd") String upCateCd) {
        return adminProductService.selectCategory(upCateCd);
    }

    @PostMapping("/register")
    public String register(ProductDTO productDTO,
                           ProductNoticeDTO productNoticeDTO,
                           ProductMangementDTO productMangementDTO) {

        int discount = productDTO.getDiscount();
        double  sale = (1 - Double.parseDouble(String.valueOf(productDTO.getDiscount())) / 100);
        int salePrice = (int) (productDTO.getPrice() * sale);

        if(productDTO.getDiscount() != 0) {
            productDTO.setSalePrice(salePrice);
        }

        log.info("productDTO = {}, productNoticeDTO = {}, productMangementDTO = {}",  productDTO, productNoticeDTO, productMangementDTO);
        //adminProductService.insert(productDTO, productNoticeDTO, productMangementDTO);

        return "redirect:/admin/product/list";
    }
}
