package com.wallet.base.test.runner;

import com.blockchain.wallet.api.WalletApiConstants;
import com.blockchain.wallet.api.WalletApiException;
import com.blockchain.wallet.api.WalletTypeConstants;
import com.blockchain.wallet.api.dto.WalletApiRechargeRequestDTO;
import com.blockchain.wallet.api.internal.util.*;
import com.blockchain.wallet.api.request.WalletCommonApiRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Administrator on 2018/7/16.
 */
@Slf4j
public class RechargeAccount {
    static String publicKey ="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCc/bau1DxQbVxfnetNlfGkYFI/Etx1TODYLMrBAzBbwGhzGsjcQyXmlzNZl5b6a3+xUkt4ZhCG/3k31IuXOZX2pBcypOKLiw0Oiz1IhH7sCqv7+dOqOgiOq/69jr9SolkRJAA9Yp26Guf42B0k4iUc81mZ9m0QQZQ4+0BX62oZxwIDAQAB";
    static String prikey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJz9tq7UPFBtXF+d602V8aRgUj8S3HVM4NgsysEDMFvAaHMayNxDJeaXM1mXlvprf7FSS3hmEIb/eTfUi5c5lfakFzKk4ouLDQ6LPUiEfuwKq/v506o6CI6r/r2Ov1KiWREkAD1inboa5/jYHSTiJRzzWZn2bRBBlDj7QFfrahnHAgMBAAECgYB8Jjv4yAgmhkV9rBHv2jstBaslDBJhhPlumwWAW7g7Zs5y8GSVo1huoXXPTQztXmOt4+mg/f3l9FR1LVVysux+0Fd/BXLvviDGvSHMJUQUQwVDI5Z4/ZUlIfSCTQt72/TSRTMgLoWWz+n3+Fn0949XdztUaYbCgsTEE1f3VuIEOQJBANvsOHUGQTbKBuZSw6kg+FPWXZVk0AXwq9wnl2C3ky95ooGYPCcc+uipVRXin7kGgQahqQRVjn8KvlqAF9kzopsCQQC2vqRkkz5xwg4SKmAmrbj1TomCQTfiztyWbOJaR4rGy/CSs5ys9axdR15Ne88cx3YPfR5KhKFewzKtsTnRhzJFAkEAwj6xWwNsRFVKpNKDSPX7ACm9vL8vUZ/SXACIHv/lu7AGjcqFjtJ091zg7xScmNMa0V2mKFLdKm611L+A0PJfcwJATi88WEdi9PqoWH9GyeWTd2hVjqPmjCQi3jzRjvyC1FDWqX0s8Bbf2ry5DP+GefKggTnvpsQd4lQzL3remNV13QJADjS+HKv80EWtGeHJxlSnu74SXYoS4XEqcPKcai0hxzX2ALzQpu85oeAIPoyoxKrQWVty5SaF+5N98msZ+IX7JA==";

    public static void main(String args[]) throws WalletApiException {
        WalletCommonApiRequest czr = new WalletCommonApiRequest();
        czr.setTimestamp(System.currentTimeMillis());
        czr.setSignType("RSA");
        czr.setUserName("zhangsan");
        WalletApiRechargeRequestDTO dto = new WalletApiRechargeRequestDTO();
        dto.setAbsentFee(0L);
        dto.setOrderNo("order-no1234567890");
        dto.setValue(50L);
        dto.setAccUserName("zhangsan");
        dto.setWalletAddress("14qyp7htJrQmxQiWfKJqpstryuMobKPwVY");
        dto.setWalletType(WalletTypeConstants.WALLET_TYPE_USDT);
        dto.setTimestamp(System.currentTimeMillis());
        String content = JSON.toJsonString(dto);
        //log.info(content);

      //  String[] keys = KeyTool.generateKey(KeyType.RSA1024);
      //  System.out.println(keys[0]);
      //  System.out.println(keys[1]);

        content =  KeyExchangeAlgorithmEncrypt.encryptContent(content,"Eu+GJi1eI+6ZdQltDkbV0yZ1FTAx9iSSiV4mLLtmqUA=", WalletApiConstants.CHARSET_UTF8);
        log.info(content);
        czr.setRequestContent(content);

        String cc = JSON.toJsonString(czr);

        String sign = WalletSignature.rsaSign(cc,prikey, WalletApiConstants.CHARSET_UTF8,WalletApiConstants.SIGN_TYPE_RSA);
        log.warn("sign : {} " , sign);
        czr.setSign(sign);

        String mw = JSON.toJsonString(czr);

        System.err.println(mw);


        WalletCommonApiRequest request = JSON.parseObject(mw , WalletCommonApiRequest.class);

        sign = request.getSign();
        request.setSign("");
         cc= JSON.toJsonString(request);

      //  log.info("cc:{}" , cc);

        boolean a = WalletSignature.rsaCheck(cc , sign,publicKey ,WalletApiConstants.CHARSET_UTF8 , WalletApiConstants.SIGN_TYPE_RSA);

        log.info(a+"");

       // test();

        String  tttt = WalletSignature.rsaEncrypt("123" , WalletApiConstants.CHARSET_UTF8);
        System.out.println(tttt);

       // WalletSignature.rsaDecrypt(tttt , WalletApiConstants.CHARSET_UTF8);

        System.out.println(WalletSignature.rsaDecrypt(tttt , WalletApiConstants.CHARSET_UTF8));

    }


    static void test(){
        int i=0;
        System.out.println("============================");
        if(i==0){
            System.out.println("1111111111111111111111111");
            return;
        }

        System.out.println("abcdefghijklmnopqrstuvwxyz");

    }

}
