package com.tianle.userservice.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author Tianle
 * @since 2022-01-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="UserOrder对象", description="")
public class UserOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
      @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "课程Id")
    private String courseId;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "支付金额")
    private Long point;

    @ApiModelProperty(value = "支付状态(0:未支付,1:已支付)")
    private Boolean status;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @TableLogic
    @TableField("is_deleted")
    private Boolean deleted;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


}
