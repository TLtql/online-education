package com.tianle.userservice.service;

import com.tianle.userservice.entity.UserOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tianle.userservice.entity.vo.UserOrderDTO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Tianle
 * @since 2022-01-13
 */
public interface UserOrderService extends IService<UserOrder> {

    Boolean saveOrder(UserOrder userOrder);

    Boolean isExistOrder(String userId, String courseId);

    List<UserOrderDTO> getUserOrderList(String userId);
}
