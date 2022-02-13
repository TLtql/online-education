package com.tianle.userservice.controller;


import com.tianle.commonutils.JwtUtils;
import com.tianle.commonutils.R;
import com.tianle.servicebase.handler.CustomException;
import com.tianle.userservice.service.PayLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author Tianle
 * @since 2022-01-14
 */
@RestController
@RequestMapping("/userservice/pay-log")
@CrossOrigin
@Api(tags = "微信支付接口")
public class PayLogController {
    @Autowired
    private PayLogService payLogService;

    @GetMapping("createNative/{price}")
    @ApiOperation("生成支付二维码")
    public R createNative(HttpServletRequest request, @PathVariable BigDecimal price){
        String id = JwtUtils.getMemberIdByJwtToken(request);
        if (!StringUtils.hasLength(id)){
            throw new CustomException(20001,"用户未登录");
        }
        Map map = payLogService.createNative(id, price);
        return R.ok().data(map);
    }

    @GetMapping("queryPayStatus/{orderNo}")
    @ApiOperation("查询支付状态")
    public R queryPayStatus(HttpServletRequest request, @PathVariable String orderNo){
        String id = JwtUtils.getMemberIdByJwtToken(request);
        if (!StringUtils.hasLength(id)){
            throw new CustomException(20001,"用户未登录");
        }
        Map<String, String> map = payLogService.queryPayStatus(orderNo);
        System.out.println("***************"+map.toString());
        if (map == null) {//出错
            return R.error().message("支付出错");
        }
        if (map.get("trade_state").equals("SUCCESS")) {//如果成功
            //更改订单状态
            payLogService.savePayLog(map,id);
            return R.ok().message("支付成功");
        }
        return R.ok().code(25000).message("支付中");
    }
}

