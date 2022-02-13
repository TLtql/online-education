package com.tianle.oss.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: Tianle
 * @Date: 2021/11/23 16:24
 * @Description:
 */
public interface FileService {

    String upload(MultipartFile file, String host);
}
