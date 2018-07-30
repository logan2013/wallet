package com.blockchain.wallet.api.service;

import com.blockchain.wallet.api.WalletApiException;
import com.blockchain.wallet.api.dto.*;
import com.blockchain.wallet.api.request.WalletApiResponse;
import com.blockchain.wallet.api.request.WalletRsaResponse;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @date 2018/6/27
 */
public interface WalletApiService {

    /**
     *  考虑接口的幂等性实现
     * @param request
     * @return
     */
     WalletApiResponse<WalletApiRegisterResponseDTO> generateWalletAddress(WalletApiRegisterRequestDTO request) throws WalletApiException;

    /**
     *  考虑接口的幂等性
     * @param request
     * @return
     * @throws WalletApiException
     */
    WalletRsaResponse rechargeOnLine(WalletApiRechargeRequestDTO request) throws WalletApiException;


    /**
     *  考虑接口的幂等性
     * @param request
     * @return
     * @throws WalletApiException
     */
    WalletRsaResponse cashTrade(WalletApiCashTradeRequestDTO request) throws WalletApiException;



    /**
     *  考虑接口的幂等性
     * @param request
     * @return
     * @throws WalletApiException
     */
    WalletRsaResponse withdrawBill(WalletApiWithdrawRequestDTO request) throws WalletApiException;






    /**
     * 签名交易
     * @param recipientAddress
     * @param satoshi
     * @param privateKey
     * @return
     */
    public String signRawTx(String recipientAddress, long satoshi, String privateKey);


    public String sendRawTx(String signedTx);

    /**
     * 生成地址
     * @return
     */
    public Map<String, String> produceAddress();

    public boolean vailedAddress(String address);


    public String usdtTx(String receiveAddress, String amount);


    public void sendMail();

    public void sendSimpleMail(String sendTo, String titel, String content);

    public boolean blockscan() throws WalletApiException;

    public List<WalletUsdtResponseDTO> blockScanUsdt();



    WalletApiResponse<WalletApiRegisterResponseDTO> restWalletAccountPass(WalletApiRegisterRequestDTO request) throws WalletApiException;




}
