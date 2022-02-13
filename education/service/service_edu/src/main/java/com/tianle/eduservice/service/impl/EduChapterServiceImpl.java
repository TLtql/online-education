package com.tianle.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tianle.eduservice.entity.EduChapter;
import com.tianle.eduservice.entity.EduVideo;
import com.tianle.eduservice.entity.vo.ReturnChapterVideo;
import com.tianle.eduservice.mapper.EduChapterMapper;
import com.tianle.eduservice.service.EduChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianle.eduservice.service.EduVideoService;
import com.tianle.servicebase.handler.CustomException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author Tianle
 * @since 2021-12-20
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduVideoService videoService;

    @Override
    public List<ReturnChapterVideo> getChapterVideoByCourseId(String courseId) {
        //返回结果集
        List<ReturnChapterVideo> returnList=new ArrayList<>();
        //查询章节
        QueryWrapper<EduChapter> chapterWrapper = new QueryWrapper<>();
        chapterWrapper.eq("course_id",courseId);
        chapterWrapper.orderBy(true, true, "sort");
        List<EduChapter> eduChapters = baseMapper.selectList(chapterWrapper);
        //查询小节
        QueryWrapper<EduVideo> videoWrapper = new QueryWrapper<>();
        videoWrapper.eq("course_id",courseId);
        videoWrapper.orderBy(true, true, "sort");
        List<EduVideo> eduVideos = videoService.list(videoWrapper);
        //将章节存入一级结果集
        for (EduChapter eduChapter : eduChapters) {
            ReturnChapterVideo returnChapterVideo = new ReturnChapterVideo();
            BeanUtils.copyProperties(eduChapter,returnChapterVideo);
            returnList.add(returnChapterVideo);
        }
        // 遍历一级结果集
        for (ReturnChapterVideo returnChapterVideo : returnList) {
            //二级结果集
            List<ReturnChapterVideo> childrenList = new ArrayList<>();
            //遍历小节，存入对应章节中
            for (EduVideo eduVideo : eduVideos) {

                if(returnChapterVideo.getId().equals(eduVideo.getChapterId())){
                    ReturnChapterVideo returnChapterVideoChildren = new ReturnChapterVideo();
                    BeanUtils.copyProperties(eduVideo,returnChapterVideoChildren);
                    childrenList.add(returnChapterVideoChildren);
                }
            }
            returnChapterVideo.setChildren(childrenList);
        }
        return returnList;
    }

    @Override
    public void addChapterByCourseId(EduChapter eduChapter, String courseId) {
        eduChapter.setCourseId(courseId);
        int insert = baseMapper.insert(eduChapter);
        if (insert == 0){
            throw new CustomException(20001, "添加失败");
        }
    }

    @Override
    public EduChapter getChapterById(String chapterId) {
        EduChapter eduChapter = baseMapper.selectById(chapterId);
        if(eduChapter == null){
            throw new CustomException(20001, "数据不存在");
        }
        return eduChapter;
    }

    @Override
    public void updateChapterById(EduChapter eduChapter) {
        int update = baseMapper.updateById(eduChapter);
        if (update == 0){
            throw new CustomException(20001, "更新失败");
        }
    }

    @Override
    public void deleteChapterById(String chapterId) {
        int delete = baseMapper.deleteById(chapterId);
        if (delete == 0){
            throw new CustomException(20001, "删除失败");
        }
    }

    @Override
    public void deleteByCourseId(String courseId) {
        QueryWrapper<EduChapter> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        int delete = baseMapper.delete(wrapper);
    }
}
