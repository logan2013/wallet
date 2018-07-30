package com.wallet.base.test.runner;

import com.blockchain.wallet.api.WalletApiConstants;
import com.blockchain.wallet.api.WalletApiException;
import com.blockchain.wallet.api.WalletTypeConstants;
import com.blockchain.wallet.api.dto.WalletApiRegisterRequestDTO;
import com.blockchain.wallet.api.internal.util.*;
import com.blockchain.wallet.api.request.WalletCommonApiRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Administrator on 2018/7/16.
 */
@Slf4j
public class RegisterAccount {

    public static void main(String args[]) throws WalletApiException {
        KeyExchangeAlgorithmEncrypt local = new KeyExchangeAlgorithmEncrypt();
        log.info("KeyExchangeAlgorithmEncrypt.privateKey : {} " , local.getPrivateKey());
        log.info("KeyExchangeAlgorithmEncrypt.publicKey : {} " , local.getPublicKey());
        WalletCommonApiRequest request = new WalletCommonApiRequest();
        WalletApiRegisterRequestDTO dto = new WalletApiRegisterRequestDTO();
        dto.setUserId("1234567890");
        dto.setAccUserName("zhangsan");
        dto.setSignType("RSA");
        dto.setWalletType(WalletTypeConstants.WALLET_TYPE_BTC); //BTC
        String[] keys = KeyTool.generateKey(KeyType.RSA1024);
        dto.setWalletType(WalletTypeConstants.WALLET_TYPE_USDT);
        log.info("KeyTool.privateKey : {} " , keys[0]);
        log.info("KeyTool.publicKey : {} " , keys[1]);
        dto.setAppPublicKey(keys[1]);
        dto.setExchageUserPublicKey(local.getPublicKey());
        String secrtKey = JSON.toJSONString(dto);
        log.info(secrtKey);
        secrtKey = WalletSignature.rsaEncrypt(secrtKey , WalletApiConstants.CHARSET_UTF8 );
        log.error(secrtKey);
        request.setRequestContent(secrtKey);
        request.setTimestamp(System.currentTimeMillis());
        System.out.println(JSON.toJSONString(request));
    }

}
