package kr.co.kmarket.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.kmarket.dto.BasicDTO;
import kr.co.kmarket.dto.MemberDTO;
import kr.co.kmarket.security.MyUserDetails;
import kr.co.kmarket.service.MemberService;
import kr.co.kmarket.service.PageCounterService;
import kr.co.kmarket.service.admin.BasicService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(basePackages = {"kr.co.kmarket.controller"})
@RequiredArgsConstructor
public class GlobalController {

    private final PageCounterService counterService;

    private final BasicService basicService;

    @ModelAttribute("member")
    public MemberDTO addUserToModel(@AuthenticationPrincipal MyUserDetails myMember) {
        if (myMember != null) {
            return myMember.getMemberDTO();
        }
        return new MemberDTO();
    }

    @ModelAttribute("visitorTotal")
    public int visitorTotal(HttpServletRequest request, HttpServletResponse response) {
        counterService.countVisit(request, response);
        return counterService.getTotalVisitsLastTwoDays();
    }

    @ModelAttribute("visitorToday")
    public int visitorToday() {
        return counterService.getTodayVisits();
    }

    @ModelAttribute("visitorYesterday")
    public int visitorYesterday() {
        return counterService.getYesterdayVisits();
    }


    @ModelAttribute("recentVersion")
    public String recentVersion(Model model) {
        return basicService.getRecentVersion();
    }

    @ModelAttribute("basicData")
    public BasicDTO basicData(Model model) {
        return basicService.getBasic();
    }
}