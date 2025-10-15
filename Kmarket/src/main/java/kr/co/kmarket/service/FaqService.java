package kr.co.kmarket.service;

import kr.co.kmarket.dto.FaqDTO;
import kr.co.kmarket.mapper.FaqMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class FaqService {
    private final FaqMapper faqMapper;
    public void insertFaq(FaqDTO faqDTO) {faqMapper.insert(faqDTO);}
}
