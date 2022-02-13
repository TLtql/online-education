package com.tianle.userservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tianle.userservice.entity.UserMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tianle.userservice.entity.vo.MemberLoginVo;
import com.tianle.userservice.entity.vo.MemberRegisterVo;
import com.tianle.userservice.entity.vo.UserQueryVo;

import java.util.List;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author Tianle
 * @since 2022-01-08
 */
public interface UserMemberService extends IService<UserMember> {

    String login(MemberLoginVo member);

    void register(MemberRegisterVo registerVo);

    UserMember getByOpenid(String openid);


    void updatePoint(String id, Integer point);

    /**
     * 分页条件查询用户信息
     * @param userQueryVo
     * @param pageParam
     * @return
     */
    void getUserList(UserQueryVo userQueryVo, Page<UserMember> pageParam);

    /**
     * 修改个人信息
     * @param userInfo
     */
    void updateUserInfoById(UserMember userInfo);

    /**
     * 判断手机号是否存在
     * @param mobile
     * @return
     */
    Boolean hasMobile(String mobile);

    /**
     * 根据ID 修改用户手机号
     * @param id
     * @param mobile
     * @param code
     */
    void updateMobileBYId(String id, String mobile, String code);

    /**
     * 后台 根据id 禁用用户
     * @param id
     * @param time
     * @return
     */
    String banUserById(String id, Long time);

    /**
     * 根据ID 获取用户封禁剩余时间
     * @param id
     * @return
     */
    String getBlockTimeById(String id);

    /**
     * 根据 Id 解封用户
     * @param id
     */
    void relieveUserById(String id);

    /**
     * 删除用户所有信息
     * @param id
     */
    void deleteUserInfoById(String id);

    /**
     * 根据公众号动态码登录
     * @param loginCode
     * @return
     */
    String loginByCode(String loginCode);
}
