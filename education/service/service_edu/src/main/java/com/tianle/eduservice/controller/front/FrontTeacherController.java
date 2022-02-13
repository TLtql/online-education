package com.tianle.eduservice.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tianle.commonutils.R;
import com.tianle.eduservice.entity.EduCourse;
import com.tianle.eduservice.entity.EduTeacher;
import com.tianle.eduservice.service.EduCourseService;
import com.tianle.eduservice.service.EduTeacherService;
import com.tianle.servicebase.handler.CustomException;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author: Tianle
 * @Date: 2022/1/10 14:46
 * @Description: 讲师前台接口
 */
@RestController
@CrossOrigin
@RequestMapping("/eduservice/front-teacher")
@Api(tags = "讲师前台接口")
public class FrontTeacherController {
    @Autowired
    private EduTeacherService teacherService;

    @Autowired
    private EduCourseService courseService;

    @GetMapping("{page}/{limit}")
    public R getTeacherList(@PathVariable Long page, @PathVariable Long limit){
        Page<EduTeacher> pageParam = new Page<>(page, limit);
        Map<String,Object> map = teacherService.getFrontTeacher(pageParam);
        return R.ok().data(map);
    }

    @GetMapping("getTeacherInfo/{id}")
    public R getTeacherInfo(@PathVariable String id){
        EduTeacher teacher = teacherService.getById(id);
        if (teacher == null){
            throw new CustomException(20001, "讲师不存在");
        }
        List<EduCourse> courseList = courseService.getCourseByTeacherId(id);
        return R.ok().data("teacher", teacher).data("courseList", courseList);
    }
}
