package com.tianle.eduservice.controller;


import com.tianle.commonutils.R;
import com.tianle.eduservice.entity.EduChapter;
import com.tianle.eduservice.entity.vo.ReturnChapterVideo;
import com.tianle.eduservice.service.EduChapterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author Tianle
 * @since 2021-12-20
 */
@RestController
@RequestMapping("/eduservice/edu-chapter")
@CrossOrigin
@Api(tags = "章节管理")
public class EduChapterController {
    @Autowired
    private EduChapterService chapterService;

    @GetMapping("chapterList/{courseId}")
    @ApiOperation("获取课程大纲")
    public R getChapterVideo(@PathVariable String courseId){
        List<ReturnChapterVideo> list=chapterService.getChapterVideoByCourseId(courseId);
        return R.ok().data("list",list);
    }
    @PostMapping("addChapter/{courseId}")
    @ApiOperation("添加章节")
    public R addChapter(@RequestBody EduChapter chapter, @PathVariable String courseId){
        chapterService.addChapterByCourseId(chapter, courseId);
        return R.ok();
    }
    @GetMapping("{chapterId}")
    @ApiOperation("根据id获取章节")
    public R getOneChapter(@PathVariable String chapterId){
        EduChapter eduChapter = chapterService.getChapterById(chapterId);
        return R.ok().data("chapter",eduChapter);
    }
    @PutMapping("{id}")
    @ApiOperation("根据id 修改章节")
    public R updateChapter(@RequestBody EduChapter eduChapter, @PathVariable String id){
        eduChapter.setId(id);
        chapterService.updateChapterById(eduChapter);
        return R.ok();
    }
    @DeleteMapping("{chapterId}")
    @ApiOperation("删除章节")
    public R deleteChapter(@PathVariable String chapterId){
        chapterService.deleteChapterById(chapterId);
        return R.ok();
    }

}

