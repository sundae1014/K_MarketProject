package kr.co.kmarket.service.admin;

import kr.co.kmarket.mapper.admin.TotalMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TotalService {
    private final TotalMapper totalMapper;
    public int getMemberRegToday(){
        return totalMapper.selectCountRegToday();
    };
    public int getMemberRegYesterday(){
        return totalMapper.selectCountRegYesterday();
    };

}
