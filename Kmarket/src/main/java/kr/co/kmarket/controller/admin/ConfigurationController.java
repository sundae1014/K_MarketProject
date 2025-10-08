package kr.co.kmarket.controller.admin;

import kr.co.kmarket.dto.PolicyDTO;
import kr.co.kmarket.service.PolicyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/config")
@Controller
public class ConfigurationController {
    private final PolicyService policyService;

    @GetMapping("/basic")
    public String basic() {return "admin/configuration/admin_basicSetting";}

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
        log.info(policyDTO.toString());

        return "redirect:/admin/config/policy";
    }

    @GetMapping("/category")
    public String category() {return "admin/configuration/admin_category";}

    @GetMapping("/version")
    public String version() {return "admin/configuration/admin_version";}
}
