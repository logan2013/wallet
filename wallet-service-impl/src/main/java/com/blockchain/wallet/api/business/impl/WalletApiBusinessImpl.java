package com.blockchain.wallet.api.business.impl;

import com.blockchain.wallet.api.WalletApiConstants;
import com.blockchain.wallet.api.WalletApiException;
import com.blockchain.wallet.api.business.WalletApiBusiness;
import com.blockchain.wallet.api.dto.WalletApiRegisterRequestDTO;
import com.blockchain.wallet.api.dto.WalletApiRegisterResponseDTO;
import com.blockchain.wallet.api.dto.WalletHotAccountDTO;
import com.blockchain.wallet.api.exception.BizException;
import com.blockchain.wallet.api.internal.util.*;
import com.blockchain.wallet.api.po.WalletAccountPO;
import com.blockchain.wallet.api.po.WalletHotAccountPO;
import com.blockchain.wallet.api.po.WalletHotPO;
import com.blockchain.wallet.api.request.WalletApiResponse;
import com.blockchain.wallet.api.response.ResponseMessage;
import com.blockchain.wallet.api.service.UsdtServie;
import com.blockchain.wallet.api.service.WalletAccountService;
import com.blockchain.wallet.api.service.WalletHotAccountService;
import com.blockchain.wallet.api.service.WalletHotService;
import com.blockchain.wallet.api.utils.CreateAddress;
import com.blockchain.wallet.api.utils.WalletContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 *  业务层 处理业务逻辑 以及事务相关的
 *   原则上业务层不允许调用本层次的代码
 * @author shujun
 * @date 2018/6/29
 */
@Component
@Slf4j
public class WalletApiBusinessImpl implements WalletApiBusiness {

    @Autowired
    WalletHotService walletHotService;

    @Autowired
    private PlatformTransactionManager ptm;

    @Autowired
    private CreateAddress address;

    @Autowired
    WalletAccountService walletAccountService;

    @Autowired
    WalletHotAccountService walletHotAccountService;

    @Autowired
    private UsdtServie usdtServie;

    /**
     *  考虑使用编程式事务管理
     *  todo : 后期要考虑多种币种的问题，采用异步化初始化的方式实现。
     *  todo :  一期默认生成 BTC 地址
     * @param request
     * @return
     * @throws WalletApiException
     */
    @Override
    public WalletApiResponse<WalletApiRegisterResponseDTO> generateWalletAddress(WalletApiRegisterRequestDTO request)
            throws WalletApiException {

        boolean isExsits = walletHotAccountService.isExsitWalletAddressAccount(request.getAccUserName() , request.getWalletType());
        if(isExsits==true){
            log.warn("{} already register [{}] account address ." , request.getAccUserName() ,request.getWalletType() );
            throw new WalletApiException( 4001 , request.getAccUserName()+" already register "+ request.getWalletType() +" address .");
        }
        KeyExchangeAlgorithmEncrypt local = new KeyExchangeAlgorithmEncrypt(request.getExchageUserPublicKey());
        String scretKey = local.getSecretKey(request.getExchageUserPublicKey(), WalletApiConstants.CHARSET_UTF8);
        String[] keys = KeyTool.generateKey(KeyType.RSA1024);
        // 3. 调用钱包接口生成钱包地址
        // todo ：考虑实现钱包地址数据的异步保存，多地方存储
      //  WalletContainer addr = address.getWalletAddress(request.getWalletType());
        WalletApiRegisterResponseDTO dto = new WalletApiRegisterResponseDTO();
        dto.setUserId(request.getUserId());
        dto.setAccUserName(request.getAccUserName());
        //平台对于账户的公钥 传递给客户端
        dto.setPlatPublicKey(keys[1]);
        // 钱包地址
        //dto.setWalletAddress(addr.getWalletAddress());
        dto.setWalletAddress(address.fetchAddressFromPool());
        dto.setExchageWaPublicKey(local.getPublicKey());

        long timeStamp = System.currentTimeMillis();
        WalletHotAccountPO apo = new WalletHotAccountPO();
        apo.setAppPublicKey(request.getAppPublicKey());
        apo.setWalletPrivateKey(keys[0]);
        apo.setSecretKey(scretKey);
        apo.setUserName(request.getAccUserName());
        apo.setUserId(request.getUserId());
        apo.setCreateTime(timeStamp);
        apo.setExpireState(false);

        WalletHotPO record = new WalletHotPO();
       // BeanUtils.copyProperties(dto , record, new String[]{"exchageWaPublicKey"});
        record.setUserId(request.getUserId());
        record.setUserName(request.getAccUserName());
        record.setAccountType(request.getWalletType());
        record.setBalance(0L);
        record.setFreezBalance(0L);
       // record.setAppPublicKey(request.getAppPublicKey());
        //验签名使用
       // record.setWalletPrivateKey(keys[0]);

        record.setCreateTime(timeStamp);
        record.setExpireState(false);
        record.setUseStatus(true); // 默认: 激活状态
        record.setIsDefaultAccount(true);
        //record.setSecretKey(scretKey);
        record.setWalletAddressHash(dto.getWalletAddress());
       // record.setOrigWalletPrvk(addr.getWalletPrvKey());
       // record.setOrigWalletPuk(addr.getWalletPubKey());
       // record.setOrigWalletWif(addr.getWalletPrivateKeyAsWiF());
        record.setImportFlag(true);
//        try {
//            usdtServie.importPrivkey(addr.getWalletPrivateKeyAsWiF()); //导入私钥
//            record.setImportFlag(true);
//        }catch (Exception e){
//            log.error("import privatekey error {}" , e.getMessage());
//        }
        //record.setImportFlag(false);
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        WalletApiResponse<WalletApiRegisterResponseDTO> resp = WalletApiResponse.success();
        String jsonString = JSON.toJsonString(dto);
        String sign = WalletSignature.rsaSignData(jsonString , WalletApiConstants.CHARSET_UTF8  , request.getSignType());
        WalletAccountPO po = new WalletAccountPO();
        po.setUserName(record.getUserName());
        po.setWalletAddressHash(record.getWalletAddressHash());
        po.setAccountType(record.getAccountType());
        po.setBalance(0L);
        po.setCreateTime(timeStamp);
        po.setExpireState(false);
        po.setFreezBalance(0L);
        po.setUseStatus(true);
        boolean flg = walletAccountService.isExsitAccount(po.getUserName(),po.getWalletAddressHash(),po.getAccountType());
        TransactionStatus status = ptm.getTransaction(def);
        try{
            walletHotAccountService.insertSelective(apo);
            record.setAccountId(apo.getAccountId());
            walletHotService.insertSelective(record);
            if(flg==false){
                walletAccountService.insertWalletAccount(po);
            }
            ptm.commit(status);
            resp.setTimestamp(timeStamp);
            resp.setData(dto);
            resp.setSignType(request.getSignType());
            resp.setSign(sign);
        } catch (Exception e) {
            ptm.rollback(status);
            log.error("db opration error {}" , e.getMessage());
            resp.setCode(ResponseMessage.ERROR.getCode());
            resp.setMessage(ResponseMessage.ERROR.getMessage());
            resp.setTimestamp(timeStamp);
        }
        return resp;
    }

