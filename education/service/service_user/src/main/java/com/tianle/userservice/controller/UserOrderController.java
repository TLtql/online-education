package com.tianle.userservice.controller;


import com.tianle.commonutils.JwtUtils;
import com.tianle.commonutils.R;
import com.tianle.servicebase.handler.CustomException;
import com.tianle.userservice.entity.UserMember;
import com.tianle.userservice.entity.UserOrder;
import com.tianle.userservice.entity.vo.UserOrderDTO;
import com.tianle.userservice.service.UserOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Tianle
 * @since 2022-01-13
 */
@RestController
@RequestMapping("/userservice/user-order")
@CrossOrigin
@Api(tags = "订单")
public class UserOrderController {
    @Autowired
    private UserOrderService orderService;

    @PostMapping("create-order")
    public R createOrder(@RequestBody UserOrder userOrder){
        Boolean b = orderService.saveOrder(userOrder);
        return R.ok().data("isBuyCourse", b);
    }

    @GetMapping("existOrder/{userId}/{courseId}")
    public R existOrder(@PathVariable String userId, @PathVariable String courseId){
        Boolean isBuyCourse = orderService.isExistOrder(userId, courseId);
        return R.ok().data("isBuyCourse", isBuyCourse);
    }

    @GetMapping("getUserOrder")
    @ApiOperation("获取用户订单")
    public R getUserOrder(HttpServletRequest request){
        try {
            String userId = JwtUtils.getMemberIdByJwtToken(request);
            List<UserOrderDTO> orderList = orderService.getUserOrderList(userId);
            return R.ok().data("orderList",orderList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(20001,"获取订单出错了");
        }
    }
    @DeleteMapping("deleteOrder/{orderId}")
    @ApiOperation("删除订单")
    public R deleteOrder(@PathVariable String orderId){
        boolean remove = orderService.removeById(orderId);
        if (remove){
            return R.ok();
        } else {
            return R.error().message("删除订单失败");
        }
    }
}

