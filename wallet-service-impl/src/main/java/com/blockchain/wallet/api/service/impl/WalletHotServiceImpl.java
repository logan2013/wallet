package com.blockchain.wallet.api.service.impl;

import com.blockchain.wallet.api.WalletApiConstants;
import com.blockchain.wallet.api.WalletApiException;
import com.blockchain.wallet.api.WalletTypeConstants;
import com.blockchain.wallet.api.dao.WalletHotPOMapper;
import com.blockchain.wallet.api.dto.WalletApiRegisterRequestDTO;
import com.blockchain.wallet.api.internal.util.*;
import com.blockchain.wallet.api.po.WalletHotPO;
import com.blockchain.wallet.api.request.WalletCommonApiRequest;
import com.blockchain.wallet.api.service.WalletHotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author shujun
 * @date 2018/6/28
 */

@Component
@Slf4j
public class WalletHotServiceImpl implements WalletHotService {

    @Autowired
    WalletHotPOMapper mapper;

    @Override
    public int insertSelective(WalletHotPO po) {
       return mapper.insertSelective(po);
    }

    @Override
    public boolean isExsitWalletHot(String userName, String walletAddress, String walletType) {
        WalletHotPO p = new WalletHotPO();
        p.setUserName(userName);
        p.setWalletAddressHash(walletAddress);
        p.setAccountType(walletType);
       int i = mapper.selectWalletHotByPo(p);
       if(i>0){
           return true;
       }
       return false;
    }

//    /**
//     *  考虑加上缓存
//     * @param addressHash
//     * @return
//     */
//    @Override
//    public WalletHotPO selectWalletHotByAddr(String addressHash) {
//        return mapper.selectWalletHotByAddr(addressHash);
//    }
//
//    @Override
//    public WalletHotPO selectWalletHotByUserName(String username) {
//        return null;
//    }

    public static void main(String args[]) throws WalletApiException {
        KeyExchangeAlgorithmEncrypt local = new KeyExchangeAlgorithmEncrypt();
        log.info("privateKey : {} " , local.getPrivateKey());
        log.info("publicKey : {} " , local.getPublicKey());
        WalletCommonApiRequest request = new WalletCommonApiRequest();
        WalletApiRegisterRequestDTO dto = new WalletApiRegisterRequestDTO();
        dto.setUserId("1234567890");
        dto.setAccUserName("zhangsan");
        dto.setSignType("RSA");
        dto.setWalletType(WalletTypeConstants.WALLET_TYPE_BTC);
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
