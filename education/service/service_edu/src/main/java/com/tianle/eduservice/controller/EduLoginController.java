package com.tianle.eduservice.controller;

import com.tianle.commonutils.JwtUtils;
import com.tianle.commonutils.R;
import com.tianle.eduservice.entity.AdminUser;
import com.tianle.eduservice.entity.vo.AdminLoginVo;
import com.tianle.eduservice.service.AdminUserService;
import com.tianle.servicebase.handler.CustomException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Tianle
 * @Date: 2021/11/19 16:57
 * @Description:
 */
@RestController
@RequestMapping("/eduservice/user")
@CrossOrigin
public class EduLoginController {
    @Autowired
    private AdminUserService userService;
    //login
    @PostMapping("login")
    @ApiOperation("登录")
    public R login(@RequestBody AdminLoginVo adminLoginVo){
        String token = userService.login(adminLoginVo);
        return R.ok().data("token",token);
    }

    //info
    @GetMapping("info")
    @ApiOperation("获取管理员信息")
    public R info(HttpServletRequest request){
        String id = JwtUtils.getMemberIdByJwtToken(request);
        AdminUser user = userService.getById(id);
        if (user == null){
            throw new CustomException(20001,"用户不存在");
        }
        List<String> roles = new ArrayList<>();
        roles.add(user.getRoleName());
        return R.ok().data("name",user.getUsername())
                .data("roles",roles)
                .data("avatar",user.getAvatar());
    }


    @PostMapping("logout")
    @ApiOperation("管理员登出")
    public R logout(){
        return R.ok();
    }
}
