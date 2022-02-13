package com.tianle.eduservice.controller.front;

import com.tianle.commonutils.JwtUtils;
import com.tianle.commonutils.R;
import com.tianle.eduservice.client.UserClient;
import com.tianle.eduservice.entity.EduCourse;
import com.tianle.eduservice.entity.EduTeacher;
import com.tianle.eduservice.service.EduCourseService;
import com.tianle.eduservice.service.EduTeacherService;
import com.tianle.servicebase.handler.CustomException;
import io.jsonwebtoken.Jwt;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Tianle
 * @Date: 2022/1/5 17:25
 * @Description: 首页前台控制器
 */
@RestController
@RequestMapping("/eduservice/front-index")
@CrossOrigin
@Api(tags = "首页课程与讲师的显示")
public class FrontIndexController {
    @Autowired
    private EduTeacherService teacherService;

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private UserClient userClient;

    @GetMapping
    public R getIndexData(){
        List<EduTeacher> teacherList = teacherService.getIndexTeacher();
        List<EduCourse> courseList = courseService.getIndexCourse();
        return R.ok().data("teacherList", teacherList).data("courseList", courseList);
    }

    @PostMapping("signIn/{id}")
    @ApiOperation("用户每日签到")
    public R userSignIn(@PathVariable String id){
        // 判断今日是否已签到
        String key = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + id;
        String index = redisTemplate.opsForValue().get(key);
        if (index == null) {
            // 签到添加积分【5,20】
            Integer point = 5 + (int) (Math.random() * (20 - 5));
            userClient.userSignIn(id,point);
            // 添加进redis 每日刷新
            index = UUID.randomUUID().toString().substring(0, 5);
            redisTemplate.opsForValue().set(key, index,1, TimeUnit.DAYS);
            return R.ok().data("point", point);
        } else {
            throw new CustomException(20001,"今日已签到过了");
        }
    }
    @GetMapping("isSignIn")
    @ApiOperation("判断用户今日是否已签到")
    public R isSignIn(HttpServletRequest request){
        String id = JwtUtils.getMemberIdByJwtToken(request);
        if(!StringUtils.hasLength(id)){
            return R.ok().code(30000).message("用户未登录!");
        }
        String key = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + id;
        String index = redisTemplate.opsForValue().get(key);
        if (!StringUtils.hasLength(index)){
            return R.ok().data("isSignIn",false);
        } else {
            return R.ok().data("isSignIn",true);
        }
    }
}
