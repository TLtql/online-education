package com.tianle.userservice.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tianle.commonutils.JwtUtils;
import com.tianle.commonutils.R;
import com.tianle.servicebase.handler.CustomException;
import com.tianle.userservice.entity.UserMember;
import com.tianle.userservice.entity.vo.MemberLoginVo;
import com.tianle.userservice.entity.vo.MemberRegisterVo;
import com.tianle.userservice.entity.vo.UserQueryVo;
import com.tianle.userservice.service.UserMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author Tianle
 * @since 2022-01-08
 */
@RestController
@RequestMapping("/userservice/user-member")
@CrossOrigin
@Api(tags = "用户登录注册")
public class UserMemberController {

    @Autowired
    private UserMemberService userMember;

    @PostMapping("login")
    @ApiOperation("用户登录")
    public R userLogin(@RequestBody MemberLoginVo member){
        String token = userMember.login(member);
        return R.ok().data("token", token);
    }

    @PostMapping("login/{loginCode}")
    @ApiOperation("公众号动态码登录")
    public R userLogin(@PathVariable String loginCode){
        String token = userMember.loginByCode(loginCode);
        return R.ok().data("token", token);
    }

    @PostMapping("register")
    @ApiOperation("用户注册")
    public R userRegister(@RequestBody MemberRegisterVo registerVo){
        userMember.register(registerVo);
        return R.ok();
    }

    @GetMapping("getUserInfo")
    @ApiOperation(value = "根据token获取登录信息")
    public R getLoginInfo(HttpServletRequest request){
        try {
            String userId = JwtUtils.getMemberIdByJwtToken(request);
            if(!StringUtils.hasLength(userId)){
                return R.ok().code(30000).message("用户未登录!");
            }
            UserMember member = userMember.getById(userId);
            member.setPassword("");
            return R.ok().data("user", member);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(20001,"error");
        }
    }
    @PutMapping("signIn/{id}/{point}")
    @ApiOperation(value = "用户签到添加积分")
    public R userSignIn(@PathVariable String id, @PathVariable Integer point){
        userMember.updatePoint(id, point);
        return R.ok();
    }

    @PostMapping("pageList/{page}/{limit}")
    @ApiOperation("分页条件查询用户")
    public R pageListUser(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,

            @ApiParam(name = "userQueryVo", value = "查询对象", required = false)
            @RequestBody UserQueryVo userQueryVo){
        Page<UserMember> pageParam = new Page<>(page,limit);
        userMember.getUserList(userQueryVo, pageParam);
        return R.ok().data("item", pageParam);
    }

    @PutMapping("updateUserInfo")
    @ApiOperation("修改个人信息")
    public R updateUserInfo(@RequestBody UserMember userInfo){
        userMember.updateUserInfoById(userInfo);
        return R.ok();
    }

    @GetMapping("existMobile/{mobile}")
    @ApiOperation("检验手机号是否被注册")
    public R existMobile(@PathVariable String mobile){
        Boolean isExist = userMember.hasMobile(mobile);
        return R.ok().data("exist", isExist);
    }

    @PutMapping("updateMobile/{mobile}/{code}")
    @ApiOperation("修改绑定手机号")
    public R updateMobile(@PathVariable String mobile, @PathVariable String code, HttpServletRequest request){
        String id = JwtUtils.getMemberIdByJwtToken(request);
        if(!StringUtils.hasLength(id)){
            return R.ok().code(30000).message("用户未登录!");
        }
        userMember.updateMobileBYId(id,mobile,code);
        return R.ok();
    }

    @PutMapping("banUser/{id}/{time}")
    @ApiOperation("根据id禁用用户")
    public R banUser(@PathVariable String id, @PathVariable Long time){
        String returnTime = userMember.banUserById(id, time);
        return R.ok().data("returnTime",returnTime);
    }

    @GetMapping("getBlockTime/{id}")
    @ApiOperation("获取用户封禁时间")
    public R getBlockTime(@PathVariable String id){
        String time = userMember.getBlockTimeById(id);
        return R.ok().data("time", time);
    }

    @PutMapping("relieveUser/{id}")
    @ApiOperation("解封用户")
    public R relieveUser(@PathVariable String id){
        userMember.relieveUserById(id);
        return R.ok();
    }

    @DeleteMapping("removeUser/{id}")
    @ApiOperation("删除用户")
    public R removeUser(@PathVariable String id){
        userMember.deleteUserInfoById(id);
        return R.ok();
    }

    @DeleteMapping("removeUser")
    @ApiOperation("前台注销用户")
    public R deleteUser(HttpServletRequest request){
        String id = JwtUtils.getMemberIdByJwtToken(request);
        if (!StringUtils.hasLength(id)){
            return R.ok().code(30000).message("用户未登录!");
        }
        userMember.deleteUserInfoById(id);
        return R.ok();
    }

}

