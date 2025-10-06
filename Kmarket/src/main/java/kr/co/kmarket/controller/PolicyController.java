package kr.co.kmarket.controller;

import kr.co.kmarket.dto.PolicyDTO;
import kr.co.kmarket.service.PolicyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/policy")
@Controller
public class PolicyController {
    private final PolicyService policyService;

    @GetMapping("/buyer")
    public String buyer(Model model){
        List<PolicyDTO> dtoList = policyService.selectPolicyAll("buyer");
        log.info("dtoList = {}", dtoList);
        model.addAttribute(dtoList);

        return "/policy/policy_list";
    }
}
