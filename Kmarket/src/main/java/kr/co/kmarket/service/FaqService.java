package kr.co.kmarket.service;

import kr.co.kmarket.dto.FaqDTO;
import kr.co.kmarket.mapper.FaqMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class FaqService {
    private final FaqMapper faqMapper;

    public FaqDTO getFaq(int id) {return faqMapper.findById(id);}
    public List<FaqDTO> selectAll() {return faqMapper.findAll();}

    public List<FaqDTO> selectType1(String type1) {
        List<FaqDTO> dtoList = faqMapper.findType1(type1);
        log.info("dtoList={}", dtoList);

        return dtoList;
    }

    public List<FaqDTO> selectTypeAll(String type1, String type2) {
        List<FaqDTO> dtoList = faqMapper.findTypeAll(type1, type2);
        log.info("dtoList={}", dtoList);

        return dtoList;
    }

    public void insertFaq(FaqDTO faqDTO) {
        String type1 = faqDTO.getType1();
        String type2 = faqDTO.getType2();
        int count = faqMapper.countAll(type1, type2);
        log.info("count: = {}", count);

        if(count < 10) {
            faqMapper.insert(faqDTO);
        }
    }
    public void updateFaq(FaqDTO faqDTO) {faqMapper.update(faqDTO);}
    public void updateViews(FaqDTO  faqDTO) {faqMapper.update2(faqDTO);}

    public void remove(long id) {faqMapper.delete(id);}
    public void removeAll(List<Long> idList) {faqMapper.deleteAll(idList);}
}
