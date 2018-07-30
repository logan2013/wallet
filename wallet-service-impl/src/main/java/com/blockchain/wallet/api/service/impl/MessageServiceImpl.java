package com.blockchain.wallet.api.service.impl;

import com.blockchain.wallet.api.Message;
import com.blockchain.wallet.api.pojo.MessageCode;
import com.blockchain.wallet.api.pojo.MessageResult;
import com.blockchain.wallet.api.service.MessageService;
import java.util.Map;

public class MessageServiceImpl implements MessageService {

    @Override
    public MessageResult sendCode(Map<String, Object> params) {
        //获取手机号
        Object phoneNumberObj = params.get("phoneNumber");
        String phoneNumber = null;
        //校验手机号参数是否为空
        if (phoneNumberObj != null) {
            phoneNumber = phoneNumberObj.toString();
        } else {
            return new MessageResult(MessageCode.EORROR.getCode(), MessageCode.EORROR.getMsg(), "手机号不可为空");
        }

        //生成验证码
        String verificationCode = Message.createVerificationCode();
        //对验证码进行参数格式化
        String templateParam = Message.formattemplateParamForRegister(verificationCode);

        //生成短信相关信息:短信签名，短信模板;
        String signature = "\u6D77\u9614\u7F51\u7EDC";
        String template = "SMS_136110013";
        //发送验证码,返回短信发送状态码
        String statusCode = Message.sendMessage(phoneNumber, signature, template, templateParam);

        //存储当前发送时间，用于两次发送时间的比较（不论成功与否都要保存当前发送时间，即时发送失败，一分钟内也不允许再次发送）
        //long lastSendTime = System.currentTimeMillis() / 1000;
        //session.setAttribute("lastSendTime", lastSendTime);

        //根据短信回执状态码，判断是否发送成功
        if (statusCode != null && statusCode.equals("OK")) {

            //返回结果集
            return new MessageResult(MessageCode.OK.getCode(), MessageCode.OK.getMsg(), "注册短信验证码发送成功");
        } else {
            System.out.println("短信发送失败，回执状态码为：" + statusCode);
            return new MessageResult(MessageCode.EORROR.getCode(), MessageCode.EORROR.getMsg(), "注册短信验证码发送失败");
        }
    }

}
