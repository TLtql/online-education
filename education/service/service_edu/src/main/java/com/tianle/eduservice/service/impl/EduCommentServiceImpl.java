package com.tianle.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tianle.eduservice.entity.EduComment;
import com.tianle.eduservice.entity.vo.front.FrontCommentVo;
import com.tianle.eduservice.mapper.EduCommentMapper;
import com.tianle.eduservice.service.EduCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianle.servicebase.handler.CustomException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论 服务实现类
 * </p>
 *
 * @author Tianle
 * @since 2022-01-11
 */
@Service
public class EduCommentServiceImpl extends ServiceImpl<EduCommentMapper, EduComment> implements EduCommentService {

    @Override
    public Map<String, Object> pageList(Page<EduComment> pageParam, String courseId) {
        QueryWrapper<EduComment> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        wrapper.orderByDesc("gmt_create");
        baseMapper.selectPage(pageParam,wrapper);
        List<EduComment> commentList = pageParam.getRecords();

        Map<String, Object> map = new HashMap<>();
        map.put("items", commentList);
        map.put("current", pageParam.getCurrent());
        map.put("pages", pageParam.getPages());
        map.put("size", pageParam.getSize());
        map.put("total", pageParam.getTotal());
        map.put("hasNext", pageParam.hasNext());
        map.put("hasPrevious", pageParam.hasPrevious());
        return map;
    }

    @Override
    public List<FrontCommentVo> getUserComment(String userId) {
        List<FrontCommentVo> commentList = baseMapper.getCommentList(userId);
        return commentList;
    }

    @Override
    public void deleteCommentByUserId(String userId) {
        QueryWrapper<EduComment> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        int delete = baseMapper.delete(wrapper);
        if (delete == 0){
            throw new CustomException(20001, "删除评论失败");
        }
    }
}
