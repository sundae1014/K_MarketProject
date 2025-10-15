package kr.co.kmarket.service.admin;

import kr.co.kmarket.dto.PageRequestDTO;
import kr.co.kmarket.dto.PageResponseDTO;
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
    public VersionDTO getVersion(String versionId) {return versionMapper.findById(versionId);}

    public PageResponseDTO selectAll(PageRequestDTO pageRequestDTO) {
        List<VersionDTO> dtoList = versionMapper.findAll(pageRequestDTO);

        int total = versionMapper.selectCountTotal(pageRequestDTO);

        return PageResponseDTO.<VersionDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }
    public int selectCountTotal(PageRequestDTO pageRequestDTO) {
        return versionMapper.selectCountTotal(pageRequestDTO);
    }

    public void remove (List<Long> idList) {versionMapper.delete(idList);}
}
