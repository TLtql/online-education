package com.tianle.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tianle.eduservice.client.VodClient;
import com.tianle.eduservice.entity.EduVideo;
import com.tianle.eduservice.entity.vo.VideoInfoForm;
import com.tianle.eduservice.mapper.EduVideoMapper;
import com.tianle.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianle.servicebase.handler.CustomException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author Tianle
 * @since 2021-12-20
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    @Autowired
    private VodClient vodClient;

    @Override
    public void saveVideoInfo(VideoInfoForm videoInfo) {
        EduVideo eduVideo = new EduVideo();
        BeanUtils.copyProperties(videoInfo, eduVideo);
        int insert = baseMapper.insert(eduVideo);
        if (insert == 0){
            throw new CustomException(20001,"课时添加失败");
        }
    }

    @Override
    public VideoInfoForm getVideoInfoById(String videoId) {
        EduVideo eduVideo = baseMapper.selectById(videoId);
        if (eduVideo == null){
            throw new CustomException(20001, "数据不存在");
        }
        VideoInfoForm videoInfo = new VideoInfoForm();
        BeanUtils.copyProperties(eduVideo, videoInfo);
        return videoInfo;
    }

    @Override
    public void updateVideoById(VideoInfoForm videoInfoForm) {
        EduVideo eduVideo = new EduVideo();
        BeanUtils.copyProperties(videoInfoForm, eduVideo);
        int update = baseMapper.updateById(eduVideo);
        if (update == 0){
            throw new CustomException(20001,"课时信息修改失败");
        }
    }

    @Override
    public void deleteVideoById(String videoId) {
        EduVideo eduVideo = baseMapper.selectById(videoId);
        String videoSourceId = eduVideo.getVideoSourceId();
        // 删除阿里云视屏
        if (StringUtils.hasLength(videoSourceId)) {
            vodClient.deleteVideo(videoSourceId);
        }

        int delete = baseMapper.deleteById(videoId);
        if (delete == 0){
            throw new CustomException(20001,"课时删除失败");
        }
    }

    @Override
    public void deleteByCourseId(String courseId) {
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        wrapper.select("video_source_id");
        List<EduVideo> videoList = baseMapper.selectList(wrapper);

        List<String> videoSourceIds = new ArrayList<>();
        for (EduVideo video : videoList) {
            String videoSourceId = video.getVideoSourceId();
            if (StringUtils.hasLength(videoSourceId)){
                videoSourceIds.add(videoSourceId);
            }
        }

        if (videoSourceIds.size() > 0) {
            //调用vod服务删除远程视频
            vodClient.deleteVideoBatch(videoSourceIds);
        }

        QueryWrapper<EduVideo> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("course_id", courseId);
        baseMapper.delete(wrapper2);
    }
}
