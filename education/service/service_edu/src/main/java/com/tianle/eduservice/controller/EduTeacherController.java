package com.tianle.eduservice.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tianle.commonutils.R;
import com.tianle.eduservice.entity.EduTeacher;
import com.tianle.eduservice.query.TeacherQuery;
import com.tianle.eduservice.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author tianle
 * @since 2021-10-29
 */
@RestController
@RequestMapping("/eduservice/edu-teacher")
@Api(tags = "讲师管理")
@CrossOrigin
public class EduTeacherController {

    @Autowired
    private EduTeacherService teacherService;

    @ApiOperation("所有讲师列表")
    @GetMapping("findAll")
    public R findAll(){
        List<EduTeacher> eduTeachers = teacherService.list();
        return R.ok().data("items", eduTeachers);
    }

    @ApiOperation("逻辑删除讲师")
    @DeleteMapping("{id}")
    public R removeTeacher(@PathVariable Integer id){
        boolean b = teacherService.removeById(id);
        return b ? R.ok() : R.error();
    }

    @ApiOperation("分页查询讲师")
    @GetMapping("{page}/{limit}")
    public R pageList(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,

            @ApiParam(name = "teacherQuery", value = "查询对象", required = false)
            TeacherQuery teacherQuery){
        Page<EduTeacher> pageParam = new Page<>(page, limit);
        teacherService.pageQuery(pageParam,teacherQuery);
        return R.ok().data("page",pageParam);
    }

    @ApiOperation("新增讲师")
    @PostMapping
    public R saveTeacher(
            @ApiParam(name = "teacher", value = "讲师对象", required = true)
            @RequestBody EduTeacher teacher){

        boolean save = teacherService.save(teacher);
        return save ? R.ok() : R.error().message("保存失败！！！");
    }

    @ApiOperation("根据id查询讲师")
    @GetMapping("{id}")
    public R getTeacherById(@PathVariable Integer id){
        EduTeacher teacher = teacherService.getById(id);
        return R.ok().data("teacher",teacher);
    }

    @ApiOperation("根据Id修改讲师")
    @PutMapping("{id}")
    public R updateTeacher(
            @ApiParam(name = "id", value = "讲师id", required = true)
            @PathVariable Integer id,

            @ApiParam(name = "teacher", value = "讲师对象", required = true)
            @RequestBody EduTeacher teacher){

        teacher.setId(id);
        boolean b = teacherService.updateById(teacher);
        return b ? R.ok() : R.error();
    }


}

