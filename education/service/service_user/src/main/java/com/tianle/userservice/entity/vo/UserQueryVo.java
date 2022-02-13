package com.tianle.userservice.entity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Tianle
 * @Date: 2022/1/25 14:55
 * @Description:
 */
@Data
public class UserQueryVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String nickname;

    private String mobile;

    private Integer disabled;
}
