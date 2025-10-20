package kr.co.kmarket.controller.admin;

import kr.co.kmarket.dto.MemberDTO;
import kr.co.kmarket.dto.PageRequestDTO;
import kr.co.kmarket.dto.PageResponseDTO;
import kr.co.kmarket.service.MemberService;
import kr.co.kmarket.service.admin.SalesService;
import kr.co.kmarket.service.admin.ShopService;
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
@RequestMapping("/admin/shop")
@Controller
public class ShopController {
    private final ShopService shopService;
    private final MemberService memberService;
    private final SalesService salesService;

    @GetMapping("/list")
    public String list(Model model, PageRequestDTO pageRequestDTO) {
        pageRequestDTO.setSize(5);
        PageResponseDTO pageResponseDTO = shopService.selectAll(pageRequestDTO);
        log.info("pageResponseDTO = {}", pageResponseDTO);
        model.addAttribute("pageResponseDTO", pageResponseDTO);

        return "admin/shop/admin_shopList";
    }
    @GetMapping("/list/search")
    public String searchList(Model model, PageRequestDTO pageRequestDTO) { //검색 테스트
        pageRequestDTO.setSize(5);
        log.info("pageRequestDTO = {}", pageRequestDTO);

        PageResponseDTO pageResponseDTO = shopService.searchAll(pageRequestDTO);
        model.addAttribute("pageResponseDTO", pageResponseDTO);

        return"admin/shop/admin_shopList";
    }

    @PostMapping("/list")
    public String insertShop(MemberDTO memberDTO) {
        memberDTO.setAuth(3);
        memberDTO.setOperation("ready");
        log.info(memberDTO.toString());
        memberService.save(memberDTO);

        return "redirect:/admin/shop/list";
    }

    @PatchMapping("/list")
    @ResponseBody
    public ResponseEntity<?> updateOperation(@RequestBody Map<String, Object> data) { //운영 상태 변경
        Long custNumber = Long.valueOf(data.get("cust_number").toString());
        String operation = data.get("operation").toString();
        shopService.updateOperation(custNumber, operation);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/list")
    @ResponseBody
    public String deleteShop(@RequestBody List<Long> idList) {
        log.info("idList = " + idList);

        shopService.remove(idList);
        return "";
    }


    @GetMapping("/sales")
    public String sales(Model model, PageRequestDTO pageRequestDTO) {
        PageResponseDTO pageResponseDTO = salesService.selectAll(pageRequestDTO);
        log.info("매출현황 값 = {}", pageResponseDTO);
        model.addAttribute("pageResponseDTO", pageResponseDTO);

        return "admin/shop/admin_salesStatus";
    }

    @GetMapping("/sales/{type}")
    public String sales(@PathVariable("type") String type, PageRequestDTO pageRequestDTO, Model model) {
        PageResponseDTO pageResponseDTO = salesService.selectTypeAll(type, pageRequestDTO);
        log.info("일별,주별,달별 값 = {}", pageResponseDTO);
        model.addAttribute("pageResponseDTO", pageResponseDTO);

        return "admin/shop/admin_salesStatus";
    }
}
