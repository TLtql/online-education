package com.tianle.servicebase.handler;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Tianle
 * @Date: 2021/10/31 14:57
 * @Description: 自定义异常类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomException extends RuntimeException{

    @ApiModelProperty(value = "状态码")
    private Integer code;

    private String msg;
}
