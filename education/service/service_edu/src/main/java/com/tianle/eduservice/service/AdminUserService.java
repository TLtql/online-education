package com.tianle.eduservice.service;

import com.tianle.eduservice.entity.AdminUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tianle.eduservice.entity.vo.AdminLoginVo;

/**
 * <p>
 * 管理员表 服务类
 * </p>
 *
 * @author Tianle
 * @since 2022-02-11
 */
public interface AdminUserService extends IService<AdminUser> {

    /**
     * 后台管理员登录
     * @param adminLoginVo
     * @return token
     */
    String login(AdminLoginVo adminLoginVo);

}
