package kr.co.kmarket.controller.admin;

import kr.co.kmarket.dto.MemberDTO;
import kr.co.kmarket.service.MemberService;
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

    @GetMapping("/list")
    public String list(Model model) { //상점 목록
        List<MemberDTO> dtoList = shopService.selectAll();
        log.info("dtoList={}", dtoList);

        model.addAttribute("dtoList", dtoList);

        return "admin/shop/admin_shopList";
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

    @GetMapping("/sales")
    public String sales() {return "admin/shop/admin_salesStatus";}
}
