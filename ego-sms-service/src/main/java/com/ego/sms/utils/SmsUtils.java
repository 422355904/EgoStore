package com.ego.sms.utils;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;


/**
 * @Author TheKing
 * @Date 2019/10/10 15:31
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
public class SmsUtils {

    /**
     * 阿里大于短信服务工具
     * @param phone
     * @param code
     */
    public static void sendSms(String phone, String code) {

        /*accessKeyId: LTAI4FeYyiAaQGxwftsR5P4U  # 你自己的accessKeyId
        accessKeySecret: WAbivbKe4igsnKjJJrv5PEXeJhyeBa # 你自己的AccessKeySecret
        signName: 富豪交流 # 签名名称
        templateCode: SMS_175175756 # 模板名称*/

        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI4FeYyiAaQGxwftsR5P4U", "WAbivbKe4igsnKjJJrv5PEXeJhyeBa");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", "富豪交流");
        request.putQueryParameter("TemplateCode", "SMS_175175756");
        request.putQueryParameter("TemplateParam", "{\"code\":\""+code+"\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
