package com.tianle.eduservice.entity.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Tianle
 * @Date: 2022/2/11 15:45
 * @Description: 管理员登录 Vo
 */
@Data
@ApiModel(value="登录对象", description="登录对象")
public class AdminLoginVo implements Serializable {

    private static final long serialVersionUID = 1L;
    //登录用户名
    private String username;
    // 登录密码
    private String password;
}
