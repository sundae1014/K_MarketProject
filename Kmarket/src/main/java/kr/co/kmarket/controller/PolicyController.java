package kr.co.kmarket.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/policy")
@Controller
public class PolicyController {

    @GetMapping("/buyer")
    public String buyer(){return "/policy/policy_list";}
}
