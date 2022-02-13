package com.tianle.userservice.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: Tianle
 * @Date: 2022/1/8 15:00
 * @Description: 用户注册封装实体类
 */
@Data
@ApiModel(value="注册对象", description="注册对象")
public class MemberRegisterVo {

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "验证码")
    private String code;
}
