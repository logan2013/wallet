package com.blockchain.wallet.api.consumer;

import com.alibaba.dubbo.config.annotation.Reference;
import com.blockchain.wallet.api.WalletApiConstants;
import com.blockchain.wallet.api.WalletApiException;
import com.blockchain.wallet.api.dto.*;
import com.blockchain.wallet.api.exception.ParamterValidateException;
import com.blockchain.wallet.api.internal.util.JSON;
import com.blockchain.wallet.api.internal.util.KeyExchangeAlgorithmEncrypt;
import com.blockchain.wallet.api.internal.util.WalletSignature;
import com.blockchain.wallet.api.request.WalletApiResponse;
import com.blockchain.wallet.api.request.WalletCommonApiRequest;
import com.blockchain.wallet.api.request.WalletRsaResponse;
import com.blockchain.wallet.api.service.RabbitMessageService;
import com.blockchain.wallet.api.service.WalletApiService;
import com.blockchain.wallet.api.service.WalletHotAccountProviderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author shujun
 * @date 2018/6/27
 */
@Component
@Slf4j
public class WalletApiConsumer {

    @Reference(timeout = 5000, loadbalance="leastactive" , cluster="failfast")
    private WalletApiService walletApiService;

    @Reference(timeout = 5000, loadbalance="leastactive" , retries = 2)
    private WalletHotAccountProviderService walletHotProvider;

    @Reference(timeout = 5000, loadbalance="leastactive" , retries = 2)
    private RabbitMessageService rabbitMessageService;

    public WalletApiResponse<WalletApiRegisterResponseDTO> generateWalletAddress(WalletCommonApiRequest request) throws WalletApiException {
        String content = null;
        try{
            content = WalletSignature.rsaDecrypt(request.getRequestContent(), WalletApiConstants.CHARSET_UTF8 );
            WalletApiRegisterRequestDTO dto = JSON.parseObject(content, WalletApiRegisterRequestDTO.class);
            if(dto.check() == false){
                log.error("invalid paramter : {} " , dto);
                throw new WalletApiException(400,"invalid paramters ");
            }
            dto.setSignType(request.getSignType());
            WalletApiResponse<WalletApiRegisterResponseDTO> resp = walletApiService.generateWalletAddress(dto);
            return resp;
        }catch (WalletApiException e){
            log.error(e.getMessage());
            if(e.getErrCode()==4001){
                throw e;
            }
            throw new ParamterValidateException(400,"rsaDecrypt fail.");
        }catch (JSON.JSONException e){
            log.error("invalid paramter : {} " , content);
            throw new ParamterValidateException(400,"invalid paramters ");
        }
    }

    /**
     *  在线充值
     * @param request
     * @return
     * @throws WalletApiException
     */
    public WalletRsaResponse rechargeOnLine(WalletCommonApiRequest request) throws WalletApiException  {
        String content = null;
        WalletHotAccountDTO account = walletHotProvider.selectWalletHotByUserName(request.getUserName());
        try{
            String sign = request.getSign();
            request.setSign("");
            String cc= JSON.toJsonString(request);
            // 公钥验签名
            boolean a = WalletSignature.rsaCheck(cc , sign,account.getAppPublicKey() ,WalletApiConstants.CHARSET_UTF8 , request.getSignType());
            if(a == false){
                throw new WalletApiException(400," invalid paramters ");
            }
            content =  KeyExchangeAlgorithmEncrypt.decryptContent(request.getRequestContent(),account.getSecretKey(),WalletApiConstants.CHARSET_UTF8);
            WalletApiRechargeRequestDTO dto = JSON.parseObject(content, WalletApiRechargeRequestDTO.class);
            if(dto.check() == false){
                log.error("invalid paramter : {} " , dto);
                throw new WalletApiException(400,"invalid paramters ");
            }
            dto.setSignType(request.getSignType());
            WalletRsaResponse resp = walletApiService.rechargeOnLine(dto);
            String jsonString = KeyExchangeAlgorithmEncrypt.encryptContent(resp.getBizContent(),account.getSecretKey(),WalletApiConstants.CHARSET_UTF8);
            resp.setBizContent(jsonString);
            String sign2 = WalletSignature.rsaSign(JSON.toJsonString(resp) , account.getWalletPrivateKey(), WalletApiConstants.CHARSET_UTF8, request.getSignType());
            resp.setSign(sign2);
            return resp;
        }catch (WalletApiException e){
            log.error(e.getMessage());
            throw new ParamterValidateException(400,"invalid paramters.");
        }catch (JSON.JSONException e){
            log.error("invalid paramter : {} " , content);
            throw new WalletApiException(400,"invalid paramters ");
        }
    }


