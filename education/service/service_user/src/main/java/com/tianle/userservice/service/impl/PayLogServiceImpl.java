package com.tianle.userservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.wxpay.sdk.WXPayUtil;
import com.tianle.servicebase.handler.CustomException;
import com.tianle.userservice.entity.PayLog;
import com.tianle.userservice.entity.UserMember;
import com.tianle.userservice.mapper.PayLogMapper;
import com.tianle.userservice.service.PayLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianle.userservice.service.UserMemberService;
import com.tianle.userservice.service.UserOrderService;
import com.tianle.userservice.utils.HttpClient;
import com.tianle.userservice.utils.OrderNoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author Tianle
 * @since 2022-01-14
 */
@Service
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements PayLogService {

    @Autowired
    private UserMemberService userMemberService;
    @Override
    public Map createNative(String id, BigDecimal price) {
        UserMember user = userMemberService.getById(id);
        String orderNo = OrderNoUtil.getOrderNo();

        Map m = new HashMap();
        //1、设置支付参数
        m.put("appid", "wx74862e0dfcf69954");
        m.put("mch_id", "1558950191"); // 商户号
        m.put("nonce_str", WXPayUtil.generateNonceStr()); //生成随机的字符串，让每次生成的二维码不一样
        m.put("body", price.movePointRight(4).intValue()+"积分"); // 设置二维码的名字
        m.put("out_trade_no", orderNo); //二维码唯一的标识
        m.put("total_fee", price.multiply(new BigDecimal("100")).longValue()+"");
        m.put("spbill_create_ip", "127.0.0.1"); ;//现在进行支付的ip地址，实际项目使用项目的域名
        m.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify");//支付后回调地址
        m.put("trade_type", "NATIVE");

        try{
            //2、HTTPClient来根据URL访问第三方接口并且传递参数
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");

            //client设置参数
            client.setXmlParam(WXPayUtil.generateSignedXml(m, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            client.post();

            //3、返回第三方的数据
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            //4、封装返回结果集

            Map map = new HashMap<>();
            map.put("out_trade_no", orderNo);
            map.put("user_id", user.getId());
            map.put("total_fee", price);
            map.put("result_code", resultMap.get("result_code"));
            map.put("code_url", resultMap.get("code_url"));

            //微信支付二维码2小时过期，可采取2小时未支付取消订单
            //redisTemplate.opsForValue().set(orderNo, map, 120, TimeUnit.MINUTES);

            return map;
        }catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    @Override
    public Map<String, String> queryPayStatus(String orderNo) {
        try {
            //1、封装参数
            Map m = new HashMap<>();
            m.put("appid", "wx74862e0dfcf69954");
            m.put("mch_id", "1558950191");
            m.put("out_trade_no", orderNo);
            m.put("nonce_str", WXPayUtil.generateNonceStr());

            //2、设置请求
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(WXPayUtil.generateSignedXml(m, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            client.post();
            //3、返回第三方的数据
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            //6、转成Map
            //7、返回
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void savePayLog(Map<String, String> map, String id) {
        int point = new BigDecimal(map.get("total_fee")).movePointRight(2).intValue();
        // 添加用户积分
        userMemberService.updatePoint(id,point);
        //记录支付日志
        PayLog payLog=new PayLog();
        payLog.setOrderNo(map.get("out_trade_no"));//支付订单号
        payLog.setPayTime(new Date());
        payLog.setPayType(1);//支付类型
        payLog.setTotalFee(new BigDecimal(map.get("total_fee")));//总金额(分)
        payLog.setTradeState(map.get("trade_state"));//支付状态
        payLog.setTransactionId(map.get("transaction_id"));
        payLog.setAttr(JSONObject.toJSONString(map));
        int insert = baseMapper.insert(payLog);//插入到支付日志表
        if (insert == 0){
            throw new CustomException(20001,"生成订单日志失败");
        }
    }
}
