package com.tianle.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tianle.eduservice.entity.StatisticsDaily;
import com.tianle.eduservice.mapper.StatisticsDailyMapper;
import com.tianle.eduservice.service.StatisticsDailyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianle.servicebase.handler.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author Tianle
 * @since 2022-02-05
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Override
    public void createStatistics(String day) {
        // 先删除存在的数据
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.eq("date_calculated",day);
        baseMapper.delete(wrapper);

        String key ="EducationStatisticsDaily";
        String registerKey = "registerNumber::" + day;
        String loginKey = "loginNumber::" + day;
        String videoViewKey = "videoViewNumber::" + day;

        StatisticsDaily statisticsDaily = new StatisticsDaily();

        Boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey){
            Map<Object, Object> map = redisTemplate.opsForHash().entries(key);
            if (map.containsKey(loginKey)){
                String loginNumber = map.get(loginKey).toString();
                statisticsDaily.setLoginNum(Integer.parseInt(loginNumber));
            } else {
                statisticsDaily.setLoginNum(0);
            }

            if (map.containsKey(registerKey)){
                statisticsDaily.setRegisterNum(Integer.parseInt((String) map.get(registerKey)));
            } else {
                statisticsDaily.setRegisterNum(0);
            }

            if (map.containsKey(videoViewKey)){
                statisticsDaily.setVideoViewNum(Integer.parseInt((String) map.get(videoViewKey)));
            } else {
                statisticsDaily.setVideoViewNum(0);
            }
        }
        statisticsDaily.setDateCalculated(day);

        int insert = baseMapper.insert(statisticsDaily);
        if (insert == 0){
            throw new CustomException(20001,"添加统计数据失败");
        }
    }
}
