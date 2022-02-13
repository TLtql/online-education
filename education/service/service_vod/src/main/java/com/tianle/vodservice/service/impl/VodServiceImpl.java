package com.tianle.vodservice.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.tianle.servicebase.handler.CustomException;
import com.tianle.vodservice.service.VodService;
import com.tianle.vodservice.utils.InitVodClient;
import com.tianle.vodservice.utils.VodConstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Author: Tianle
 * @Date: 2022/1/1 17:48
 * @Description:
 */
@Service
public class VodServiceImpl implements VodService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public String uploadPlayVideo(MultipartFile file) {
        //获取阿里云API秘钥
        String accessKeyId = VodConstantUtil.KEY_ID;
        String accessKeySecret = VodConstantUtil.KEY_SECRET;
        //获取文件全名
        String fileName = file.getOriginalFilename();
        //截取文件名去掉后缀
        String title = fileName.substring(0, fileName.lastIndexOf("."));
        String videoId = null;
        try {
            //获取流
            InputStream inputStream = file.getInputStream();
            UploadStreamRequest request = new UploadStreamRequest(accessKeyId, accessKeySecret, title, fileName, inputStream);
            /* 存储区域(可选) */
            request.setStorageLocation("outin-a211afe06ad111ecba2b00163e038793.oss-cn-beijing.aliyuncs.com");
            /* 点播服务接入点 */
            request.setApiRegionId("cn-beijing");
            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);
            //获取上传后的阿里云视屏id
            videoId = response.getVideoId();
            if (!response.isSuccess()) {
                String errorMessage = "阿里云上传错误：" + "code：" + response.getCode() + ", message：" + response.getMessage();
                if(!StringUtils.hasLength(videoId)){
                    throw new CustomException(20001, errorMessage);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return videoId;
    }

    @Override
    public void deleteVideoById(String videoId) {
        try {
            DefaultAcsClient client = InitVodClient.initVodClient(VodConstantUtil.KEY_ID, VodConstantUtil.KEY_SECRET);
            DeleteVideoRequest request = new DeleteVideoRequest();
            request.setVideoIds(videoId);
            client.getAcsResponse(request);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(20001,"删除视屏失败");
        }
    }

    @Override
    public void removeVideoList(List<String> videoIdList) {
        try {
            DefaultAcsClient client = InitVodClient.initVodClient(
                    VodConstantUtil.KEY_ID, VodConstantUtil.KEY_SECRET);
            // 一次只能批量删除20个
            String videos = org.apache.commons.lang.StringUtils.join(videoIdList.toArray(), ",");
            DeleteVideoRequest request = new DeleteVideoRequest();
            request.setVideoIds(videos);
            client.getAcsResponse(request);
        } catch (Exception e) {
            throw new CustomException(20001, "视屏删除失败");
        }
    }

    @Override
    public String getPlayAuth(String videoId) {
        //获取阿里云API秘钥
        String accessKeyId = VodConstantUtil.KEY_ID;
        String accessKeySecret = VodConstantUtil.KEY_SECRET;
        String playAuth;
        try {
            DefaultAcsClient client = InitVodClient.initVodClient(accessKeyId, accessKeySecret);
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            request.setVideoId(videoId);
            GetVideoPlayAuthResponse response = client.getAcsResponse(request);
            playAuth = response.getPlayAuth();
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(20001,"获取凭证失败");
        }

        //统计视屏播放数
        String key ="EducationStatisticsDaily";
        String videoKey = "videoViewNumber::" + new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Boolean hasKey = redisTemplate.boundHashOps(key).hasKey(videoKey);
        if (hasKey){
            redisTemplate.boundHashOps(key).increment(videoKey, 1L);
        } else {
            redisTemplate.boundHashOps(key).put(videoKey,"1");
        }


        return playAuth;
    }
}
