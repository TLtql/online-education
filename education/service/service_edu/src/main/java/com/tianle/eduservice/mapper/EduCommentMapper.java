package com.tianle.eduservice.mapper;

import com.tianle.eduservice.entity.EduComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tianle.eduservice.entity.vo.front.FrontCommentVo;

import java.util.List;

/**
 * <p>
 * 评论 Mapper 接口
 * </p>
 *
 * @author Tianle
 * @since 2022-01-11
 */
public interface EduCommentMapper extends BaseMapper<EduComment> {

    List<FrontCommentVo> getCommentList(String userId);
}
