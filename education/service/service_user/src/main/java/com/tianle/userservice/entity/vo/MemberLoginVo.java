package com.tianle.userservice.entity.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @Author: Tianle
 * @Date: 2022/1/8 14:40
 * @Description: 登录封装实体类
 */
@Data
@ApiModel(value="登录对象", description="登录对象")
public class MemberLoginVo {
    //用户登录手机号
    private String mobile;
    // 用户登录密码
    private String password;
}
