package com.tianle.eduservice.mapper;

import com.tianle.eduservice.entity.EduCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tianle.eduservice.entity.vo.CoursePublishVo;
import com.tianle.eduservice.entity.vo.front.FrontCourseVo;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author Tianle
 * @since 2021-12-20
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {

    CoursePublishVo selectCoursePublish(String courseId);

    FrontCourseVo getCourseInfo(String courseId);
}
