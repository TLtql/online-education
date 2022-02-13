package com.tianle.eduservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tianle.eduservice.entity.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tianle.eduservice.entity.EduTeacher;
import com.tianle.eduservice.entity.vo.CourseInfoVo;
import com.tianle.eduservice.entity.vo.CoursePublishVo;
import com.tianle.eduservice.entity.vo.front.FrontCourseVo;
import com.tianle.eduservice.query.CourseQuery;
import com.tianle.eduservice.query.front.FrontCourseQuery;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author Tianle
 * @since 2021-12-20
 */
public interface EduCourseService extends IService<EduCourse> {

    String saveCourse(CourseInfoVo courseInfoVo);

    CourseInfoVo getCourseById(String id);

    void updateCourse(String id, CourseInfoVo courseInfoVo);

    CoursePublishVo fetchCoursePublishInfoById(String courseId);

    void publishCourseById(String courseId);

    void PageCourseList(Page<EduCourse> pageParam, CourseQuery courseQuery);

    void deleteCourseById(String courseId);

    /**
     * 前台首页显示课程
     * @return 返回前8条数据作为热门课程
     */
    List<EduCourse> getIndexCourse();

    /**
     * 前台讲师详情页显示课程
     * 根据讲师id 查询课程
     * @return 返回该讲师所讲前4个课程
     */
    List<EduCourse> getCourseByTeacherId(String id);

    /**
     * 前台课程列表页显示课程
     * 根据条件 查询课程
     * @return 返回分页数据
     */
    Map<String, Object> selectPageList(Page<EduCourse> pageParam, FrontCourseQuery courseQuery);

    /**
     * 前台课程详情页显示课程
     * 根据id 查询课程 并更新浏览量
     * @return 返回课程详情
     */
    FrontCourseVo getCourseInfoById(String courseId);

    /**
     * 当有人成功购买课程时更新BuyCount
     */
    void updateBuyCountById(String courseId);

    /**
     * 课程下架
     * @param courseId
     */
    void soldOutCourseById(String courseId);
}
