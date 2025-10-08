package kr.co.kmarket.service;


import kr.co.kmarket.dto.PolicyDTO;
import kr.co.kmarket.mapper.PolicyMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class PolicyService {
    private final PolicyMapper policyMapper;

    public List<PolicyDTO> selectPolicyAll(String type) {
        return policyMapper.selectAllByType(type);
    }
    public void updatePolicy(PolicyDTO policyDTO) {policyMapper.update(policyDTO);}
}
