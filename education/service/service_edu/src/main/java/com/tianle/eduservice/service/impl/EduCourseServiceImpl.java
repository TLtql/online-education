package com.tianle.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tianle.eduservice.entity.EduCourse;
import com.tianle.eduservice.entity.EduCourseDescription;
import com.tianle.eduservice.entity.EduTeacher;
import com.tianle.eduservice.entity.vo.CourseInfoVo;
import com.tianle.eduservice.entity.vo.CoursePublishVo;
import com.tianle.eduservice.entity.vo.front.FrontCourseVo;
import com.tianle.eduservice.mapper.EduCourseMapper;
import com.tianle.eduservice.query.CourseQuery;
import com.tianle.eduservice.query.front.FrontCourseQuery;
import com.tianle.eduservice.service.EduChapterService;
import com.tianle.eduservice.service.EduCourseDescriptionService;
import com.tianle.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianle.eduservice.service.EduVideoService;
import com.tianle.servicebase.handler.CustomException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author Tianle
 * @since 2021-12-20
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    @Autowired
    private EduCourseDescriptionService courseDescriptionService;

    @Autowired
    private EduVideoService videoService;

    @Autowired
    private EduChapterService chapterService;

    @Override
    public String saveCourse(CourseInfoVo courseInfoVo) {
        // 添加课程信息
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int insert = baseMapper.insert(eduCourse);
        if (insert == 0){
            throw new CustomException(20001,"添加失败");
        }
        //获取课程Id
        String id = eduCourse.getId();

        //添加课程简介
        EduCourseDescription courseDescription = new EduCourseDescription();
        courseDescription.setDescription(courseInfoVo.getDescription());
        courseDescription.setId(id);
        courseDescriptionService.save(courseDescription);

        return id;
    }

    @Override
    public CourseInfoVo getCourseById(String id) {

        CourseInfoVo courseInfoVo = new CourseInfoVo();
        EduCourse eduCourse = baseMapper.selectById(id);
        if (eduCourse == null){
            throw new CustomException(20001,"数据不存在");
        }
        BeanUtils.copyProperties(eduCourse,courseInfoVo);
        EduCourseDescription description = courseDescriptionService.getById(id);
        courseInfoVo.setDescription(description.getDescription());
        return courseInfoVo;
    }

    @Override
    public void updateCourse(String id, CourseInfoVo courseInfoVo) {
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        eduCourse.setId(id);
        int update = baseMapper.updateById(eduCourse);
        if (update == 0){
            throw new CustomException(20001,"信息更新失败");
        }
        EduCourseDescription description = new EduCourseDescription();
        description.setDescription(courseInfoVo.getDescription());
        description.setId(id);
        boolean b = courseDescriptionService.updateById(description);
        if (!b){
            throw new CustomException(20001,"课程简介更新失败");
        }
    }

    @Override
    public CoursePublishVo fetchCoursePublishInfoById(String courseId) {
        CoursePublishVo coursePublish = baseMapper.selectCoursePublish(courseId);
        if (coursePublish == null){
            throw new CustomException(20001, "获取数据失败");
        }
        return coursePublish;
    }

    @Override
    public void publishCourseById(String courseId) {
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(courseId);
        eduCourse.setStatus("Normal");
        int update = baseMapper.updateById(eduCourse);
        if (update == 0){
            throw new CustomException(20001, "课程发布失败");
        }
    }

    @Override
    public void PageCourseList(Page<EduCourse> pageParam, CourseQuery courseQuery) {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("gmt_create");
        if (courseQuery == null){
            baseMapper.selectPage(pageParam,wrapper);
            return;
        }

        if (StringUtils.hasLength(courseQuery.getSubjectParentId())){
            wrapper.eq("subject_parent_id", courseQuery.getSubjectParentId());
        }
        if (StringUtils.hasLength(courseQuery.getSubjectId())){
            wrapper.eq("subject_id",courseQuery.getSubjectId());
        }
        if (StringUtils.hasLength(courseQuery.getTeacherId())){
            wrapper.eq("teacher_id", courseQuery.getTeacherId());
        }
        if (StringUtils.hasLength(courseQuery.getTitle())){
            wrapper.like("title", courseQuery.getTitle());
        }
        baseMapper.selectPage(pageParam,wrapper);
    }

    @Override
    public void deleteCourseById(String courseId) {
        // 根据课程id删除小节
        videoService.deleteByCourseId(courseId);
        // 根据课程id 删除章节
        chapterService.deleteByCourseId(courseId);
        // 根据课程id 删除课程详情
        boolean remove = courseDescriptionService.removeById(courseId);
        if (!remove){
            throw new CustomException(20001, "删除课程描述失败");
        }
        int delete = baseMapper.deleteById(courseId);
        if (delete == 0){
            throw new CustomException(20001, "删除课程失败");
        }
    }

    @Override
    public List<EduCourse> getIndexCourse() {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("buy_count");
        wrapper.eq("status", "Normal");
        wrapper.last("limit 8");
        List<EduCourse> courseList = baseMapper.selectList(wrapper);
        return courseList;
    }

    @Override
    public List<EduCourse> getCourseByTeacherId(String id) {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_id", id);
        wrapper.eq("status", "Normal");
        wrapper.orderByDesc("buy_count");
        wrapper.last("limit 4");
        List<EduCourse> courseList = baseMapper.selectList(wrapper);
        return courseList;
    }

    @Override
    public Map<String, Object> selectPageList(Page<EduCourse> pageParam, FrontCourseQuery courseQuery) {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("status","Normal");
        if (courseQuery == null){
            baseMapper.selectPage(pageParam,wrapper);
        }else {
            if (StringUtils.hasLength(courseQuery.getTeacherId())){
                wrapper.eq("teacher_id",courseQuery.getTeacherId());
            }
            if (StringUtils.hasLength(courseQuery.getSubjectParentId())){
                wrapper.eq("subject_parent_id", courseQuery.getSubjectParentId());
            }
            if (StringUtils.hasLength(courseQuery.getSubjectId())){
                wrapper.eq("subject_id",courseQuery.getSubjectId());
            }
            if (StringUtils.hasLength(courseQuery.getTitle())){
                wrapper.like("title", courseQuery.getTitle());
            }
            if (StringUtils.hasLength(courseQuery.getBuyCountSort())){
                wrapper.orderByDesc("buy_count");
            }
            if (StringUtils.hasLength(courseQuery.getGmtCreateSort())){
                wrapper.orderByDesc("gmt_create");
            }
            if (StringUtils.hasLength(courseQuery.getPriceSort())){
                wrapper.orderByDesc("price");
            }
            baseMapper.selectPage(pageParam, wrapper);
        }
        List<EduCourse> records = pageParam.getRecords();
        long current = pageParam.getCurrent();
        long pages = pageParam.getPages();
        long size = pageParam.getSize();
        long total = pageParam.getTotal();
        boolean hasNext = pageParam.hasNext();
        boolean hasPrevious = pageParam.hasPrevious();

        Map<String, Object> map = new HashMap<>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);
        return map;
    }

    @Override
    public FrontCourseVo getCourseInfoById(String courseId) {
        this.updatePageViewCount(courseId);
        FrontCourseVo courseVo = baseMapper.getCourseInfo(courseId);
        return courseVo;
    }

    @Override
    public void updateBuyCountById(String courseId) {
        EduCourse course = baseMapper.selectById(courseId);
        course.setBuyCount(course.getBuyCount() + 1);
        int update = baseMapper.updateById(course);
        if (update == 0){
            throw new CustomException(20001, "更新购买数时异常");
        }
    }

    @Override
    public void soldOutCourseById(String courseId) {
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(courseId);
        eduCourse.setStatus("Draft");
        int update = baseMapper.updateById(eduCourse);
        if (update == 0){
            throw new CustomException(20001, "课程下架失败");
        }
    }

    public void updatePageViewCount(String courseId){
        EduCourse course = baseMapper.selectById(courseId);
        course.setViewCount(course.getViewCount() + 1);
        int update = baseMapper.updateById(course);
        if (update == 0){
            throw new CustomException(20001, "更新浏览数时异常");
        }
    }

}
