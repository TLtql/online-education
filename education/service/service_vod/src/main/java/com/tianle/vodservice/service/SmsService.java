package com.tianle.vodservice.service;

/**
 * @Author: Tianle
 * @Date: 2022/1/7 17:56
 * @Description:
 */
public interface SmsService {
    Boolean send(String code, String phone);
}
