package com.tianle.vodservice.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author: Tianle
 * @Date: 2022/1/1 17:47
 * @Description:
 */
public interface VodService {
    String uploadPlayVideo(MultipartFile file);

    void deleteVideoById(String videoId);

    void removeVideoList(List<String> videoIdList);

    String getPlayAuth(String videoId);
}
