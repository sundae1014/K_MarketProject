package kr.co.kmarket.service.admin;


import kr.co.kmarket.dto.BasicDTO;
import kr.co.kmarket.mapper.admin.BasicMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicService {

    private final BasicMapper basicMapper;

    public String getRecentVersion(){
        return basicMapper.selectRecentVersion();
    }

    public void modifyBasic(BasicDTO basicDTO){
        basicMapper.updateBasic(basicDTO);
    }

    public BasicDTO getBasic(){
        return basicMapper.selectBasic();
    }
}
