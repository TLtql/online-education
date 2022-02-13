package com.tianle.eduservice.entity.vo.front;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Tianle
 * @Date: 2022/1/19 17:44
 * @Description: 用户中心评论列表
 */
@Data
public class FrontCommentVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String courseId;

    private String cover;

    private String title;

    private String content;

    private String gmtCreate;
}
