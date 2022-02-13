package com.tianle.vodservice.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author: Tianle
 * @Date: 2022/1/1 18:17
 * @Description:
 */
@Component
public class VodConstantUtil implements InitializingBean {

    @Value("${aliyun.vod.file.keyId}")
    private String keyId;

    @Value("${aliyun.vod.file.keySecret}")
    private String keySecret;

    public static String KEY_ID;

    public static String KEY_SECRET;
    @Override
    public void afterPropertiesSet() throws Exception {
        KEY_ID = keyId;
        KEY_SECRET = keySecret;
    }
}
