package kr.co.kmarket.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.kmarket.dto.BannerDTO;
import kr.co.kmarket.dto.BasicDTO;
import kr.co.kmarket.dto.CategoryDTO;
import kr.co.kmarket.dto.MemberDTO;
import kr.co.kmarket.security.MyUserDetails;
import kr.co.kmarket.service.MemberService;
import kr.co.kmarket.service.PageCounterService;
import kr.co.kmarket.service.admin.BannerService;
import kr.co.kmarket.service.admin.BasicService;
import kr.co.kmarket.service.admin.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDate;
import java.util.*;

@ControllerAdvice(basePackages = {"kr.co.kmarket.controller"})
@RequiredArgsConstructor
public class GlobalController {

    private final PageCounterService counterService;

    private final BasicService basicService;

    private final CategoryService categoryService;

    private final BannerService bannerService;

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

    @ModelAttribute("basicData")
    public BasicDTO basicData(Model model) {
        return basicService.getBasic();
    }

    @ModelAttribute("categories")
    public Map<String, List<Map<String, String>>> addCategoriesToModel() {
        List<CategoryDTO> list = categoryService.getAllCategories();

        Map<String, List<Map<String, String>>> map = new LinkedHashMap<>();

        // 1차 카테고리 초기화
        list.stream()
                .filter(c -> c.getUp_cate_cd() == null)
                .sorted((a,b) -> Integer.compare(a.getCate_order(), b.getCate_order()))
                .forEach(c -> map.put(c.getCate_name(), new ArrayList<>()));

        // 2차 카테고리 추가
        list.stream()
                .filter(c -> c.getUp_cate_cd() != null)
                .forEach(c -> {
                    // 상위 카테고리 찾기
                    list.stream()
                            .filter(p -> p.getCate_cd().equals(c.getUp_cate_cd()))
                            .findFirst()
                            .ifPresent(parent -> {
                                map.get(parent.getCate_name())
                                        .add(Map.of("name", c.getCate_name(), "code", c.getCate_cd()));
                            });
                });

        return map;
    }

    @ModelAttribute("main2Banners")
    public List<BannerDTO> main2Banners() {
        return bannerService.getBannersByLocationStatus("main2");
    }


    @ModelAttribute("loginBanners")
    public List<BannerDTO> getLoginBanners() {
        LocalDate today = LocalDate.now();
        return bannerService.getBannersByLocationStatus("member1").stream()
                .filter(b -> b.getBanner_status() != null && b.getBanner_status() == 1) // 사용중인 배너만
                .filter(b -> {
                    LocalDate start = b.getStart_date();
                    LocalDate end = b.getEnd_date();
                    return (start == null || !today.isBefore(start)) && (end == null || !today.isAfter(end));
                })
                .sorted(Comparator.comparing(BannerDTO::getBanner_order))
                .toList();
    }

    @ModelAttribute("main1Banners")
    public List<BannerDTO> main1Banners() {
        return bannerService.getBannersByLocationStatus("main1");
    }

    @ModelAttribute("product1Banners")
    public List<BannerDTO> product1Banners() {
        return bannerService.getBannersByLocationStatus("product1");
    }

    @ModelAttribute("myBanners")
    public List<BannerDTO> myBanners() {
        return bannerService.getBannersByLocationStatus("my");
    }




}