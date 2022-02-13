package com.tianle.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.tianle.oss.service.FileService;
import com.tianle.oss.utils.ConstantPropertiesUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;

/**
 * @Author: Tianle
 * @Date: 2021/11/23 16:29
 * @Description:
 */
@Service
public class FileServiceImpl implements FileService {
    @Override
    public String upload(MultipartFile file, String host){
        String basePath = null;
        if (host == null){
            basePath="edu/";
        }else {
            basePath = "edu/"+host+"/";
        }
        String uploadUrl = null;
        try {
            OSS ossClient  = new OSSClientBuilder().build(ConstantPropertiesUtil.END_POINT
                    , ConstantPropertiesUtil.ACCESS_KEY_ID, ConstantPropertiesUtil.ACCESS_KEY_SECRET);
            // 获取流
            InputStream inputStream = file.getInputStream();
            //生成uuid
            String uuid = UUID.randomUUID().toString().replace("-", "").substring(0,6);
            // 拼接uuid 避免 文件名重复
            String filename =uuid + file.getOriginalFilename();

            // 创建PutObjectRequest对象
            PutObjectRequest putObjectRequest = new PutObjectRequest(ConstantPropertiesUtil.BUCKET_NAME,
                    basePath + filename, inputStream);

            //上传文件
            ossClient.putObject(putObjectRequest);
            //拼接返回文件路径
            uploadUrl = "https://" + ConstantPropertiesUtil.BUCKET_NAME + "."
                    + ConstantPropertiesUtil.END_POINT.substring(ConstantPropertiesUtil.END_POINT.lastIndexOf("/")+1)
                    + "/" + basePath + filename;
            // 关闭连接
            ossClient.shutdown();
        } catch (Exception e){
            e.printStackTrace();
        }
        return uploadUrl;
    }
}
