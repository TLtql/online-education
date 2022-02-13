package com.tianle.eduservice.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: Tianle
 * @Date: 2021/12/1 20:02
 * @Description:  树形结构课程分类返回格式实体类
 */
@Data
public class ReturnSubject {
    private String id;
    private String title;
    private List<ReturnSubject> children;
}
