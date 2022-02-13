package com.tianle.eduservice.service;

import com.tianle.eduservice.entity.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tianle.eduservice.entity.vo.VideoInfoForm;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author Tianle
 * @since 2021-12-20
 */
public interface EduVideoService extends IService<EduVideo> {

    void saveVideoInfo(VideoInfoForm videoInfo);

    VideoInfoForm getVideoInfoById(String videoId);

    void updateVideoById(VideoInfoForm videoInfoForm);

    void deleteVideoById(String videoId);

    void deleteByCourseId(String courseId);
}
