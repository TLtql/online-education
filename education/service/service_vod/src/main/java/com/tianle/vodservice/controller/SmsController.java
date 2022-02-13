package com.tianle.vodservice.controller;

import com.tianle.commonutils.R;
import com.tianle.vodservice.service.SmsService;
import com.tianle.vodservice.utils.RandomUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * @Author: Tianle
 * @Date: 2022/1/7 17:54
 * @Description:
 */
@RestController
@RequestMapping("vodService/sms")
@CrossOrigin
@Api(tags = "腾讯云短信服务")
public class SmsController {
    @Autowired
    private SmsService smsService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @GetMapping("sendSms/{phone}")
    public R sendSmsCode(@PathVariable String phone){
        String code = redisTemplate.opsForValue().get(phone);
        if (StringUtils.hasLength(code)) {
            return R.ok();
        }
        //redis 中没有验证码 再进行验证码发送
        code = RandomUtil.getSixBitRandom();
        Boolean isSend = smsService.send(code, phone);
        if (isSend) {
            redisTemplate.opsForValue().set(phone, code,2, TimeUnit.MINUTES);
            return R.ok();
        } else {
            return R.error().message("发送信息失败");
        }
    }
}
