package com.tianle.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.tianle.eduservice.entity.EduSubject;
import com.tianle.eduservice.entity.excel.SubjectData;
import com.tianle.eduservice.entity.vo.ReturnSubject;
import com.tianle.eduservice.listener.SubjectExcelListener;
import com.tianle.eduservice.mapper.EduSubjectMapper;
import com.tianle.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author Tianle
 * @since 2021-11-29
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    /**
     * 通过easyExcel 读取excel文件保存 课程分类
     * @param file
     * @param subjectService
     */
    @Override
    public void saveSubject(MultipartFile file, EduSubjectService subjectService) {
        try{
            InputStream inputStream = file.getInputStream();
            //通过easyExcel 读取excel文件保存 课程分类  SubjectExcelListener
            EasyExcel.read(inputStream, SubjectData.class, new SubjectExcelListener(subjectService)).sheet().doRead();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取课程分类列表， 并封装为树形结构
     * @return
     */
    @Override
    public List<ReturnSubject> getSubject() {
        //查询所有课程分类
        List<EduSubject> subjects = baseMapper.selectList(null);
        //最终结果集
        List<ReturnSubject> returnSubjects=new ArrayList<>();
        //遍历课程分类，parentId为0的为一级分类直接加入到结果集中
        for (EduSubject subject : subjects) {
            ReturnSubject returnSubject = new ReturnSubject();
            if("0".equals(subject.getParentId())){
                //returnSubject.setId(subject.getId());
                //returnSubject.setTitle(subject.getTitle());
                //spring  BeanUtils工具类可实现以上功能
                BeanUtils.copyProperties(subject, returnSubject);
                returnSubjects.add(returnSubject);
            }
        }

        //遍历结果集中的一级分类
        for (ReturnSubject returnSubject : returnSubjects){
            List<ReturnSubject> children = new ArrayList<>();
            //循环遍历，parentID 不为 0 的为二级分类
            for (EduSubject subject : subjects) {
                if(!"0".equals(subject.getParentId())){
                    ReturnSubject returnSubject2 = new ReturnSubject();
                    //通过比较二级分类的parentID 与 一级分类的id 值 相等的加入其 children 属性集合中
                    if (subject.getParentId().equals(returnSubject.getId())){
                        BeanUtils.copyProperties(subject, returnSubject2);
                        children.add(returnSubject2);
                    }
                    returnSubject.setChildren(children);
                }
            }
        }

        return returnSubjects;
    }
}
