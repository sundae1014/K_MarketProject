package kr.co.kmarket.mapper.admin;

import kr.co.kmarket.dto.BannerDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BannerMapper {
    List<BannerDTO> selectBanners();
    void insertBanner(BannerDTO dto);
    void updateBannerStatus(int banner_no, int banner_status);
    void deleteBanners(List<Integer> banner_No);
    List<BannerDTO> selectBannersByLocation(String location);
    List<BannerDTO> selectBannersByLocationStatus(String location);
    void deleteBanner(List<Integer> bannerNos);
}
