package com.tianle.eduservice.controller;


import com.tianle.commonutils.R;
import com.tianle.eduservice.entity.EduVideo;
import com.tianle.eduservice.entity.vo.VideoInfoForm;
import com.tianle.eduservice.service.EduVideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author Tianle
 * @since 2021-12-20
 */
@RestController
@RequestMapping("/eduservice/edu-video")
@CrossOrigin
@Api(tags = "课程视屏")
public class EduVideoController {
    @Autowired
    private EduVideoService videoService;

    @PostMapping("addVideo")
    @ApiOperation("添加课时信息")
    private R saveVideo(@RequestBody VideoInfoForm videoInfo){
        videoService.saveVideoInfo(videoInfo);
        return R.ok();
    }

    @GetMapping("{videoId}")
    @ApiOperation("获取课时信息")
    private R getOneVideo(@PathVariable String videoId){
        VideoInfoForm videoInfo = videoService.getVideoInfoById(videoId);
        return R.ok().data("video",videoInfo);
    }
    @PutMapping
    @ApiOperation("修改课时信息")
    private R updateVideo(@RequestBody VideoInfoForm videoInfoForm){
        videoService.updateVideoById(videoInfoForm);
        return R.ok();
    }
    @ApiOperation("删除课时信息")
    @DeleteMapping("{videoId}")
    private R deleteVideo(@PathVariable String videoId){
        videoService.deleteVideoById(videoId);
        return R.ok();
    }

}

