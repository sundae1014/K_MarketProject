package kr.co.kmarket.controller;

import kr.co.kmarket.dto.HireDTO;
import kr.co.kmarket.dto.PageRequestDTO;
import kr.co.kmarket.dto.PageResponseDTO;
import kr.co.kmarket.dto.StoryDTO;
import kr.co.kmarket.service.StoryService;
import kr.co.kmarket.service.admin.RecruitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/company")
public class CompanyController {

    private final RecruitService recruitService;

    private final StoryService storyService;

    @GetMapping("/culture")
    public String culture(){
        return "company/culture";
    }

    @GetMapping("/index")
    public String index(){
        return "company/index";
    }

    @GetMapping("/media")
    public String media(){
        return "company/media";
    }

    @GetMapping("/recruit")
    public String recruit(PageRequestDTO pageRequestDTO, Model model){
        pageRequestDTO.setSize(4);

        List<HireDTO> hires = recruitService.getHiresByPage(pageRequestDTO);
        int total = recruitService.getTotalHires();

        PageResponseDTO<HireDTO> pageResponseDTO = PageResponseDTO.<HireDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(hires)
                .total(total)
                .build();

        model.addAttribute("RecruitPage", pageResponseDTO);

        log.info("RecruitPage = {}", pageResponseDTO);

        return "company/recruit";
    }

    @GetMapping("/recruit/detail")
    public String recruitDetail(@RequestParam("hire_no") int hire_no, Model model) {
        HireDTO hire = recruitService.getHire(hire_no);
        model.addAttribute("hire", hire);
        return "company/recruit/detail";
    }

    @GetMapping("/story")
    public String story(Model model){
        List<StoryDTO> storyItems =  storyService.getStoryList();
        log.info("storyItems = {}", storyItems);
        model.addAttribute("storyItems", storyItems);
        return "company/story";
    }

    @GetMapping("/story/detail")
    public String detail(@RequestParam("story_no") int story_no, Model model) {
        StoryDTO storyitem = storyService.getStoryById(story_no);
        log.info("storyitem = {}", storyitem);
        model.addAttribute("storyitem", storyitem);
        return "company/story/detail";
    }
}
