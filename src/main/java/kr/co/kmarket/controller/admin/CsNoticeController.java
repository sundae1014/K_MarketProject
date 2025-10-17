package kr.co.kmarket.controller.admin;

import kr.co.kmarket.dto.NoticeDTO;
import kr.co.kmarket.dto.PageRequestDTO;
import kr.co.kmarket.dto.PageResponseDTO;
import kr.co.kmarket.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/cs")
@Controller
public class CsNoticeController {
    private final NoticeService noticeService;

    @GetMapping("/notice/list")
    public String noticeList(Model model, PageRequestDTO pageRequestDTO) {
        PageResponseDTO pageResponseDTO = noticeService.selectAll(pageRequestDTO);
        log.info("관리자 공지사항 리스트: {}", pageResponseDTO);
        model.addAttribute("pageResponseDTO", pageResponseDTO);

        return "admin/cs/notice/noticeList";
    }

    @GetMapping("/notice/list/{nType}")
    public String noticeList(@PathVariable("nType") String nType, PageRequestDTO pageRequestDTO, Model model) {
        log.info("nType: {}", nType);
        String typeName = switch(nType){
            case "csService" -> "고객서비스";
            case "safeTrade" -> "안전거래";
            case "dangerProduct" -> "위해상품";
            case "event" -> "이벤트 당첨";

            default -> throw new IllegalStateException("Unexpected value: " + nType);
        };
        log.info("typeName={}", typeName);
        PageResponseDTO pageResponseDTO = noticeService.selectTypeAll(typeName, pageRequestDTO);
        model.addAttribute("pageResponseDTO", pageResponseDTO);

        return "admin/cs/notice/noticeList";
    }

    @DeleteMapping("/notice/list")
    @ResponseBody
    public ResponseEntity<Void> noticeDelete(@RequestBody List<Long> idList) {
        log.info("idList = " + idList);
        noticeService.remove(idList);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/notice/delete")
    public String deleteNotice(@RequestParam Long id) {
        log.info("id = {}", id);
        noticeService.remove2(id);

        return "redirect:/admin/cs/notice/list";
    }

    @GetMapping("/notice/view")
    public String noticeView(int id, Model model) {
        log.info("id: {}", id);

        NoticeDTO noticeDTO = noticeService.getNotice(id);
        log.info("noticeDTO: {}", noticeDTO);
        model.addAttribute("noticeDTO", noticeDTO);

        noticeDTO.setViews(noticeDTO.getViews() + 1); //조회수 증가
        noticeService.updateViews(noticeDTO);

        return "admin/cs/notice/noticeView";
    }

    @GetMapping("/notice/write")
    public String noticeWrite() {return "admin/cs/notice/noticeWrite";}

    @PostMapping("/notice/write")
    public String noticeWrite(NoticeDTO noticeDTO) {
        log.info("noticeDTO={}", noticeDTO);
        noticeDTO.setReg_date(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        noticeService.insertNotice(noticeDTO);

        return "redirect:/admin/cs/notice/list";
    }

    @GetMapping("/notice/modify")
    public String noticeModify(int id, Model model) {
        log.info("id: {}", id);
        NoticeDTO noticeDTO = noticeService.getNotice(id);
        log.info("수정 전 데이터: {}", noticeDTO);
        model.addAttribute("noticeDTO", noticeDTO);

        return "admin/cs/notice/noticeModify";
    }

    @PostMapping("/notice/modify")
    public String noticeModify(NoticeDTO noticeDTO) {
        log.info("수정할 데이터 = {}",  noticeDTO);
        noticeService.updateNotice(noticeDTO);

        return "redirect:/admin/cs/notice/list";
    }
}
