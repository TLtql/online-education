package com.tianle.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tianle.commonutils.JwtUtils;
import com.tianle.commonutils.MD5;
import com.tianle.eduservice.entity.AdminUser;
import com.tianle.eduservice.entity.vo.AdminLoginVo;
import com.tianle.eduservice.mapper.AdminUserMapper;
import com.tianle.eduservice.service.AdminUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianle.servicebase.handler.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 管理员表 服务实现类
 * </p>
 *
 * @author Tianle
 * @since 2022-02-11
 */
@Service
public class AdminUserServiceImpl extends ServiceImpl<AdminUserMapper, AdminUser> implements AdminUserService {

    @Override
    public String login(AdminLoginVo adminLoginVo) {
        String username = adminLoginVo.getUsername();
        String password = adminLoginVo.getPassword();
        if (!StringUtils.hasLength(username) || !StringUtils.hasLength(password)){
            throw new CustomException(20001,"用户名或密码为空！");
        }
        QueryWrapper<AdminUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        AdminUser adminUser = baseMapper.selectOne(wrapper);

        if (adminUser == null){
            throw new CustomException(20001,"用户不存在！");
        }
        // 检验密码
        if (!MD5.encrypt(password).equals(adminUser.getPassword())) {
            throw new CustomException(20001, "用户名或密码错误");
        }
        // 密码正确 生成token字符串
        String token = JwtUtils.getJwtToken(adminUser.getId(), adminUser.getUsername());

        return token;
    }
}
