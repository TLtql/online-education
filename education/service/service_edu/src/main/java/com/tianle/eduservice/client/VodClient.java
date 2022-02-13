package com.tianle.eduservice.client;

import com.tianle.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author: Tianle
 * @Date: 2022/1/3 14:12
 * @Description:  调用 service-vod 服务
 */
@Component
@FeignClient(name = "service-vod")
public interface VodClient {

    @DeleteMapping("/vodService/video/deleteVideo/{videoId}")
    R deleteVideo(@PathVariable("videoId") String videoId);

    @DeleteMapping("/vodService/video/delete-batch")
    R deleteVideoBatch(@RequestParam("videoIdList") List<String> videoIdList);


}
