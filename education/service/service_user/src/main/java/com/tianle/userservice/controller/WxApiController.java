package com.tianle.userservice.controller;

import com.google.gson.Gson;
import com.tianle.commonutils.JwtUtils;
import com.tianle.servicebase.handler.CustomException;
import com.tianle.userservice.entity.UserMember;
import com.tianle.userservice.service.UserMemberService;
import com.tianle.userservice.utils.BlockUtil;
import com.tianle.userservice.utils.ConstantPropertiesUtil;
import com.tianle.userservice.utils.HttpClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

/**
 * @Author: Tianle
 * @Date: 2022/1/9 18:18
 * @Description: 微信二维码
 */
@CrossOrigin
@Controller
@RequestMapping("/api/user/wx")
public class WxApiController {
    @Autowired
    private UserMemberService memberService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @GetMapping("login")
    public String genQrConnect(HttpSession session) {

        // 微信开放平台授权baseUrl
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        // 回调地址
        String redirectUrl = ConstantPropertiesUtil.WX_OPEN_REDIRECT_URL; //获取业务服务器重定向地址
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "UTF-8"); //url编码
        } catch (UnsupportedEncodingException e) {
            throw new CustomException(20001, e.getMessage());
        }

        // 防止csrf攻击（跨站请求伪造攻击）
        String state = UUID.randomUUID().toString().replaceAll("-", "");//一般情况下会使用一个随机数
        //String state = "imhelen";//为了让大家能够使用我搭建的外网的微信回调跳转服务器，这里填写你在ngrok的前置域名
        //System.out.println("state = " + state);

        // 采用redis等进行缓存state 使用sessionId为key 30分钟后过期，可配置
        //键："wechar-open-state-" + httpServletRequest.getSession().getId()
        //值：satte
        //过期时间：30分钟

        //生成qrcodeUrl
        String qrcodeUrl = String.format(
                baseUrl,
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                redirectUrl,
                state);

        return "redirect:" + qrcodeUrl;
    }

    @GetMapping("callback")
    public String callback(String code, String state){
        String token = null;
        try {
            //判断 state TODO

            //向认证服务器发送请求换取access_token
            String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                    "?appid=%s" +
                    "&secret=%s" +
                    "&code=%s" +
                    "&grant_type=authorization_code";

            String accessTokenUrl = String.format(baseAccessTokenUrl,
                    ConstantPropertiesUtil.WX_OPEN_APP_ID,
                    ConstantPropertiesUtil.WX_OPEN_APP_SECRET,
                    code);
            String result = HttpClientUtils.get(accessTokenUrl);
            //解析json字符串
            Gson gson = new Gson();
            HashMap map = gson.fromJson(result, HashMap.class);
            String accessToken = (String)map.get("access_token");
            String openid = (String)map.get("openid");
            //查询数据库当前用用户是否曾经使用过微信登录
            UserMember member = memberService.getByOpenid(openid);
            if (member == null) {
                //访问微信的资源服务器，获取用户信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                String userInfoUrl = String.format(baseUserInfoUrl, accessToken, openid);
                String resultUserInfo = HttpClientUtils.get(userInfoUrl);

                HashMap<String, Object> mapUserInfo = gson.fromJson(resultUserInfo, HashMap.class);
                String nickname = (String)mapUserInfo.get("nickname");
                String headimgurl = (String)mapUserInfo.get("headimgurl");
                //向数据库中插入一条记录
                member = new UserMember();
                member.setNickname(nickname);
                member.setOpenid(openid);
                member.setAvatar(headimgurl);
                memberService.save(member);

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
            //统计登录数
            String key2 ="EducationStatisticsDaily";
            String loginKey = "loginNumber::" + new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            Boolean hasKey = redisTemplate.boundHashOps(key2).hasKey(loginKey);
            if (hasKey){
                redisTemplate.boundHashOps(key2).increment(loginKey, 1L);
            } else {
                redisTemplate.boundHashOps(key2).put(loginKey,"1");
            }

            token = JwtUtils.getJwtToken(member.getId(), member.getNickname());
        } catch (Exception e) {
            throw new CustomException(20001,"登录失败");
        }
        return "redirect:http://localhost:3000?token=" + token;
    }
}
