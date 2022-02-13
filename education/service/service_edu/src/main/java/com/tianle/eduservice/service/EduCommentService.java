package com.tianle.eduservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tianle.eduservice.entity.EduComment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tianle.eduservice.entity.vo.front.FrontCommentVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论 服务类
 * </p>
 *
 * @author Tianle
 * @since 2022-01-11
 */
public interface EduCommentService extends IService<EduComment> {

    /**
     * 前台课程评论显示
     * @return
     */
    Map<String, Object> pageList(Page<EduComment> pageParam, String courseId);

    /**
     * 查询用户个人评论
     * @param userId
     * @return
     */
    List<FrontCommentVo> getUserComment(String userId);

    /**
     * 根据用户Id 删除评论
     * @param userId
     */
    void deleteCommentByUserId(String userId);
}
