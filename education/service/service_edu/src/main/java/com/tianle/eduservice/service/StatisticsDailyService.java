package com.tianle.eduservice.service;

import com.tianle.eduservice.entity.StatisticsDaily;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author Tianle
 * @since 2022-02-05
 */
public interface StatisticsDailyService extends IService<StatisticsDaily> {

    /**
     * 生成当天的统计数
     * @param day
     */
    void createStatistics(String day);
}