    public WalletRsaResponse cashTrade( WalletCommonApiRequest request) throws WalletApiException {
        String content = null;
        WalletHotAccountDTO account = walletHotProvider.selectWalletHotByUserName(request.getUserName());
        try{
            String sign = request.getSign();
            request.setSign("");
            String cc= JSON.toJsonString(request);
            // 公钥验签名
            boolean a = WalletSignature.rsaCheck(cc , sign,account.getAppPublicKey() ,WalletApiConstants.CHARSET_UTF8 , request.getSignType());
            if(a == false){
                throw new WalletApiException(400," invalid paramters ");
            }
            content =  KeyExchangeAlgorithmEncrypt.decryptContent(request.getRequestContent(),account.getSecretKey(),WalletApiConstants.CHARSET_UTF8);
            WalletApiCashTradeRequestDTO dto = JSON.parseObject(content, WalletApiCashTradeRequestDTO.class);
            if(dto.check() == false){
                log.error("invalid paramter : {} " , dto);
                throw new WalletApiException(400,"invalid paramters ");
            }
            dto.setSignType(request.getSignType());
            WalletRsaResponse resp = walletApiService.cashTrade(dto);
            String jsonString = KeyExchangeAlgorithmEncrypt.encryptContent(resp.getBizContent(),account.getSecretKey(),WalletApiConstants.CHARSET_UTF8);
            resp.setBizContent(jsonString);
            String sign2 = WalletSignature.rsaSign(JSON.toJsonString(resp) , account.getWalletPrivateKey(), WalletApiConstants.CHARSET_UTF8, request.getSignType());
            resp.setSign(sign2);
            return resp;
        }catch (WalletApiException e){
            log.error(e.getMessage());
            throw new ParamterValidateException(400,"invalid paramters.");
        }catch (JSON.JSONException e){
            log.error("invalid paramter : {} " , content);
            throw new WalletApiException(400,"invalid paramters ");
        }
    }


    /**
     *  提现申请
     * @param request
     * @return
     * @throws WalletApiException
     */
    public WalletRsaResponse withdraw( WalletCommonApiRequest request) throws WalletApiException{
        String content = null;
        WalletHotAccountDTO account = walletHotProvider.selectWalletHotByUserName(request.getUserName());
        try{
            String sign = request.getSign();
            request.setSign("");
            String cc= JSON.toJsonString(request);
            // 公钥验签名
            boolean a = WalletSignature.rsaCheck(cc , sign,account.getAppPublicKey() ,WalletApiConstants.CHARSET_UTF8 , request.getSignType());
            if(a == false){
                throw new WalletApiException(400," invalid paramters ");
            }
            content =  KeyExchangeAlgorithmEncrypt.decryptContent(request.getRequestContent(),account.getSecretKey(),WalletApiConstants.CHARSET_UTF8);
            WalletApiWithdrawRequestDTO dto = JSON.parseObject(content, WalletApiWithdrawRequestDTO.class);
            if(dto.check() == false){
                log.error("invalid paramter : {} " , dto);
                throw new WalletApiException(400,"invalid paramters ");
            }
            dto.setSignType(request.getSignType());
            WalletRsaResponse resp = walletApiService.withdrawBill(dto);
            WalletApiRechargeResponseDTO ad = JSON.parseObject( resp.getBizContent() , WalletApiRechargeResponseDTO.class);
            String jsonString = KeyExchangeAlgorithmEncrypt.encryptContent(resp.getBizContent(),account.getSecretKey(),WalletApiConstants.CHARSET_UTF8);
            resp.setBizContent(jsonString);
            String sign2 = WalletSignature.rsaSign(JSON.toJsonString(resp) , account.getWalletPrivateKey(), WalletApiConstants.CHARSET_UTF8, request.getSignType());
            resp.setSign(sign2);
            // 发送消息mq 通知钱包业务处理提现。
            dto.setBizNo(ad.getTxid());
            rabbitMessageService.sendWithdrawApply(dto);
            return resp;
        }catch (WalletApiException e){
            log.error(e.getMessage());
            throw new ParamterValidateException(400,"invalid paramters.");
        }catch (JSON.JSONException e){
            log.error("invalid paramter : {} " , content);
            throw new WalletApiException(400,"invalid paramters ");
        }
    }


    /**
     * 签名交易
     * @param recipientAddress
     * @param satoshi
     * @param privateKey
     * @return
     */
    public String signedTx(String recipientAddress, long satoshi, String privateKey){
        return walletApiService.signRawTx(recipientAddress, satoshi, privateKey);
    }

    /**
     * 发送交易
     * @param signedTX
     * @return
     */
    public String sendRawTx(String signedTX){
        return walletApiService.sendRawTx(signedTX);
    }

    /**
     * btc/usdt address vaileaddress
     * @param address
     * @return
     */
    public boolean vailedAddress(String address){
        return walletApiService.vailedAddress(address);
    }

    // usdt交易
    public String usdtTx(String receiveAddress, String amount){
        return walletApiService.usdtTx(receiveAddress, amount);
    }


    public void sendEmail(String sendTo, String titel, String content ) {
        walletApiService.sendSimpleMail(sendTo,titel,content);
    }


    public boolean blockscan()throws WalletApiException {
        return walletApiService.blockscan();
    }

    public List<WalletUsdtResponseDTO> blockScanUsdt(){
        return walletApiService.blockScanUsdt();
    }




    public WalletApiResponse<WalletApiRegisterResponseDTO> restWalletAccountPass(WalletCommonApiRequest request) throws WalletApiException {
        String content = null;
        try{
            content = WalletSignature.rsaDecrypt(request.getRequestContent(), WalletApiConstants.CHARSET_UTF8 );
            WalletApiRegisterRequestDTO dto = JSON.parseObject(content, WalletApiRegisterRequestDTO.class);
            if(dto.check() == false){
                log.error("invalid paramter : {} " , dto);
                throw new WalletApiException(400,"invalid paramters ");
            }
            dto.setSignType(request.getSignType());
            WalletApiResponse<WalletApiRegisterResponseDTO> resp = walletApiService.restWalletAccountPass(dto);
            return resp;
        }catch (WalletApiException e){
            log.error(e.getMessage());
            if(e.getErrCode()==4001){
                throw e;
            }
            throw new ParamterValidateException(400,"rsaDecrypt fail.");
        }catch (JSON.JSONException e){
            log.error("invalid paramter : {} " , content);
            throw new ParamterValidateException(400,"invalid paramters ");
        }
    }




}
