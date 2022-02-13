package com.tianle.vodservice.service.impl;

import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import com.tianle.vodservice.service.SmsService;
import org.springframework.stereotype.Service;
import com.tencentcloudapi.common.Credential;

/**
 * @Author: Tianle
 * @Date: 2022/1/7 17:56
 * @Description:
 */
@Service
public class SmsServiceImpl implements SmsService {
    @Override
    public Boolean send(String code, String phone) {
        try {
            String phoneNum = "+86" + phone;
            //实例化一个认证对象，入参需要传入腾讯云账户密钥对secretId，secretKey
            String secretId = "AKIDTklX2FBqFNDm56tQiiRt4zd05vUmnX0C";
            String secretKey = "xjFUS8ZjcEaeRxDCuOcaHhqjKehUzDAq";
            Credential cred  = new Credential(secretId, secretKey);

            //实例化要请求产品(以sms为例)的client对象
            SmsClient client = new SmsClient(cred, "ap-guangzhou");
            // 实例化一个请求对象，根据调用的接口和实际情况，可以进一步设置请求参数
            SendSmsRequest req = new SendSmsRequest();
            /* 短信应用ID: 短信SdkAppId在 [短信控制台] 添加应用后生成的实际SdkAppId，示例如1400006666 */
            String sdkAppId = "1400621135";
            req.setSmsSdkAppId(sdkAppId);

            /* 短信签名内容: 使用 UTF-8 编码，必须填写已审核通过的签名，签名信息可登录 [短信控制台] 查看 */
            String signName = "java学习交流区";
            req.setSignName(signName);

            /* 模板 ID: 必须填写已审核通过的模板 ID。模板ID可登录 [短信控制台] 查看 */
            String templateId = "1270339";
            req.setTemplateId(templateId);

            /* 下发手机号码，采用 E.164 标准，+[国家或地区码][手机号]
             * 示例如：+8613711112222， 其中前面有一个+号 ，86为国家码，13711112222为手机号，最多不要超过200个手机号 */
            String[] phoneNumberSet = {phoneNum};
            req.setPhoneNumberSet(phoneNumberSet);

            /* 模板参数: 若无模板参数，则设置为空 */
            String[] templateParamSet = {code,"2"};
            req.setTemplateParamSet(templateParamSet);

            // 最终发送
            client.SendSms(req);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
