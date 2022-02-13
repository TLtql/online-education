package com.tianle.eduservice.service;

import com.tianle.eduservice.entity.EduChapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tianle.eduservice.entity.vo.ReturnChapterVideo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author Tianle
 * @since 2021-12-20
 */
public interface EduChapterService extends IService<EduChapter> {

    List<ReturnChapterVideo> getChapterVideoByCourseId(String courseId);

    void addChapterByCourseId(EduChapter eduChapter,String courseId);

    EduChapter getChapterById(String chapterId);

    void updateChapterById(EduChapter eduChapter);

    void deleteChapterById(String chapterId);

    void deleteByCourseId(String courseId);
}
