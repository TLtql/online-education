package com.tianle.vodservice.controller;

import com.tianle.commonutils.R;
import com.tianle.vodservice.service.VodService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author: Tianle
 * @Date: 2022/1/1 17:46
 * @Description:
 */
@RestController
@CrossOrigin
@RequestMapping("/vodService/video")
@Api(tags = "视屏点播")
public class VodController {

    @Autowired
    private VodService service;

    @PostMapping("uploadVideo")
    public R uploadVideo(MultipartFile file){
        String videoId = service.uploadPlayVideo(file);
        return R.ok().data("videoSourceId",videoId);
    }

    @DeleteMapping("deleteVideo/{videoId}")
    public R deleteVideo(@PathVariable String videoId){
        service.deleteVideoById(videoId);
        return R.ok();
    }

    @DeleteMapping("delete-batch")
    public R deleteVideoBatch(
            @ApiParam(name = "videoIdList", value = "云端视频id", required = true)
            @RequestParam("videoIdList") List<String> videoIdList){
        service.removeVideoList(videoIdList);
        return R.ok();
    }

    @GetMapping("getPlayAuth/{videoId}")
    public R getVideoPlayAuth(@PathVariable String videoId){
        String playAuth = service.getPlayAuth(videoId);
        return R.ok().data("playAuth",playAuth);
    }

}
