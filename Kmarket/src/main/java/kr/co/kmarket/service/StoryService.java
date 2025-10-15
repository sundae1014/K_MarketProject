package kr.co.kmarket.service;

import kr.co.kmarket.dto.StoryDTO;
import kr.co.kmarket.mapper.StoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StoryService {

    private final StoryMapper storyMapper;

    public List<StoryDTO> getStoryList(){
        return storyMapper.selectCardData();
    }

    public StoryDTO getStoryById(int story_no){
        return storyMapper.selectById(story_no);
    }

    public List<StoryDTO> getRecentStory(){
        return storyMapper.selectRecent();
    }
}
