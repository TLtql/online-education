package com.tianle.userservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tianle.commonutils.JwtUtils;
import com.tianle.commonutils.MD5;
import com.tianle.servicebase.handler.CustomException;
import com.tianle.userservice.client.EduServiceClient;
import com.tianle.userservice.entity.UserCollect;
import com.tianle.userservice.entity.UserMember;
import com.tianle.userservice.entity.UserOrder;
import com.tianle.userservice.entity.vo.MemberLoginVo;
import com.tianle.userservice.entity.vo.MemberRegisterVo;
import com.tianle.userservice.entity.vo.UserQueryVo;
import com.tianle.userservice.mapper.UserMemberMapper;
import com.tianle.userservice.service.UserCollectService;
import com.tianle.userservice.service.UserMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianle.userservice.service.UserOrderService;
import com.tianle.userservice.utils.BlockUtil;
import org.apache.catalina.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author Tianle
 * @since 2022-01-08
 */
@Service
public class UserMemberServiceImpl extends ServiceImpl<UserMemberMapper, UserMember> implements UserMemberService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private UserCollectService collectService;

    @Autowired
    private UserOrderService orderService;

    @Autowired
    private EduServiceClient eduServiceClient;

    @Override
    public String login(MemberLoginVo member) {
        String mobile = member.getMobile();
        String password = member.getPassword();
        // 校验数据
        if (!StringUtils.hasLength(mobile) || !StringUtils.hasLength(password)){
            throw new CustomException(20001, "登录失败");
        }
        // 根据mobile 查询数据
        QueryWrapper<UserMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        UserMember userMember = baseMapper.selectOne(wrapper);
        // 判断用户是否存在
        if (userMember == null) {
            throw new CustomException(20001, "用户不存在！");
        }
        // 判断密码是否正确
        if (!MD5.encrypt(password).equals(userMember.getPassword())) {
            throw new CustomException(20001, "用户名或密码错误");
        }
        // 检验用户是否被禁用
        if (userMember.getDisabled() == -1) {
            throw new CustomException(20001, "您已被永久封禁");
        }
        if (userMember.getDisabled() == 1) {
            String key = "block::" + userMember.getId();
            String value = redisTemplate.opsForValue().get(key);
            // value 不存在（未封禁或已过期）
            if (!StringUtils.hasLength(value)){
                // 刷新用户的禁用状态
                BlockUtil.updateOneBlockStatus(userMember.getId());
            } else {  // value 存在,用户封禁中
                Long time = redisTemplate.opsForValue().getOperations().getExpire(key);
                String dateTime = BlockUtil.formatDateTime(time);
                throw new CustomException(20001,"您已被封禁，" + dateTime + " 后解封");
            }


        }
        // 生成token 字符串
        String token = JwtUtils.getJwtToken(userMember.getId(), userMember.getNickname());

        //统计登录数
        String key ="EducationStatisticsDaily";
        String loginKey = "loginNumber::" + new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Boolean hasKey = redisTemplate.boundHashOps(key).hasKey(loginKey);
        if (hasKey){
            redisTemplate.boundHashOps(key).increment(loginKey, 1L);
        } else {
            redisTemplate.boundHashOps(key).put(loginKey,"1");
        }

        return token;
    }

    @Override
    public void register(MemberRegisterVo registerVo) {
        String password = registerVo.getPassword();
        String mobile = registerVo.getMobile();
        String nickname = registerVo.getNickname();
        String code = registerVo.getCode();

        // 判断非空
        if (!StringUtils.hasLength(mobile) || !StringUtils.hasLength(password) ||
               !StringUtils.hasLength(nickname) || !StringUtils.hasLength(code)) {
            throw new CustomException(20001, "注册失败");
        }
        // 校验验证码
        String mobileCode = redisTemplate.opsForValue().get(mobile);
        if (!StringUtils.hasLength(mobileCode)) {
            throw new CustomException(20001,"验证码不存在或已失效");
        }
        if (!code.equals(mobileCode)) {
            throw new CustomException(20001, "验证码不正确");
        }
        // 查询手机号是否存在
        QueryWrapper<UserMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        Integer count = baseMapper.selectCount(wrapper);
        if (count > 0){
            throw new CustomException(20001, "手机号已被注册");
        }
        // 判断 nickname 是否重复
        // TODO

        UserMember userMember = new UserMember();
        BeanUtils.copyProperties(registerVo,userMember);
        userMember.setPassword(MD5.encrypt(password));
        userMember.setAvatar("https://edu-tianle.oss-cn-beijing.aliyuncs.com/edu/user/default.png");
        int insert = baseMapper.insert(userMember);

        //统计注册数
        String key ="EducationStatisticsDaily";
        String register = "registerNumber::" + new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Boolean hasKey = redisTemplate.boundHashOps(key).hasKey(register);
        if (hasKey){
            redisTemplate.boundHashOps(key).increment(register, 1L);
        } else {
            redisTemplate.boundHashOps(key).put(register,"1");
        }

        if (insert == 0) {
            throw new CustomException(20001,"注册失败2");
        }
    }

    @Override
    public UserMember getByOpenid(String openid) {
        QueryWrapper<UserMember> wrapper = new QueryWrapper<>();
        wrapper.eq("openid",openid);
        UserMember member = baseMapper.selectOne(wrapper);
        return member;
    }

    @Override
    public void updatePoint(String id, Integer point) {
        UserMember userMember = baseMapper.selectById(id);
        userMember.setPoint(userMember.getPoint() + point);
        int update = baseMapper.updateById(userMember);
        if (update == 0) {
            throw new CustomException(20001, "添加积分失败");
        }
    }

    @Override
    public void getUserList(UserQueryVo userQueryVo, Page<UserMember> pageParam) {
        // 查询之前先刷新用户的禁用状态
        BlockUtil.updateAllBlockStatus();

        QueryWrapper<UserMember> wrapper = new QueryWrapper<>();
        if (userQueryVo == null) {
            baseMapper.selectPage(pageParam,null);
            return;
        }
        if (StringUtils.hasLength(userQueryVo.getId())){
            wrapper.eq("id",userQueryVo.getId());
        }
        if (StringUtils.hasLength(userQueryVo.getNickname())){
            wrapper.like("nickname",userQueryVo.getNickname());
        }
        if (userQueryVo.getDisabled() != null){
            if (userQueryVo.getDisabled() != 0){
                wrapper.ne("is_disabled",0);
            } else {
                wrapper.eq("is_disabled", 0);
            }
        }
        if (StringUtils.hasLength(userQueryVo.getMobile())){
            wrapper.eq("mobile", userQueryVo.getMobile());
        }

        baseMapper.selectPage(pageParam, wrapper);
    }

    @Override
    public void updateUserInfoById(UserMember userInfo) {
        UserMember userMember = baseMapper.selectById(userInfo.getId());
        userInfo.setPassword(userMember.getPassword());
        int update = baseMapper.updateById(userInfo);
        if (update == 0){
            throw new CustomException(20001,"修改信息失败！");
        }
    }

    @Override
    public Boolean hasMobile(String mobile) {
        QueryWrapper<UserMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        Integer count = baseMapper.selectCount(wrapper);
        if (count > 0){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void updateMobileBYId(String id, String mobile, String code) {
        UserMember userMember = baseMapper.selectById(id);

        // 判断非空
        if (!StringUtils.hasLength(mobile) || !StringUtils.hasLength(code) || userMember == null) {
            throw new CustomException(20001, "数据不存在");
        }
        // 校验验证码
        String mobileCode = redisTemplate.opsForValue().get(mobile);

        if (!StringUtils.hasLength(mobileCode)) {
            throw new CustomException(20001,"验证码不存在或已失效");
        }
        if (!code.equals(mobileCode)) {
            throw new CustomException(20001, "验证码不正确");
        }
        // 查询手机号是否存在
        QueryWrapper<UserMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        Integer count = baseMapper.selectCount(wrapper);
        if (count > 0){
            throw new CustomException(20001, "手机号已被注册");
        }

        userMember.setMobile(mobile);
        int update = baseMapper.updateById(userMember);
        if (update == 0){
            throw new CustomException(20001,"修改失败");
        }

    }

    @Override
    public String banUserById(String id, Long time) {
        UserMember user = baseMapper.selectById(id);
        // 永久封禁
        if (time == -1){
            // 设置状态为 永久封禁（-1）
            user.setDisabled(-1);
            baseMapper.updateById(user);
            return "-1";
        } else { // 暂时封禁
            String key = "block::" + id;
            String index = UUID.randomUUID().toString().substring(0, 5);
            // 添加进redis 并设置过期时间为 封禁时间
            redisTemplate.opsForValue().set(key, index, time, TimeUnit.SECONDS);
            // 设置状态为 暂时封禁（1）
            user.setDisabled(1);
            baseMapper.updateById(user);
            return BlockUtil.formatDateTime(time - 1);
        }

    }

    @Override
    public String getBlockTimeById(String id) {
        String key = "block::" + id;
        // 没设置过期时间返回-1，没有该值返回-2
        Long time = redisTemplate.opsForValue().getOperations().getExpire(key);
        if (time == -1 || time == -2){
            throw new CustomException(20001,"用户已解封或数据异常");
        }
        String dateTime = BlockUtil.formatDateTime(time);

        return dateTime;
    }

    @Override
    public void relieveUserById(String id) {
        UserMember user = baseMapper.selectById(id);
        if (user.getDisabled() == 0){
            throw new CustomException(20001,"用户未被禁用");
        } else if (user.getDisabled() == -1){ //永久封禁的
            user.setDisabled(0);
            int update = baseMapper.updateById(user);
            if (update == 0){
                throw new CustomException(20001,"解封失败");
            }
        } else if (user.getDisabled() == 1){ // 暂时封禁的
            String key = "block::" + id;
            // 删除redis 中的信息
            Boolean delete = redisTemplate.delete(key);
            // 更新user状态
            user.setDisabled(0);
            int update = baseMapper.updateById(user);
            if (!delete || update == 0){
                throw new CustomException(20001,"解封失败");
            }
        }
    }

    @Override
    public void deleteUserInfoById(String id) {
        //删除用户评论
        try {
            eduServiceClient.deleteCommentByUser(id);
        } catch (Exception e){
            throw new CustomException(20001, "删除评论失败");
        }
        //删除用户收藏
        QueryWrapper<UserCollect> collectWrapper = new QueryWrapper<>();
        collectWrapper.eq("user_id", id);
        boolean remove = collectService.remove(collectWrapper);
        // 删除用户订单
        QueryWrapper<UserOrder> orderWrapper = new QueryWrapper<>();
        orderWrapper.eq("user_id", id);
        boolean remove1 = orderService.remove(orderWrapper);
        // 删除用户信息
        int delete = baseMapper.deleteById(id);
        if (delete == 0){
            throw new CustomException(20001, "删除用户信息失败");
        }

    }

    @Override
    public String loginByCode(String loginCode) {
        if (!StringUtils.hasLength(loginCode)){
            throw new CustomException(20001, "请输入动态码");
        }
        String key = "LoginCode::" + loginCode;
        String openid = redisTemplate.opsForValue().get(key);
        if (!StringUtils.hasLength(openid)){
            throw new CustomException(20001,"动态码不存在或已过期");
        }
        // 判断是否为第一次登录
        QueryWrapper<UserMember> wrapper = new QueryWrapper<>();
        wrapper.eq("openid",openid);
        UserMember member = baseMapper.selectOne(wrapper);
        if (member == null){
            member = new UserMember();
            member.setNickname("Student"+new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            member.setPoint(20L);
            member.setAvatar("https://edu-tianle.oss-cn-beijing.aliyuncs.com/edu/user/default.png");
            member.setOpenid(openid);
            baseMapper.insert(member);

            //统计注册数
            String key1 ="EducationStatisticsDaily";
            String register = "registerNumber::" + new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            Boolean hasKey = redisTemplate.boundHashOps(key1).hasKey(register);
            if (hasKey){
                redisTemplate.boundHashOps(key1).increment(register, 1L);
            } else {
                redisTemplate.boundHashOps(key1).put(register,"1");
            }

        } else {
            // 检验用户是否被禁用
            if (member.getDisabled() == -1) {
                throw new CustomException(20001, "您已被永久封禁");
            }
            if (member.getDisabled() == 1) {
                String key1 = "block::" + member.getId();
                String value = redisTemplate.opsForValue().get(key1);
                // value 不存在（未封禁或已过期）
                if (!StringUtils.hasLength(value)){
                    // 刷新用户的禁用状态
                    BlockUtil.updateOneBlockStatus(member.getId());
                } else {  // value 存在,用户封禁中
                    Long time = redisTemplate.opsForValue().getOperations().getExpire(key1);
                    String dateTime = BlockUtil.formatDateTime(time);
                    throw new CustomException(20001,"您已被封禁，" + dateTime + " 后解封");
                }
            }
        }

        String token = JwtUtils.getJwtToken(member.getId(), member.getNickname());

        //统计登录数
        String key2 ="EducationStatisticsDaily";
        String loginKey = "loginNumber::" + new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Boolean hasKey = redisTemplate.boundHashOps(key2).hasKey(loginKey);
        if (hasKey){
            redisTemplate.boundHashOps(key2).increment(loginKey, 1L);
        } else {
            redisTemplate.boundHashOps(key2).put(loginKey,"1");
        }

        return token;
    }


}
