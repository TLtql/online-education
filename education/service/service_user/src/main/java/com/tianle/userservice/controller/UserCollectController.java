package com.tianle.userservice.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tianle.commonutils.JwtUtils;
import com.tianle.commonutils.R;
import com.tianle.servicebase.handler.CustomException;
import com.tianle.userservice.entity.UserCollect;
import com.tianle.userservice.entity.vo.UserCollectDTO;
import com.tianle.userservice.service.UserCollectService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Tianle
 * @since 2022-01-17
 */
@RestController
@RequestMapping("/userservice/user-collect")
@CrossOrigin
@Api(tags = "用户收藏")
public class UserCollectController {
    @Autowired
    private UserCollectService collectService;

    @PostMapping("saveCollect")
    public R saveCollect(@RequestBody UserCollect collect){
        boolean save = collectService.save(collect);
        if (save){
            return R.ok();
        }else {
            return R.error().message("收藏失败");
        }
    }
    @GetMapping("{userId}/{courseId}")
    public R existCollect(@PathVariable String userId,@PathVariable String courseId){
        boolean isCollect = collectService.existUserCollect(userId,courseId);
        return R.ok().data("isCollect",isCollect);
    }

    @GetMapping("getCollect")
    public R pageListCollect(HttpServletRequest request){
        try {
            String userId = JwtUtils.getMemberIdByJwtToken(request);
            if (!StringUtils.hasLength(userId)){
                throw new CustomException(20001,"未登录，请先登录");
            }
            List<UserCollectDTO> collectList = collectService.getUserCollect(userId);
            return R.ok().data("collectList",collectList);
        } catch (Exception e){
            e.printStackTrace();
            throw new CustomException(20001, "获取收藏信息出错了");
        }

    }

    @DeleteMapping("deleteCollect/{id}")
    public R deleteCollect(@PathVariable String id){
        boolean remove = collectService.removeById(id);
        if (remove){
            return R.ok();
        }else {
            return R.error().message("取消收藏失败");
        }
    }

    @DeleteMapping("deleteCollect/{userId}/{courseId}")
    public R deleteCollect(@PathVariable String userId, @PathVariable String courseId){
        collectService.cancelCollect(userId, courseId);
        return R.ok();
    }

}

