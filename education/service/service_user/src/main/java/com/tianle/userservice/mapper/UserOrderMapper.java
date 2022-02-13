package com.tianle.userservice.mapper;

import com.tianle.userservice.entity.UserOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tianle.userservice.entity.vo.UserOrderDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Tianle
 * @since 2022-01-13
 */
public interface UserOrderMapper extends BaseMapper<UserOrder> {

    List<UserOrderDTO> selectListByUserId(String userId);
}
