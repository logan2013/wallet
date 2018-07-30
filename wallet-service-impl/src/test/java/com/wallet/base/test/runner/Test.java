package com.wallet.base.test.runner;

import com.blockchain.wallet.api.WalletApiConstants;
import com.blockchain.wallet.api.WalletApiException;
import com.blockchain.wallet.api.WalletTypeConstants;
import com.blockchain.wallet.api.dto.WalletApiRechargeRequestDTO;
import com.blockchain.wallet.api.internal.util.*;
import com.blockchain.wallet.api.internal.util.codec.Base64;
import com.blockchain.wallet.api.request.WalletCommonApiRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Created by Administrator on 2018/7/16.
 */
@Slf4j
public class Test {


    public static void main(String[] args) throws WalletApiException {

        String[] keys = KeyTool.generateKey(KeyType.RSA1024);
        test(keys[0] , keys[1]);
    }

    static void test(String pk , String pu ) throws WalletApiException {
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
        String content1 = JSON.toJsonString(dto);
        //czr.setRequestContent(content1);
        String kkm =  KeyExchangeAlgorithmEncrypt.encryptContent(content1,"Eu+GJi1eI+6ZdQltDkbV0yZ1FTAx9iSSiV4mLLtmqUA=", WalletApiConstants.CHARSET_UTF8);
        czr.setRequestContent(kkm);

        String content = JSON.toJsonString(czr);
        //  String sign = WalletSignature.rsaSign(content,pk, WalletApiConstants.CHARSET_UTF8);
        log.error("content:{}" , content);
        String sign = WalletSignature.rsaSign(content,pk, WalletApiConstants.CHARSET_UTF8,WalletApiConstants.SIGN_TYPE_RSA);
        log.info("sign : {}" ,sign);
        czr.setSign(sign);









        WalletCommonApiRequest request = JSON.parseObject(JSON.toJsonString(czr) , WalletCommonApiRequest.class);
        request.setSign("");
        String mmw = JSON.toJsonString(request);

       // log.info("{} ",sign.length());
      //  String ccs = new String( Base64.encodeBase64(DigestUtils.sha256(sign)));
       // log.info( ccs);
       // log.info("{} ",ccs.length());
        //boolean a = WalletSignature.rsaCheckContent(content , sign,pu ,WalletApiConstants.CHARSET_UTF8);
        boolean a = WalletSignature.rsaCheck(mmw , sign,pu ,WalletApiConstants.CHARSET_UTF8 , WalletApiConstants.SIGN_TYPE_RSA);
        log.info(" a: {} " , a);
//        // 加密解密
//        String tet =  WalletSignature.rsaEncrypt(content , pu  ,WalletApiConstants.CHARSET_UTF8);
//        log.info(tet);
//        String l =  WalletSignature.rsaDecrypt(tet , pk  ,WalletApiConstants.CHARSET_UTF8 );
//        log.info(l);
    }

}
