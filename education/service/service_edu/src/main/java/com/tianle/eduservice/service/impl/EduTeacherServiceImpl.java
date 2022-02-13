package com.tianle.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tianle.eduservice.entity.EduTeacher;
import com.tianle.eduservice.mapper.EduTeacherMapper;
import com.tianle.eduservice.query.TeacherQuery;
import com.tianle.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author tianle
 * @since 2021-10-29
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {

    /**
     * 条件分页查询
     * @param pageParam
     * @param teacherQuery
     */
    @Override
    public void pageQuery(Page<EduTeacher> pageParam, TeacherQuery teacherQuery) {
        if (teacherQuery==null){
            baseMapper.selectPage(pageParam,null);
            return;
        }
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();

        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();

        if (StringUtils.hasLength(name)){
            wrapper.like("name", name);
        }
        if(level!=null){
            wrapper.eq("level", level);
        }
        if(StringUtils.hasLength(begin)){
            wrapper.ge("gmt_create", begin);
        }
        if(StringUtils.hasLength(end)){
            wrapper.le("gmt_modified", end);
        }

        wrapper.orderByDesc("gmt_create");

        baseMapper.selectPage(pageParam,wrapper);


    }

    @Override
    public List<EduTeacher> getIndexTeacher() {
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("sort");
        wrapper.last("limit 4");
        List<EduTeacher> teacherList = baseMapper.selectList(wrapper);
        return teacherList;
    }

    @Override
    public Map<String, Object> getFrontTeacher(Page<EduTeacher> pageParam) {
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("sort");
        baseMapper.selectPage(pageParam, wrapper);

        List<EduTeacher> records = pageParam.getRecords();
        long current = pageParam.getCurrent();
        long pages = pageParam.getPages();
        long size = pageParam.getSize();
        long total = pageParam.getTotal();
        boolean hasNext = pageParam.hasNext();
        boolean hasPrevious = pageParam.hasPrevious();

        Map<String, Object> map = new HashMap<>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        return map;
    }
}
