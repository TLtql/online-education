package com.tianle.userservice.controller;

import com.github.wxpay.sdk.WXPayUtil;
import com.google.gson.Gson;
import com.tianle.commonutils.R;
import com.tianle.servicebase.handler.CustomException;
import com.tianle.userservice.utils.CheckWeChatUtil;
import com.tianle.userservice.utils.ConstantPropertiesUtil;
import com.tianle.userservice.utils.HttpClientUtils;
import com.tianle.userservice.utils.LoginCodeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author: Tianle
 * @Date: 2022/2/8 15:27
 * @Description:
 */
@CrossOrigin
@RestController
@RequestMapping("/api/wxChat")
@Api(tags = "微信公众号")
public class WxOfficialController {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;


    @GetMapping("getConnect")
    @ApiOperation("获取公众号连接")
    public String getConnect(HttpServletRequest request){
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        if (CheckWeChatUtil.checkSignature(signature, timestamp, nonce)){
            return echostr;
        } else {
            throw new CustomException(20001,"校验异常");
        }
    }

    @PostMapping("getConnect")
    @ApiOperation("获取信息")
    public String autoResponse(HttpServletRequest request){
        try {
            // request 中的请求体转换为字符串
            String xml = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            Map<String, String> map = WXPayUtil.xmlToMap(xml);
            //从集合中，获取XML各个节点的内容
            String toUserName = map.get("ToUserName");
            String fromUserName = map.get("FromUserName");
            String createTime = map.get("CreateTime");
            String msgType = map.get("MsgType");
            String content = map.get("Content");
            String msgId = map.get("MsgId");
            System.out.println(content);
            Map m = new HashMap<String,String>();
            if ("666".equals(content)){
                m.put("ToUserName",fromUserName);
                m.put("FromUserName",toUserName);
                m.put("CreateTime",createTime);
                m.put("MsgType","text");
                m.put("Content","自动回复测试\n"+"\n您发送的消息类型为："+msgType+"\n您发送的时间为"+createTime
                                +"\n您发送的内容是："+content+"\n发消息者：" + fromUserName);
                String returnContent = WXPayUtil.mapToXml(m);
                return returnContent;
            } else if("登录".equals(content) || "登陆".equals(content)){
                String loginCode = LoginCodeUtil.getCharAndNumr2(8);
                String key ="LoginCode::" + loginCode;
                redisTemplate.opsForValue().set(key,fromUserName,10, TimeUnit.MINUTES);
                m.put("ToUserName",fromUserName);
                m.put("FromUserName",toUserName);
                m.put("CreateTime",createTime);
                m.put("MsgType","text");
                m.put("Content","您正在获取登录动态码\n" + "动态码： " + loginCode +"\n"+"请于10分钟内登录");
                String returnContent = WXPayUtil.mapToXml(m);
                return returnContent;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(20001,"自动回复错误");
        }

    }

    @GetMapping("createMenu")
    @ApiOperation("创建公众号菜单")
    public R createMenu(){
        try {
            // 发送请求获取 Access token
            String baseAccessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token" +
                    "?grant_type=client_credential" +
                    "&appid=%s" +
                    "&secret=%s";

            String accessTokenUrl = String.format(baseAccessTokenUrl,
                    "your appid",
                    "your key");
            String jsonStr = HttpClientUtils.get(accessTokenUrl);
            HashMap<String,String> map = new Gson().fromJson(jsonStr, HashMap.class);
            String access_token = map.get("access_token");

            String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+ access_token;
            String requestBody = "{\"button\":[{\"type\":\"click\",\"name\":\"登录\",\"key\":\"V001_USER_LOGIN\"}}";
            jsonStr = HttpClientUtils.postParameters(url,requestBody);
            HashMap<String,String> map1 = new Gson().fromJson(jsonStr, HashMap.class);
            System.out.println(map1);
            String errmsg = map1.get("errmsg");
            if ("ok".equals(errmsg)){
                return R.ok();
            } else {
                throw new CustomException(20001,errmsg);
            }
        } catch (Exception e){
            e.printStackTrace();
            return R.error();
        }

    }
}
