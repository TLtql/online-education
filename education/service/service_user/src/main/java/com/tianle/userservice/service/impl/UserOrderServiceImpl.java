package com.tianle.userservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tianle.servicebase.handler.CustomException;
import com.tianle.userservice.client.EduServiceClient;
import com.tianle.userservice.entity.UserMember;
import com.tianle.userservice.entity.UserOrder;
import com.tianle.userservice.entity.vo.UserOrderDTO;
import com.tianle.userservice.mapper.UserOrderMapper;
import com.tianle.userservice.service.UserMemberService;
import com.tianle.userservice.service.UserOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianle.userservice.utils.OrderNoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Tianle
 * @since 2022-01-13
 */
@Service
public class UserOrderServiceImpl extends ServiceImpl<UserOrderMapper, UserOrder> implements UserOrderService {

    @Autowired
    private UserMemberService memberService;
    @Autowired
    private EduServiceClient eduClient;

    @Override
    public Boolean saveOrder(UserOrder userOrder) {
        userOrder.setOrderNo(OrderNoUtil.getOrderNo());
        int insert = baseMapper.insert(userOrder);
        if (insert == 0){
            throw new CustomException(20001, "订单生成失败");
        }

        UserMember member = memberService.getById(userOrder.getUserId());
        Long point = member.getPoint() - userOrder.getPoint();
        if (point < 0){
            throw new CustomException(20001, "积分不足，购买失败");
        }
        // 扣除积分
        member.setPoint(point);
        // 设置支付状态
        userOrder.setStatus(true);

        boolean b = memberService.updateById(member);
        int update = baseMapper.updateById(userOrder);
        eduClient.updateBuyCount(userOrder.getCourseId());

        if (b && update > 0){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean isExistOrder(String userId, String courseId) {
        QueryWrapper<UserOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("course_id", courseId);
        wrapper.eq("status", true);
        UserOrder order = baseMapper.selectOne(wrapper);
        if (order == null){
            return false;
        } else {
            return true;
        }
    }

    @Override
    public List<UserOrderDTO> getUserOrderList(String userId) {
        List<UserOrderDTO> orderList = baseMapper.selectListByUserId(userId);
        return orderList;
    }
}
