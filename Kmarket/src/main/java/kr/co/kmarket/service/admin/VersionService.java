package kr.co.kmarket.service.admin;

import kr.co.kmarket.dto.VersionDTO;
import kr.co.kmarket.mapper.admin.VersionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class VersionService {
    private final VersionMapper versionMapper;

    public void save(VersionDTO versionDTO) {versionMapper.insert(versionDTO);}
    public List<VersionDTO> selectAll() {return versionMapper.findAll();}
    public void remove (List<Long> idList) {versionMapper.delete(idList);}
}
