package com.tianle.userservice.entity.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: Tianle
 * @Date: 2022/1/17 14:54
 * @Description: 用户中心 订单列表展示
 */
@Data
public class UserOrderDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String courseId;

    private String title;

    private String cover;

    private String teacherName;

    private Long point;

    private Boolean status;

    private Date gmtCreate;

}
