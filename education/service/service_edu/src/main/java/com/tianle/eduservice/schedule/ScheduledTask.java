package com.tianle.eduservice.schedule;

import com.tianle.eduservice.service.StatisticsDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

/**
 * @Author: Tianle
 * @Date: 2022/2/5 15:38
 * @Description: 定时任务
 */
@Component
public class ScheduledTask {
    @Autowired
    private StatisticsDailyService staService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    // 每天凌晨1点执行
    @Scheduled(cron = "0 0 1 * * ?")
    public void crateStatistics(){
        // 将前一天的数据统计
        String day = new SimpleDateFormat("yyyy-MM-dd").format(LocalDate.now().plusDays(-1));
        staService.createStatistics(day);

        // 删除前一天的缓存
        String key ="EducationStatisticsDaily";
        String registerKey = "registerNumber::" + day;
        String loginKey = "loginNumber::" + day;
        String videoViewKey = "videoViewNumber::" + day;
        redisTemplate.boundHashOps(key).delete(registerKey);
        redisTemplate.boundHashOps(key).delete(loginKey);
        redisTemplate.boundHashOps(key).delete(videoViewKey);
    }
}
