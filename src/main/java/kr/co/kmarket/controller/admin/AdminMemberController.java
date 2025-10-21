package kr.co.kmarket.controller.admin;

import kr.co.kmarket.dto.AdminMemberDTO;
import kr.co.kmarket.dto.PageRequestDTO;
import kr.co.kmarket.dto.PageResponseDTO;
import kr.co.kmarket.service.admin.AdminMemberService;
import kr.co.kmarket.service.admin.AdminPointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/member")
@Controller
public class AdminMemberController {
    private final AdminMemberService adminMemberService;
    private final AdminPointService adminPointService;

    @GetMapping("/list")
    public String list(Model model, PageRequestDTO pageRequestDTO) { //회원목록
        PageResponseDTO pageResponseDTO = adminMemberService.selectAll(pageRequestDTO);
        log.info("관리자 회원목록 리스트: {}", pageResponseDTO);
        model.addAttribute("pageResponseDTO", pageResponseDTO);

        return "admin/member/admin_memberList";
    }

    @GetMapping("/list/search")
    public String searchList(Model model, PageRequestDTO pageRequestDTO) { //검색기능
        PageResponseDTO pageResponseDTO = adminMemberService.searchAll(pageRequestDTO);
        model.addAttribute("pageResponseDTO", pageResponseDTO);

        return "admin/member/admin_memberList";
    }

    @PatchMapping("/list")
    @ResponseBody
    public ResponseEntity<?> updateOperation(@RequestBody Map<String, Object> data) { //상태 수정
        String custId = data.get("custId").toString();
        String operation = data.get("operation").toString();
        log.info("custId: {}, operation: {} ", custId, operation);
        adminMemberService.updateOperation(custId, operation);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/list/rating")
    @ResponseBody
    public ResponseEntity<?> updateSelect(@RequestBody List<Map<String, Object>> data) { //선택수정
        for (Map<String, Object> item : data) {
            String custId = item.get("custId").toString();
            String rating = item.get("rating").toString();
            adminMemberService.updateRating(custId, rating);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list/{custId}")
    @ResponseBody
    public AdminMemberDTO modalView(@PathVariable String custId, Model model) {return adminMemberService.getView(custId);}

    @PostMapping("/list/modify")
    public String modalView(AdminMemberDTO adminMemberDTO) {
        log.info("adminMemberDTO= {}", adminMemberDTO);
        if(adminMemberDTO.getGender().equals("M")) {
            adminMemberDTO.setGender("2");
        } else if (adminMemberDTO.getGender().equals("F")) {
            adminMemberDTO.setGender("1");
        }
        adminMemberService.updateView(adminMemberDTO);
        return "redirect:/admin/member/list";
    }


    @GetMapping("/point")
    public String point(Model model, PageRequestDTO pageRequestDTO) {
        PageResponseDTO pageResponseDTO = adminPointService.selectAll(pageRequestDTO);
        log.info("포인트 관리 = {}", pageResponseDTO);
        model.addAttribute("pageResponseDTO", pageResponseDTO);

        return "admin/member/admin_pointManagement";
    }

    @GetMapping("/point/search")
    public String searchPoint(Model model, PageRequestDTO pageRequestDTO) { //검색기능
        PageResponseDTO pageResponseDTO = adminPointService.searchAll(pageRequestDTO);
        model.addAttribute("pageResponseDTO", pageResponseDTO);

        return "admin/member/admin_pointManagement";
    }

    @DeleteMapping("/point")
    @ResponseBody
    public String deletePoint(@RequestBody List<String> idList) {

        log.info("idList = " + idList);
        adminPointService.remove(idList);

        return "";
    }
}
