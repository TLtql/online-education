package com.tianle.eduservice.service;

import com.tianle.eduservice.entity.EduSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tianle.eduservice.entity.vo.ReturnSubject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author Tianle
 * @since 2021-11-29
 */
public interface EduSubjectService extends IService<EduSubject> {

    void saveSubject(MultipartFile file, EduSubjectService subjectService);

    List<ReturnSubject> getSubject();
}
