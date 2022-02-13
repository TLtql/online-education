package com.tianle.eduservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tianle.eduservice.entity.EduSubject;
import com.tianle.eduservice.entity.excel.SubjectData;
import com.tianle.eduservice.service.EduSubjectService;
import com.tianle.servicebase.handler.CustomException;

/**
 * @Author: Tianle
 * @Date: 2021/11/29 17:20
 * @Description: easyExcel 的回调监听器
 */
public class SubjectExcelListener extends AnalysisEventListener<SubjectData> {

    private EduSubjectService subjectService;

    public SubjectExcelListener(){}

    //此类无法被spring 容器管理，每次读取都需要new，里面用到spring可以构造方法传进去
    public SubjectExcelListener(EduSubjectService subjectService){
        this.subjectService = subjectService;
    }
    @Override
    public void invoke(SubjectData subjectData, AnalysisContext analysisContext) {
        if(subjectData == null){
            throw new CustomException(20001,"文件为空");
        }
        EduSubject oneSubject = this.existOneSubject(subjectData.getOneSubjectName(), subjectService);
        if(oneSubject == null){//不存在此一级分类，则添加
            oneSubject = new EduSubject();
            oneSubject.setTitle(subjectData.getOneSubjectName());
            oneSubject.setParentId("0");
            subjectService.save(oneSubject);
        }
        //获取一级分类的id 作为二级分类父id
        String pid = oneSubject.getId();
        EduSubject twoSubject = this.existTwoSubject(subjectData.getTwoSubjectName(), subjectService, pid);
        if(twoSubject == null){//不存在此二级分类，则添加
            twoSubject = new EduSubject();
            twoSubject.setTitle(subjectData.getTwoSubjectName());
            twoSubject.setParentId(pid);
            subjectService.save(twoSubject);
        }

    }
    //判断一级分类是否存在
    private EduSubject existOneSubject(String name, EduSubjectService subjectService){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id","0");
        EduSubject subject = subjectService.getOne(wrapper);
        return subject;
    }
    // 判断二级分类是否存在
    private EduSubject existTwoSubject(String name, EduSubjectService subjectService, String pid){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id",pid);
        EduSubject subject = subjectService.getOne(wrapper);
        return subject;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
