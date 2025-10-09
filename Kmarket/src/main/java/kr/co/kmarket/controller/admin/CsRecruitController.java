package kr.co.kmarket.controller.admin;


import kr.co.kmarket.dto.HireDTO;
import kr.co.kmarket.service.admin.RecruitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/cs/recruit")
public class CsRecruitController {
    private final RecruitService recruitService;

    @GetMapping("/list")
    public String list(Model model){
        List<HireDTO> hires = recruitService.getAllHires();

        log.info("hires={}", hires);

        model.addAttribute("hires", hires);
        return "admin/cs/recruit/list";
    }
}
