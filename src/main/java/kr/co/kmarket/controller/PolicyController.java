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
        model.addAttribute("dtoList", dtoList); //해당 약관 내용
        model.addAttribute("pageTitle","구매회원 이용약관"); //약관 제목
        model.addAttribute("activeMenu","buyer"); //맨위 약관 버튼 활성화

        return "/policy/policy_list";
    }

    @GetMapping("/seller")
    public String seller(Model model){
        List<PolicyDTO> dtoList = policyService.selectPolicyAll("seller");
        log.info("dtoList = {}", dtoList);
        model.addAttribute("dtoList", dtoList); //해당 약관 내용
        model.addAttribute("pageTitle","판매회원 이용약관"); //약관 제목
        model.addAttribute("activeMenu","seller"); //맨위 약관 버튼 활성화

        return "/policy/policy_list";
    }

    @GetMapping("/finance")
    public String finance(Model model){
        List<PolicyDTO> dtoList = policyService.selectPolicyAll("finance");
        log.info("dtoList = {}", dtoList);
        model.addAttribute("dtoList", dtoList); //해당 약관 내용
        model.addAttribute("pageTitle","전자금융거래 이용약관"); //약관 제목
        model.addAttribute("activeMenu","finance"); //맨위 약관 버튼 활성화

        return "/policy/policy_list";
    }

    @GetMapping("/location")
    public String location(Model model){
        List<PolicyDTO> dtoList = policyService.selectPolicyAll("location");
        log.info("dtoList = {}", dtoList);
        model.addAttribute("dtoList", dtoList); //해당 약관 내용
        model.addAttribute("pageTitle","위치정보 이용약관"); //약관 제목
        model.addAttribute("activeMenu","location"); //맨위 약관 버튼 활성화

        return "/policy/policy_list";
    }

    @GetMapping("/privacy")
    public String privacy(Model model){
        List<PolicyDTO> dtoList = policyService.selectPolicyAll("privacy");
        log.info("dtoList = {}", dtoList);
        model.addAttribute("dtoList", dtoList); //해당 약관 내용
        model.addAttribute("pageTitle","개인정보처리방침"); //약관 제목
        model.addAttribute("activeMenu","privacy"); //맨위 약관 버튼 활성화

        return "/policy/policy_list";
    }
}
