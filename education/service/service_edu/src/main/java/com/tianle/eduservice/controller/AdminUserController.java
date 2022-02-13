package com.tianle.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tianle.commonutils.MD5;
import com.tianle.commonutils.R;
import com.tianle.eduservice.entity.AdminUser;
import com.tianle.eduservice.service.AdminUserService;
import com.tianle.servicebase.handler.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 管理员表 前端控制器
 * </p>
 *
 * @author Tianle
 * @since 2022-02-11
 */
@RestController
@RequestMapping("/eduservice/admin-user")
@CrossOrigin
public class AdminUserController {

    @Autowired
    private AdminUserService userService;

    @GetMapping("getAdminList")
    public R getAdminList(){
        List<AdminUser> adminUsers = userService.list();
        return R.ok().data("list",adminUsers);
    }

    @GetMapping("existUsername/{username}")
    public R existUsername(@PathVariable String username){
        QueryWrapper<AdminUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        AdminUser user = userService.getOne(wrapper);
        if (user == null){
            return R.ok().data("isExist",false);
        } else {
            return R.ok().data("isExist",true);
        }
    }

    @PostMapping("addAdminUser")
    public R addAdminUSer(@RequestBody AdminUser adminUser){
        adminUser.setPassword(MD5.encrypt(adminUser.getPassword()));
        boolean save = userService.save(adminUser);
        if (!save){
            throw new CustomException(20001,"添加管理员失败");
        }
        return R.ok();
    }

    @GetMapping("getUserInfo/{id}")
    public R getUserInfo(@PathVariable String id){
        AdminUser user = userService.getById(id);
        if (user == null) {
            throw new CustomException(20001,"用户不存在");
        }
        return R.ok().data("userInfo",user);
    }

    @PutMapping("updateAdminUser")
    public R updateAdminUser(@RequestBody AdminUser adminUser){
        boolean update = userService.updateById(adminUser);
        if (!update){
            throw new CustomException(20001,"更新管理员信息失败");
        }
        return R.ok();
    }
    @DeleteMapping("removeAdmin/{id}")
    public R removeAdmin(@PathVariable String id){
        boolean remove = userService.removeById(id);
        if (!remove){
            throw new CustomException(20001,"删除管理员失败");
        }
        return R.ok();
    }

}

