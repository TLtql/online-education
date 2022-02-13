package com.tianle.eduservice.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tianle.commonutils.R;
import com.tianle.eduservice.entity.EduCourse;
import com.tianle.eduservice.entity.vo.CourseInfoVo;
import com.tianle.eduservice.entity.vo.CoursePublishVo;
import com.tianle.eduservice.query.CourseQuery;
import com.tianle.eduservice.service.EduCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author Tianle
 * @since 2021-12-20
 */
@RestController
@RequestMapping("/eduservice/edu-course")
@CrossOrigin
@Api(tags = "课程管理")
public class EduCourseController {
    @Autowired
    private EduCourseService courseService;

    @PostMapping("saveCourseInfo")
    @ApiOperation(value = "新增课程")
    public R saveCourseInfo(
            @ApiParam(name = "CourseInfoForm", value = "课程基本信息", required = true)
            @RequestBody CourseInfoVo courseInfoVo){
        String id = courseService.saveCourse(courseInfoVo);
        if (StringUtils.hasLength(id)){
            return R.ok().data("courseId",id);
        }else {
            return R.error().message("保存失败");
        }
    }
    @GetMapping("{id}")
    @ApiOperation("获取课程信息")
    public R getCourseInfo(
            @ApiParam(name = "id", value = "根据id 查询课程信息", required = true)
            @PathVariable String id){
        CourseInfoVo course=courseService.getCourseById(id);
        return R.ok().data("course", course);
    }
    @PutMapping("{id}")
    @ApiOperation("更新课程信息")
    public R updateCourseInfo(
            @ApiParam(name = "id", value = "更改课程的id", required = true)
            @PathVariable String id,
            @ApiParam(name = "CourseInfoForm", value = "课程基本信息", required = true)
            @RequestBody CourseInfoVo courseInfoVo){
        courseService.updateCourse(id,courseInfoVo);
        return R.ok();
    }
    @GetMapping("getCoursePublish/{courseId}")
    @ApiOperation("获取课程总览信息")
    private R fetchCoursePublishInfo(@PathVariable String courseId){
        CoursePublishVo coursePublishVo = courseService.fetchCoursePublishInfoById(courseId);
        return R.ok().data("courseInfo", coursePublishVo);
    }
    @PutMapping("publishCourse/{courseId}")
    @ApiOperation("课程的最终发布")
    private R publishCourse(@PathVariable String courseId){
        courseService.publishCourseById(courseId);
        return R.ok();
    }

    @PutMapping("soldOutCourse/{courseId}")
    @ApiOperation("课程下架")
    private R soldOutCourse(@PathVariable String courseId){
        courseService.soldOutCourseById(courseId);
        return R.ok();
    }
    @GetMapping("pageCourseList/{page}/{limit}")
    @ApiOperation("分页查询课程信息")
    private R getCourseList(
            @PathVariable Long limit,
            @PathVariable Long page,
            @ApiParam(name = "courseQuery", value = "查询对象", required = false)
            CourseQuery courseQuery){
        Page<EduCourse> pageParam = new Page<>(page,limit);
        courseService.PageCourseList(pageParam, courseQuery);
        return R.ok().data("page",pageParam);
    }
    @DeleteMapping("{courseId}")
    @ApiOperation("根据ID删除课程")
    private R deleteCourse(@PathVariable String courseId){
        courseService.deleteCourseById(courseId);
        return R.ok();
    }

}

