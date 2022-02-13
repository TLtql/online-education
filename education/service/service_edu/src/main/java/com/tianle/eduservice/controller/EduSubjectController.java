package com.tianle.eduservice.controller;


import com.tianle.commonutils.R;
import com.tianle.eduservice.entity.vo.ReturnSubject;
import com.tianle.eduservice.service.EduSubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.rsocket.context.LocalRSocketServerPort;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author Tianle
 * @since 2021-11-29
 */
@RestController
@RequestMapping("/eduservice/edu-subject")
@Api(tags = "excel添加课程分类")
@CrossOrigin
public class EduSubjectController {
    @Autowired
    private EduSubjectService subjectService;

    @PostMapping("addSubject")
    @ApiOperation("excel上传")
    public R addSubject(MultipartFile file){
        subjectService.saveSubject(file, subjectService);
        return R.ok();
    }

    @GetMapping("getSubject")
    @ApiOperation("获取课程分类")
    public R getAllSubject(){
        List<ReturnSubject> list= subjectService.getSubject();
        return R.ok().data("subjectList",list);
    }
}

