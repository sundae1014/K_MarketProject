package kr.co.kmarket.controller.admin;

import kr.co.kmarket.dto.PageRequestDTO;
import kr.co.kmarket.dto.PageResponseDTO;
import kr.co.kmarket.dto.QnaDTO;
import kr.co.kmarket.service.QnaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/cs")
@Controller
public class CsQnaController {
    private final  QnaService qnaService;

    @GetMapping("/qna/list")
    public String qnaList(Model model, PageRequestDTO pageRequestDTO) {
        PageResponseDTO pageResponseDTO = qnaService.selectAll(pageRequestDTO);
        log.info("pageResponseDTO={}", pageResponseDTO);

        model.addAttribute("pageResponseDTO", pageResponseDTO);

        return "admin/cs/qna/inquryList";
    }

    @DeleteMapping("/qna/list")
    @ResponseBody
    public ResponseEntity<Void> qnaDelete(@RequestBody List<Long> idList) {
        log.info("idList = " + idList);
        qnaService.remove(idList);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/qna/reply")
    public String qnaReply(int id, Model model) {
        log.info("id: {}", id);

        QnaDTO qnaDTO = qnaService.getQna(id);
        log.info("qnaDTO: {}", qnaDTO);
        model.addAttribute("qnaDTO", qnaDTO);

        return "admin/cs/qna/inquryAnswer";
    }

    @PostMapping("/qna/reply")
    public String qnaReply(QnaDTO qnaDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String admin_id = auth.getName();

        qnaDTO.setAdmin_id(admin_id);
        qnaDTO.setStatus("complete");
        log.info("qnaDTO: {}", qnaDTO);

        qnaService.insertAnswer(qnaDTO);

        return "redirect:/admin/cs/qna/list";
    }

    @GetMapping("/qna/view")
    public String qnaView(int id, Model model) {
        log.info("id: {}", id);

        QnaDTO qnaDTO = qnaService.getQna(id);
        log.info("qnaDTO: {}", qnaDTO);
        model.addAttribute("qnaDTO", qnaDTO);

        return "admin/cs/qna/inquryView";
    }

}
