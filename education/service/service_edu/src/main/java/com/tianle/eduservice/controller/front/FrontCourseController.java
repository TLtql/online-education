package com.tianle.eduservice.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tianle.commonutils.R;
import com.tianle.eduservice.entity.EduCourse;
import com.tianle.eduservice.entity.EduTeacher;
import com.tianle.eduservice.entity.vo.ReturnChapterVideo;
import com.tianle.eduservice.entity.vo.front.FrontCourseVo;
import com.tianle.eduservice.query.TeacherQuery;
import com.tianle.eduservice.query.front.FrontCourseQuery;
import com.tianle.eduservice.service.EduChapterService;
import com.tianle.eduservice.service.EduCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author: Tianle
 * @Date: 2022/1/10 17:02
 * @Description: 课程前台接口
 */
@RestController
@CrossOrigin
@RequestMapping("/eduservice/front-course")
@Api(tags = "课程前台接口")
public class FrontCourseController {
    @Autowired
    private EduCourseService courseService;
    @Autowired
    private EduChapterService chapterService;

    @PostMapping("{page}/{limit}")
    public R getCourseList(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,

            @ApiParam(name = "teacherQuery", value = "查询对象", required = false)
            @RequestBody(required = false) FrontCourseQuery courseQuery){
        Page<EduCourse> pageParam = new Page<>(page, limit);
        Map<String,Object> map = courseService.selectPageList(pageParam, courseQuery);
        return R.ok().data(map);
    }

    @GetMapping("getCourseInfo/{courseId}")
    public R getCourseInfo(@PathVariable String courseId){
        FrontCourseVo courseVo = courseService.getCourseInfoById(courseId);
        List<ReturnChapterVideo> chapterVideos = chapterService.getChapterVideoByCourseId(courseId);
        return R.ok().data("chapterVideos", chapterVideos).data("courseInfo",courseVo);
    }

    @PutMapping("update-buyCount/{courseId}")
    public R updateBuyCount(@PathVariable String courseId){
        courseService.updateBuyCountById(courseId);
        return R.ok();
    }
}
