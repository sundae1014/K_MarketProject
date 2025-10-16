package kr.co.kmarket.controller.admin;


import kr.co.kmarket.dto.HireDTO;
import kr.co.kmarket.dto.PageRequestDTO;
import kr.co.kmarket.dto.PageResponseDTO;
import kr.co.kmarket.service.admin.RecruitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/cs/recruit")
public class CsRecruitController {
    private final RecruitService recruitService;

    @GetMapping("/list")
    public String listHires(PageRequestDTO pageRequestDTO,
                            @RequestParam(value="searchType", required=false) String searchType,
                            @RequestParam(value="keyword", required=false) String keyword,
                            Model model, RedirectAttributes redirectAttributes) {

        if ("hire_no".equals(searchType)) {
            try {
                Integer.parseInt(keyword);
            } catch (NumberFormatException e) {
                redirectAttributes.addFlashAttribute("msg", "채용번호 검색 시 숫자만 입력 가능합니다.");
                return "redirect:/admin/cs/recruit/list";
            }
        }

        pageRequestDTO.setSize(4);

        List<HireDTO> hires;
        int total;

        if(searchType != null && keyword != null && !keyword.isEmpty()) {
            hires = recruitService.selectSearchPage(searchType, keyword, pageRequestDTO);
            total = recruitService.countSearchHires(searchType, keyword);
            model.addAttribute("searchType", searchType);
            model.addAttribute("keyword", keyword);
        } else {
            hires = recruitService.getHiresByPage(pageRequestDTO);
            total = recruitService.getTotalHires();
        }

        PageResponseDTO<HireDTO> pageResponseDTO = PageResponseDTO.<HireDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(hires)
                .total(total)
                .build();

        model.addAttribute("RecruitPage", pageResponseDTO);

        return "admin/cs/recruit/list";
    }



    @PostMapping("/delete")
    public ResponseEntity<String> deleteHires(@RequestBody List<Integer> hire_no) {
        log.info("hire_no={}", hire_no);
        recruitService.deleteAllByHireNo(hire_no);
        return ResponseEntity.ok("sucess");
    }

    @PostMapping("/insert")
    public ResponseEntity<HireDTO> insertHires(@RequestBody HireDTO hireDTO, @AuthenticationPrincipal UserDetails member) {
        log.info("hireDTO={}", hireDTO);

        hireDTO.setAuthor(member.getUsername());

        LocalDate today = LocalDate.now();

        // String → LocalDate 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate endDate = LocalDate.parse(hireDTO.getRecruit_period_end(), formatter);

        // 오늘 이전이면 종료
        if (endDate.isBefore(today)) {
            hireDTO.setStatus("종료");
        } else {
            hireDTO.setStatus("모집중");
        }

        recruitService.insertHire(hireDTO);

        return ResponseEntity.ok(hireDTO);
    }

    @GetMapping("/list/json")
    public ResponseEntity<List<HireDTO>> getHireList() {
        List<HireDTO> hires = recruitService.getAllHires();
        return ResponseEntity.ok(hires);
    }

    @Scheduled(cron = "0 0 0 * * *") // 매일 자정
    public void updateStatusDaily() {
       recruitService.updateExpiredHires();
    }



}
