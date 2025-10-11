package kr.co.kmarket.service.admin;

import kr.co.kmarket.dto.MemberDTO;
import kr.co.kmarket.mapper.admin.ShopMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ShopService {
    private final ShopMapper shopMapper;

    public List<MemberDTO> selectAll() {
        return shopMapper.findAll();
    }

    public void updateOperation(Long custNumber, String operation) {
        shopMapper.update(custNumber, operation);
    }
}
