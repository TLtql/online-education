package com.tianle.eduservice.entity.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Tianle
 * @Date: 2021/12/24 17:13
 * @Description:
 */
@ApiModel(value = "课程发布信息")
@Data
public class CoursePublishVo  implements Serializable {

    private static final long serialVersionUID = 1L;

    private String title;
    private String cover;
    private Integer lessonNum;
    private String subjectLevelOne;
    private String subjectLevelTwo;
    private String teacherName;
    private String price;//只用于显示
}
