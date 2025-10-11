package kr.co.kmarket.controller.admin;

import kr.co.kmarket.service.admin.TotalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AdminMainController {
    private final TotalService totalService;

    @GetMapping("/admin")
    public String admin(Model model){

        int regYesterday = totalService.getMemberRegYesterday();
        int regToday = totalService.getMemberRegToday();

        int regTotal = regYesterday +  regToday;

        log.info("regYesterday={}, regToday={}, regToday={}", regYesterday, regToday, regToday);

        model.addAttribute("regYesterday", regYesterday);
        model.addAttribute("regToday", regToday);
        model.addAttribute("regTotal", regTotal);

        return "admin/admin";
    }

}
