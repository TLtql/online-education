package com.tianle.userservice.mapper;

import com.tianle.userservice.entity.UserCollect;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tianle.userservice.entity.vo.UserCollectDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Tianle
 * @since 2022-01-17
 */
public interface UserCollectMapper extends BaseMapper<UserCollect> {

    List<UserCollectDTO> selectListByUserId(String userId);
}
