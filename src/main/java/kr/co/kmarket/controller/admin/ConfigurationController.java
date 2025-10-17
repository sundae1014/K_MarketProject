package kr.co.kmarket.controller.admin;

import kr.co.kmarket.controller.GlobalController;
import kr.co.kmarket.dto.*;
import kr.co.kmarket.service.PolicyService;
import kr.co.kmarket.service.admin.BasicService;
import kr.co.kmarket.service.admin.VersionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/config")
@Controller
public class ConfigurationController {
    private final PolicyService policyService;
    private final VersionService versionService;
    private final BasicService basicService;

    @Value("${file.upload.path}")
    private String uploadPath;

    @GetMapping("/basic")
    public String basic(Model model) {
        return "admin/configuration/admin_basicSetting";
    }

    @PostMapping("/basic/update")
    public String basic(BasicDTO basicDTO) throws IOException {

        if (basicDTO.getHeader_logoFile1() != null && !basicDTO.getHeader_logoFile1().isEmpty()) {
            String originalName = saveFileOriginalName(basicDTO.getHeader_logoFile1(), uploadPath);
            basicDTO.setHeader_logo_main(originalName);
        }
        if (basicDTO.getHeader_logoFile2() != null && !basicDTO.getHeader_logoFile2().isEmpty()) {
            String originalName = saveFileOriginalName(basicDTO.getHeader_logoFile2(), uploadPath);
            basicDTO.setHeader_logo_intro(originalName);
        }
        if (basicDTO.getHeader_logoFile3() != null && !basicDTO.getHeader_logoFile3().isEmpty()) {
            String originalName = saveFileOriginalName(basicDTO.getHeader_logoFile3(), uploadPath);
            basicDTO.setHeader_logo_admin(originalName);
        }

        if (basicDTO.getFooter_logoFile1() != null && !basicDTO.getFooter_logoFile1().isEmpty()) {
            String originalName = saveFileOriginalName(basicDTO.getFooter_logoFile1(), uploadPath);
            basicDTO.setFooter_logo_main(originalName);
        }
        if (basicDTO.getFooter_logoFile2() != null && !basicDTO.getFooter_logoFile2().isEmpty()) {
            String originalName = saveFileOriginalName(basicDTO.getFooter_logoFile2(), uploadPath);
            basicDTO.setFooter_logo_intro(originalName);
        }
        if (basicDTO.getFooter_logoFile3() != null && !basicDTO.getFooter_logoFile3().isEmpty()) {
            String originalName = saveFileOriginalName(basicDTO.getFooter_logoFile3(), uploadPath);
            basicDTO.setFooter_logo_admin(originalName);
        }

        if (basicDTO.getFavicon_File() != null && !basicDTO.getFavicon_File().isEmpty()) {
            String originalName = saveFileOriginalName(basicDTO.getFavicon_File(), uploadPath);
            basicDTO.setFavicon(originalName);
        }

        basicService.modifyBasic(basicDTO);
        return "redirect:/admin/config/basic";
    }

    private String saveFileOriginalName(MultipartFile file, String uploadDir) throws IOException {
        String originalFilename = file.getOriginalFilename();

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        file.transferTo(uploadPath.resolve(originalFilename));
        return originalFilename;
    }


    @GetMapping("/banner")
    public String banner() {return "admin/configuration/admin_banner";}

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
    public String category() {return "admin/configuration/admin_category";}

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
