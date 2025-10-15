package kr.co.kmarket.mapper;

import kr.co.kmarket.dto.StoryDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StoryMapper {

    List<StoryDTO> selectCardData();

    StoryDTO selectById(int story_no);

    List<StoryDTO> selectRecent();

}
