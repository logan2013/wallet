package com.blockchain.wallet.api;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

import java.util.Random;

public class Message {
    /**
     * 阿里云授权key
     */
    public static final String AccessKeyId = "LTAIe8lX2SzGiJQd";
    public static final String AccessKeySecret = "3oJFFNk8ZayIlKDpO1gIDKuZWh5lYd";

    /**
     * 用于审核通过短信模板中参数的格式化（转换为json）
     */
    public static String formattemplateParamForApproved(String name, String date, String site) {
        String templateParam = "{\"name\":\"" + name + "\",\"date\":\"" + date + "\",\"sites\":\"" + site + "\"}";
        return templateParam;
    }

    /**
     * 用于注册短信模板中验证码参数的格式化（转换为json）
     */
    public static String formattemplateParamForRegister(String verificationCode) {
        String templateParam = "{\"code\":\"" + verificationCode + "\"}";
        return templateParam;
    }

    /**
     * 用于发送短信的工具类
     *
     * @param phoneNumber   接收短信的手机号
     * @param signature     要使用的短信签名
     * @param template      短信模板ID
     * @param templateParam 模板参数
     * @return
     */
    public static String sendMessage(String phoneNumber, String signature, String template, String templateParam) {
        String sendSmsResponseCode = null;
        try {//设置超时时间-可自行调整
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
            System.setProperty("sun.net.client.defaultReadTimeout", "10000");
            //初始化ascClient需要的几个参数
            final String product = "Dysmsapi";//短信API产品名称（短信产品名固定，无需修改）
            final String domain = "dysmsapi.aliyuncs.com";//短信API产品域名（接口地址固定，无需修改）
            //替换成你的AK
            //你的accessKeyId,参考本文档步骤2
            final String accessKeyId = AccessKeyId;
            //你的accessKeySecret，参考本文档步骤2
            final String accessKeySecret = AccessKeySecret;
            //初始化ascClient,暂时不支持多region（请勿修改）
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId,
                    accessKeySecret);
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);

            IAcsClient acsClient = new DefaultAcsClient(profile);
            //组装请求对象
            SendSmsRequest request = new SendSmsRequest();
            //使用post提交
            request.setMethod(MethodType.POST);
            //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
            request.setPhoneNumbers(phoneNumber);
            //必填:短信签名-可在短信控制台中找到
            request.setSignName(signature);
            //必填:短信模板-可在短信控制台中找到
            request.setTemplateCode(template);
            //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
            //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
            request.setTemplateParam(templateParam);
            //可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
            //request.setSmsUpExtendCode("90997");
            //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
            request.setOutId("yourOutId");
            //请求失败这里会抛ClientException异常
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            sendSmsResponseCode = sendSmsResponse.getCode();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return sendSmsResponseCode;
    }

    /**
     * 生成验证码
     */
    public static String createVerificationCode() {
        //随机验证码
        String verificationCode = "";
        //从0-9数组中选取六位验证码
        int[] intArr = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0};

        //生成六个随机数，分别从数组中取六次，拼接为随机验证码
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            //因为数组的长度最大为9，所以随机数最大取到9
            int randomInt = random.nextInt(10);
            int element = intArr[randomInt];
            //拼接验证码
            verificationCode += element;
        }
        System.out.println(verificationCode);
        return verificationCode;
    }

}
