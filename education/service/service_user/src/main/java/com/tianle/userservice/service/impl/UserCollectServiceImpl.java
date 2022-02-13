package com.tianle.userservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tianle.servicebase.handler.CustomException;
import com.tianle.userservice.entity.UserCollect;
import com.tianle.userservice.entity.vo.UserCollectDTO;
import com.tianle.userservice.mapper.UserCollectMapper;
import com.tianle.userservice.service.UserCollectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Tianle
 * @since 2022-01-17
 */
@Service
public class UserCollectServiceImpl extends ServiceImpl<UserCollectMapper, UserCollect> implements UserCollectService {

    @Override
    public boolean existUserCollect(String userId, String courseId) {
        QueryWrapper<UserCollect> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("course_id", courseId);
        UserCollect collect = baseMapper.selectOne(wrapper);
        if (collect == null){
            return false;
        } else {
            return true;
        }

    }

    @Override
    public List<UserCollectDTO> getUserCollect(String userId) {
        List<UserCollectDTO> collectList = baseMapper.selectListByUserId(userId);
        return collectList;
    }

    @Override
    public void cancelCollect(String userId, String courseId) {
        QueryWrapper<UserCollect> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).eq("course_id", courseId);
        int delete = baseMapper.delete(wrapper);
        if (delete == 0){
            throw new CustomException(20001, "取消收藏失败");
        }
    }
}
