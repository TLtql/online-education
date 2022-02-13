package com.tianle.eduservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tianle.eduservice.entity.EduTeacher;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tianle.eduservice.query.TeacherQuery;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author tianle
 * @since 2021-10-29
 */
public interface EduTeacherService extends IService<EduTeacher> {

    void pageQuery(Page<EduTeacher> pageParam, TeacherQuery teacherQuery);

    /**
     * 前台首页显示讲师
     * @return 返回前4条数据 作为人气讲师
     */
    List<EduTeacher> getIndexTeacher();
    /**
     * 前台讲师页显示讲师
     * @return 返回前8条数据 作为第一页数据
     */
    Map<String, Object> getFrontTeacher(Page<EduTeacher> pageParam);



}
