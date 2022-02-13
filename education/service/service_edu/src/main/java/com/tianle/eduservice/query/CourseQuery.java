package com.tianle.eduservice.query;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Tianle
 * @Date: 2021/12/25 15:04
 * @Description:
 */
@ApiModel(value = "Course查询对象", description = "课程查询对象封装")
@Data
public class CourseQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    private String subjectParentId;

    private String subjectId;

    private String title;

    private String teacherId;

}
