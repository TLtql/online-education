package com.tianle.userservice.entity.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: Tianle
 * @Date: 2022/1/17 17:20
 * @Description: 用户收藏课程前台显示
 */
@Data
public class UserCollectDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    private String courseId;

    private String title;

    private String cover;

    private String teacherName;

    private Long price;

    private Date gmtCreate;
}
