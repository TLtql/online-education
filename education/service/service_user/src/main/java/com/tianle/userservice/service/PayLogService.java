package com.tianle.userservice.service;

import com.tianle.userservice.entity.PayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author Tianle
 * @since 2022-01-14
 */
public interface PayLogService extends IService<PayLog> {

    /**
     * 生成微信二维码并返回信息
     * @param id  用户id，生成orderNo
     * @param price 支付金额
     * @return
     */
    Map createNative(String id, BigDecimal price);

    /**
     * 查询支付状态
     *
     */
    Map<String, String> queryPayStatus(String orderNo);

    /**
     * 添加支付日志 并添加用户积分
     */
    void savePayLog(Map<String, String> map, String id);
}
