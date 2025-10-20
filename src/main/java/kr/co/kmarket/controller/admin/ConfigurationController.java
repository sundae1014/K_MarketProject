package kr.co.kmarket.controller.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.kmarket.controller.GlobalController;
import kr.co.kmarket.dto.*;
import kr.co.kmarket.mapper.admin.CategoryMapper;
import kr.co.kmarket.service.PolicyService;
import kr.co.kmarket.service.admin.BannerService;
import kr.co.kmarket.service.admin.BasicService;
import kr.co.kmarket.service.admin.CategoryService;
import kr.co.kmarket.service.admin.VersionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/config")
@Controller
public class ConfigurationController {
    private final PolicyService policyService;
    private final VersionService versionService;
    private final BasicService basicService;
    private final CategoryService categoryService;
    private final BannerService bannerService;

    @Value("${file.upload.path}")
    private String uploadPath;

    @GetMapping("/basic")
    public String basic(Model model) {
        return "admin/configuration/admin_basicSetting";
    }

    @PostMapping("/basic/update")
    public String basic(BasicDTO basicDTO) throws IOException {

        if (basicDTO.getHeader_logoFile1() != null && !basicDTO.getHeader_logoFile1().isEmpty()) {
            String originalName = saveFileOriginalName(basicDTO.getHeader_logoFile1(), uploadPath, "logo");
            basicDTO.setHeader_logo_main(originalName);
        }
        if (basicDTO.getHeader_logoFile2() != null && !basicDTO.getHeader_logoFile2().isEmpty()) {
            String originalName = saveFileOriginalName(basicDTO.getHeader_logoFile2(), uploadPath, "logo");
            basicDTO.setHeader_logo_intro(originalName);
        }
        if (basicDTO.getHeader_logoFile3() != null && !basicDTO.getHeader_logoFile3().isEmpty()) {
            String originalName = saveFileOriginalName(basicDTO.getHeader_logoFile3(), uploadPath, "logo");
            basicDTO.setHeader_logo_admin(originalName);
        }

        if (basicDTO.getFooter_logoFile1() != null && !basicDTO.getFooter_logoFile1().isEmpty()) {
            String originalName = saveFileOriginalName(basicDTO.getFooter_logoFile1(), uploadPath, "logo");
            basicDTO.setFooter_logo_main(originalName);
        }
        if (basicDTO.getFooter_logoFile2() != null && !basicDTO.getFooter_logoFile2().isEmpty()) {
            String originalName = saveFileOriginalName(basicDTO.getFooter_logoFile2(), uploadPath, "logo");
            basicDTO.setFooter_logo_intro(originalName);
        }
        if (basicDTO.getFooter_logoFile3() != null && !basicDTO.getFooter_logoFile3().isEmpty()) {
            String originalName = saveFileOriginalName(basicDTO.getFooter_logoFile3(), uploadPath, "logo");
            basicDTO.setFooter_logo_admin(originalName);
        }

        if (basicDTO.getFavicon_File() != null && !basicDTO.getFavicon_File().isEmpty()) {
            String originalName = saveFileOriginalName(basicDTO.getFavicon_File(), uploadPath, "logo");
            basicDTO.setFavicon(originalName);
        }

        basicService.modifyBasic(basicDTO);
        return "redirect:/admin/config/basic";
    }

    private String saveFileOriginalName(MultipartFile file, String uploadDir, String subDir) throws IOException {
        String originalFilename = file.getOriginalFilename();

        Path path = Paths.get(uploadDir, subDir);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        file.transferTo(path.resolve(originalFilename));
        return originalFilename;
    }



    @GetMapping("/banner")
    public String banner(Model model) {
        List<BannerDTO> banners = bannerService.selectBanners();
        model.addAttribute("bannerList", banners);
        return "admin/configuration/admin_banner";
    }

    @GetMapping("/banner/list")
    @ResponseBody
    public ResponseEntity<List<BannerDTO>> getBannerList(@RequestParam String location) {
            List<BannerDTO> banners = bannerService.getBannersByLocation(location);
        log.info("location = {}, banners = {}", location, banners);
            return ResponseEntity.ok(banners);
    }

    @PostMapping("/banner/register")
    @ResponseBody
    public Map<String, Object> banner(@ModelAttribute BannerDTO dto) throws IOException {
        MultipartFile file = dto.getImgFile();
        if (file != null && !file.isEmpty()) {
            String fileName = file.getOriginalFilename();

            Path targetPath = Paths.get(uploadPath, "banner");
            if (!Files.exists(targetPath)) {
                Files.createDirectories(targetPath);
            }

            file.transferTo(targetPath.resolve(fileName));
            dto.setImg(fileName);
        }

        if(dto.getBanner_order() == null) dto.setBanner_order(0);
        if(dto.getBanner_status() == null) dto.setBanner_status(1);

        bannerService.insertBanner(dto);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        return result;
    }


