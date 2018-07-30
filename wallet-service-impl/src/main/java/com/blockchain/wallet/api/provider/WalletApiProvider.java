package com.blockchain.wallet.api.provider;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.blockchain.wallet.api.WalletApiException;
import com.blockchain.wallet.api.business.WalletAccountBusiness;
import com.blockchain.wallet.api.business.WalletApiBusiness;
import com.blockchain.wallet.api.dto.*;
import com.blockchain.wallet.api.request.WalletApiResponse;
import com.blockchain.wallet.api.request.WalletRsaResponse;
import com.blockchain.wallet.api.service.BitcoinService;
import com.blockchain.wallet.api.service.EmailService;
import com.blockchain.wallet.api.service.UsdtServie;
import com.blockchain.wallet.api.service.WalletApiService;
import com.blockchain.wallet.api.wallet.BtcSend;
import com.blockchain.wallet.api.wallet.BtcWallet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
/**
 *
 * @author shujun
 * @date 2018/6/27
 */
@Service
@Slf4j
public class WalletApiProvider implements WalletApiService {

    @Autowired
    private WalletApiBusiness walletApiBusiness;

    @Autowired
    private WalletAccountBusiness walletAccountBusiness;

    @Autowired
    private BitcoinService bitcoinService;

    @Autowired
    private UsdtServie usdtServie;

    @Autowired
    private EmailService emailService;

    @Override
    public WalletApiResponse<WalletApiRegisterResponseDTO> generateWalletAddress(WalletApiRegisterRequestDTO request) throws WalletApiException {
        log.info("enter into  WalletApiProvider.{}  timestamp:{} " , "generateWalletAddress" , System.currentTimeMillis());
        return walletApiBusiness.generateWalletAddress(request);
    }

    @Override
    public WalletRsaResponse rechargeOnLine(WalletApiRechargeRequestDTO request) throws WalletApiException {
        return walletAccountBusiness.rechargeOnLine(request);
    }

    @Override
    public WalletRsaResponse cashTrade(WalletApiCashTradeRequestDTO request) throws WalletApiException {
        return walletAccountBusiness.cashTrade(request);
    }

    @Override
    public WalletRsaResponse withdrawBill(WalletApiWithdrawRequestDTO request) throws WalletApiException {
        return walletAccountBusiness.withdrawBill(request);
    }

    /**
     * 签名交易
     * @param recipientAddress
     * @param satoshi
     * @param privateKey
     * @return
     */
    @Override
    public  String signRawTx(String recipientAddress, long satoshi, String privateKey){
        return BtcSend.signRawTx(recipientAddress, satoshi, privateKey);
    }

    /**
     * 发送交易
     * @param signedTx
     * @return
     */
    @Override
    public String sendRawTx(String signedTx) {
        return bitcoinService.sendRawTx(signedTx);
    }

    /**
     * 地址生成
     * @return
     */
    @Override
    public Map<String, String> produceAddress() {
        return BtcWallet.produceAddress();
    }

    /**
     * 验证地址有效性
     * @return
     */
    @Override
    public boolean vailedAddress(String address) {
        return bitcoinService.vailedAddress(address);
    }

    @Override
    public String usdtTx(String receiveAddress, String amount){
        // step1 Construct payload
        String payload = usdtServie.createpayload(amount);
        if(payload == ""){
            return "构建paylaod失败";
        }
        // step2 Construct transaction base
        // todo 配置文件加载提现地址
        String senderAddress = "";
        String rawtx = usdtServie.createrawtransaction(senderAddress);
        if(rawtx == ""){
            return "Construct transaction base failed";
        }

        // step3 attach payload output
        String apo = usdtServie.attachPayloadOutput(rawtx, payload);
        if(apo == ""){
            return "attach payload output failed";
        }

        // step4 Attach reference/receiver output
        String aro = usdtServie.txReference(apo, receiveAddress);
        if(aro == ""){
            return "Attach reference/receiver output failed";
        }

        // step5 Specify miner fee and attach change output
        String rawtx_change = usdtServie.txCharge(aro, "13ZG51mgV9sJ9MHsQYG7dxmsEShT5pUUrX");
        if(rawtx_change ==""){
            return "Specify miner fee and attach change output failed";
        }

        // step6 sign
        JSONObject signTxJson = usdtServie.signrawtransaction(rawtx_change);
        if (signTxJson == null) {
            return "rawtx sign failed";
        }
        String signTx = signTxJson.getJSONObject("result").getString("hex");

        // step7 send
        String txHash = usdtServie.sendrawtransaction(signTx);
        if(txHash == ""){
            return "send tx failed";
        }
        return null;
    }

    @Override
    public void sendMail() {
        emailService.sendInlineMail();
    }

    @Override
    public void sendSimpleMail(String sendTo, String titel, String content){
        emailService.sendSimpleMail(sendTo,titel,content);

    }

    @Override
    public boolean blockscan()throws WalletApiException{
        return bitcoinService.blockscan();
    }

    @Override
    public List<WalletUsdtResponseDTO> blockScanUsdt() {
        return usdtServie.blockScanUsdt();
    }

    @Override
    public WalletApiResponse<WalletApiRegisterResponseDTO> restWalletAccountPass(WalletApiRegisterRequestDTO request) throws WalletApiException {
        log.info("enter into  WalletApiProvider.{}  timestamp:{} " , "generateWalletAddress" , System.currentTimeMillis());
        return walletApiBusiness.restWalletPass(request);
    }


}
