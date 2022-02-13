package com.tianle.eduservice.controller;


import com.tianle.commonutils.R;
import com.tianle.eduservice.service.StatisticsDailyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author Tianle
 * @since 2022-02-05
 */
@RestController
@RequestMapping("/eduservice/statistics-daily")
@CrossOrigin
@Api(tags = "统计数据")
public class StatisticsDailyController {

    @Autowired
    private StatisticsDailyService statisticsDailyService;

    @PostMapping("createStatistics/{day}")
    @ApiOperation("生成当天的统计数据")
    public R createStatistics(@PathVariable String day){
        statisticsDailyService.createStatistics(day);
        return R.ok();
    }
}