    @PostMapping("/banner/delete")
    @ResponseBody
    public ResponseEntity<?> banner(@RequestBody List<Integer> bannerNos) {
        bannerService.deleteBanners(bannerNos);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/banner/status")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateBannerStatus(@RequestParam int banner_no,
                                                                  @RequestParam int banner_status) {
        bannerService.updateBannerStatus(banner_no, banner_status);
        log.info("banner_no = {}, banner_status = {}", banner_no, banner_status);
        return ResponseEntity.ok(Map.of("success", true));
    }
    @GetMapping("/policy")
    public String policy(Model model) {
        List<PolicyDTO> buyerList = policyService.selectPolicyAll("buyer");
        List<PolicyDTO> sellerList = policyService.selectPolicyAll("seller");
        List<PolicyDTO> financeList = policyService.selectPolicyAll("finance");
        List<PolicyDTO> locationList = policyService.selectPolicyAll("location");
        List<PolicyDTO> privacyList = policyService.selectPolicyAll("privacy");

        model.addAttribute("buyerList", buyerList);
        model.addAttribute("sellerList", sellerList);
        model.addAttribute("financeList", financeList);
        model.addAttribute("locationList", locationList);
        model.addAttribute("privacyList", privacyList);

        return "admin/configuration/admin_policy";
    }

    @PostMapping("/policy")
    public String policyUpdate(PolicyDTO policyDTO) {
        log.info("policyDTO = {}", policyDTO);
        policyService.updatePolicy(policyDTO);

        return "redirect:/admin/config/policy";
    }

    @GetMapping("/category")
    public String category(Model model) {
        List<CategoryDTO> categories = categoryService.getAllCategories();

        Map<String, List<CategoryDTO>> grouped = categories.stream()
                .filter(c->c.getUp_cate_cd() != null)
                .collect(Collectors.groupingBy(CategoryDTO::getUp_cate_cd));

        List<CategoryDTO> topCategories = categories.stream()
                .filter(c->c.getUp_cate_cd()==null)
                .collect(Collectors.toList());

        model.addAttribute("topCategories", topCategories);
        model.addAttribute("grouped", grouped);

        return "admin/configuration/admin_category";
    }

    @PostMapping("/category/update")
    public String saveCate(
            @ModelAttribute CategoryDTO categoryDTO,
            @RequestParam(value="deletedCateCd", required=false) String deletedCateCd,
            @RequestParam(value="orderData", required=false) String orderDataJson) throws JsonProcessingException {

        categoryService.updateCategories(categoryDTO, deletedCateCd, orderDataJson);

        return "redirect:/admin/config/category";
    }

    @GetMapping("/version")
    public String version(Model  model, PageRequestDTO pageRequestDTO) {
        PageResponseDTO pageResponseDTO = versionService.selectAll(pageRequestDTO);
        log.info("pageResponseDTO = {}", pageResponseDTO);
        model.addAttribute("pageResponseDTO", pageResponseDTO);

        return "admin/configuration/admin_version";
    }

    @GetMapping("/version/{versionId}")
    @ResponseBody
    public ResponseEntity<VersionDTO> version(@PathVariable("versionId") String versionId, Model  model) {
        log.info("versionId = {}", versionId);
        VersionDTO versionDTO = versionService.getVersion(versionId);
        log.info("versionDTO = {}", versionDTO);

        model.addAttribute("versionDTO", versionDTO);

        return ResponseEntity.ok(versionDTO);
    }


    @PostMapping("/version")
    @ResponseBody
    public Map<String, Object> versionInsert(@RequestBody VersionDTO versionDTO) {
        log.info("versionDTO = {}", versionDTO);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String custId = auth.getName();
        log.info("userId = {}", custId);

        versionDTO.setWriter(custId);
        versionDTO.setReg_date(LocalDateTime.now(ZoneId.of("Asia/Seoul"))); //현재 시간 추가
        versionService.save(versionDTO);

        Map<String, Object> result = new HashMap<>();
        result.put("status", "success");

        return result;
    }

    @DeleteMapping("/version")
    @ResponseBody
    public ResponseEntity<Void> versionDelete(@RequestBody List<Long> idList) {
        log.info("idList = " + idList);

        versionService.remove(idList);

        return ResponseEntity.ok().build();
    }
}
