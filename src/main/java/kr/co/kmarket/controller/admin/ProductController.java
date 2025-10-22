package kr.co.kmarket.controller.admin;

import kr.co.kmarket.dto.*;
import kr.co.kmarket.service.admin.AdminProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("/list/search")
    public String searchList(Model model, PageRequestDTO pageRequestDTO) {
        PageResponseDTO pageResponseDTO = adminProductService.searchAll(pageRequestDTO);
        log.info("pageResponseDTO 검색={}", pageResponseDTO);
        model.addAttribute("pageResponseDTO", pageResponseDTO);

        return "admin/product/productList";
    }

    @GetMapping("/list/delete")
    public String deleteProduct(@RequestParam Long prod_number) {
        log.info("prod_number: {}", prod_number);
        adminProductService.remove(prod_number);

        return "redirect:/admin/product/list";
    }

    @DeleteMapping("/list")
    @ResponseBody
    public ResponseEntity<Void> deleteProductAll(@RequestBody List<Long> idList) {
        log.info("idList={}", idList);
        adminProductService.removeAll(idList);

        return ResponseEntity.ok().build();
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
                           ProductMangementDTO productMangementDTO,
                           @RequestParam(value = "file1", required = false) MultipartFile file1,
                           @RequestParam(value = "file2", required = false) MultipartFile file2,
                           @RequestParam(value = "file3", required = false) MultipartFile file3,
                           @RequestParam(value = "detailFile", required = false) MultipartFile detailFile) {
        log.info("file1={}, file2={}, file3={}, detailFile={}", file1, file2, file3, detailFile);
        int discount = productDTO.getDiscount();
        double  sale = (1 - Double.parseDouble(String.valueOf(productDTO.getDiscount())) / 100);
        int salePrice = (int) (productDTO.getPrice() * sale);

        if(productDTO.getDiscount() != 0) {
            productDTO.setSalePrice(salePrice);
        } else {
            productDTO.setSalePrice(productDTO.getPrice());
        }

        log.info("productDTO = {}, productNoticeDTO = {}, productMangementDTO = {}",  productDTO, productNoticeDTO, productMangementDTO);
        adminProductService.insert(productDTO, productNoticeDTO, productMangementDTO, file1, file2, file3, detailFile);

        return "redirect:/admin/product/list";
    }
}
