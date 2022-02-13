package com.tianle.userservice.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tianle.userservice.entity.UserMember;
import com.tianle.userservice.service.UserMemberService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @Author: Tianle
 * @Date: 2022/1/30 14:20
 * @Description: 刷新用户的封禁状态
 */
@Component
public class BlockUtil implements InitializingBean {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private UserMemberService userService;

    private static RedisTemplate<String,String> redis;

    private static UserMemberService service;

    @Override
    public void afterPropertiesSet() throws Exception {
        redis = redisTemplate;
        service = userService;
    }

    /**
     * 更新所有用户的封禁状态
     */
    public static void updateAllBlockStatus(){
        QueryWrapper<UserMember> wrapper = new QueryWrapper<>();
        wrapper.eq("is_disabled",1);
        List<UserMember> list = service.list(wrapper);
        for (UserMember user : list) {
            String key = "block::" + user.getId();
            String value = redis.opsForValue().get(key);
            if (!StringUtils.hasLength(value)){
                user.setDisabled(0);
                service.updateById(user);
            }
        }
    }

    /**
     * 更新 单用户 封禁状态
     * @param id
     */
    public static void updateOneBlockStatus(String id){
        String key = "block::" + id;
        String value = redis.opsForValue().get(key);
        if (!StringUtils.hasLength(value)){
            UserMember user = service.getById(id);
            user.setDisabled(0);
            service.updateById(user);
        }
    }

    /**
     * 将秒换算成天、时、分、秒格式
     * @param mss
     * @return
     */
    public static String formatDateTime(long mss) {
        String DateTimes = null;
        long days = mss / ( 60 * 60 * 24);
        long hours = (mss % ( 60 * 60 * 24)) / (60 * 60);
        long minutes = (mss % ( 60 * 60)) /60;
        long seconds = mss % 60;
        if(days>0){
            DateTimes= days + "天" + hours + "小时" + minutes + "分钟"
                    + seconds + "秒";
        }else if(hours>0){
            DateTimes=hours + "小时" + minutes + "分钟"
                    + seconds + "秒";
        }else if(minutes>0){
            DateTimes=minutes + "分钟"
                    + seconds + "秒";
        }else{
            DateTimes=seconds + "秒";
        }

        return DateTimes;
    }
}
