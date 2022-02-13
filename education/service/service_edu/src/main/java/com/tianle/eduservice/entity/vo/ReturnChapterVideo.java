package com.tianle.eduservice.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: Tianle
 * @Date: 2021/12/21 20:35
 * @Description: 树形结构返回章节和小节 实体类
 */
@Data
public class ReturnChapterVideo {
    private String id;
    private String title;
    private Boolean free;
    private String videoSourceId;
    private List<ReturnChapterVideo> children;
}
