package com.tianle.userservice.service;

import com.tianle.userservice.entity.UserCollect;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tianle.userservice.entity.vo.UserCollectDTO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Tianle
 * @since 2022-01-17
 */
public interface UserCollectService extends IService<UserCollect> {

    /**
     * 判断用户是否收藏了课程
     * @param userId
     * @param courseId
     * @return
     */
    boolean existUserCollect(String userId, String courseId);

    /**
     * 获取用户收藏列表
     * @param userId
     * @return
     */
    List<UserCollectDTO> getUserCollect(String userId);

    /**
     * 取消收藏
     */
    void cancelCollect(String userId, String courseId);
}