    @Override
    public WalletApiResponse<WalletApiRegisterResponseDTO> restWalletPass(WalletApiRegisterRequestDTO request) throws WalletApiException {
        WalletHotAccountDTO isExsits = walletHotAccountService.selectWalletHotByUserName(request.getAccUserName());
        if(isExsits == null){
            log.warn("username: {} not exsits ..." , request.getAccUserName() );
            throw new WalletApiException( 4001 , " username: "+request.getAccUserName()+" not exsits ... ");
        }

        KeyExchangeAlgorithmEncrypt local = new KeyExchangeAlgorithmEncrypt(request.getExchageUserPublicKey());
        String scretKey = local.getSecretKey(request.getExchageUserPublicKey(), WalletApiConstants.CHARSET_UTF8);
        String[] keys = KeyTool.generateKey(KeyType.RSA1024);

        WalletApiRegisterResponseDTO dto = new WalletApiRegisterResponseDTO();
        dto.setUserId(isExsits.getUserId());
        dto.setAccUserName(request.getAccUserName());
        //平台对于账户的公钥 传递给客户端
        dto.setPlatPublicKey(keys[1]);
        // 钱包地址
        dto.setWalletAddress(address.fetchAddressFromPool());
        dto.setExchageWaPublicKey(local.getPublicKey());

        long timeStamp = System.currentTimeMillis();
        WalletHotAccountPO apo = new WalletHotAccountPO();
        apo.setAppPublicKey(request.getAppPublicKey());
        apo.setWalletPrivateKey(keys[0]);
        apo.setSecretKey(scretKey);
        apo.setUserName(request.getAccUserName());
        apo.setUserId(isExsits.getUserId());
        apo.setCreateTime(timeStamp);
        apo.setAccountId(isExsits.getAccountId());
        walletHotAccountService.updateByPrimaryKeySelective(apo);

        WalletApiResponse<WalletApiRegisterResponseDTO> resp = WalletApiResponse.success();
        String jsonString = JSON.toJsonString(dto);
        String sign = WalletSignature.rsaSignData(jsonString , WalletApiConstants.CHARSET_UTF8  , request.getSignType());
        resp.setTimestamp(timeStamp);
        resp.setData(dto);
        resp.setSignType(request.getSignType());
        resp.setSign(sign);
        return resp;
    }
}
