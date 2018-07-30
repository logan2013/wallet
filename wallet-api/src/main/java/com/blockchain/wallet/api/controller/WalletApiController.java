package com.blockchain.wallet.api.controller;

import com.blockchain.wallet.api.WalletApiConstants;
import com.blockchain.wallet.api.WalletApiException;
import com.blockchain.wallet.api.consumer.WalletApiConsumer;
import com.blockchain.wallet.api.dto.WalletApiRegisterResponseDTO;
import com.blockchain.wallet.api.internal.util.WalletSignature;
import com.blockchain.wallet.api.request.WalletApiResponse;
import com.blockchain.wallet.api.request.WalletCommonApiRequest;
import com.blockchain.wallet.api.request.WalletRsaResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author shujun
 * @date 2018/6/26
 */
@RestController
@RequestMapping(value = "/api" ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
public class WalletApiController {

    @Autowired
    private WalletApiConsumer consumer;
    /**
     *  注册接口
     * @param request
     * @return
     * @throws WalletApiException
     */
    @RequestMapping(value = {"/register"}, method = {RequestMethod.POST})
    @ResponseBody
    public WalletApiResponse<WalletApiRegisterResponseDTO> generateWalletAddress(@RequestBody WalletCommonApiRequest request) throws WalletApiException {
        log.info("enter method generateWalletAddress ... ");
        return consumer.generateWalletAddress(request);
    }


    @RequestMapping(value = {"/test"}, method = {RequestMethod.POST})
    @ResponseBody
    public String test(@RequestBody String request) throws WalletApiException {
        log.info("request={} " , request);
        return  WalletSignature.rsaDecrypt(request, WalletApiConstants.CHARSET_UTF8 );
    }

    /**
     *  充值接口   version = 1.0.0_java
     *  注意：第一版简易程序使用
     *
     *  上层业务系统将线下交易的充值兑换的订单，传输给钱包系统
     * @param request
     * @return
     * @throws WalletApiException
     */
    @RequestMapping(value = {"/recharge"}, method = {RequestMethod.POST})
    @ResponseBody
    public WalletRsaResponse recharge(@RequestBody WalletCommonApiRequest request) throws WalletApiException {
        log.info("enter method recharge ... ");
        return consumer.rechargeOnLine(request);
    }

    /**
     *  撮合交易账单流水接口  version = 1.0.0_java
     * @param request
     * @return
     * @throws WalletApiException
     */
    @RequestMapping(value = {"/cash-bill"}, method = {RequestMethod.POST})
    @ResponseBody
    public WalletRsaResponse cash(@RequestBody WalletCommonApiRequest request) throws WalletApiException {
        log.info("enter method cash ... ");
        return consumer.cashTrade(request);
    }


    /**
     *  提现申请接口  version = 1.0.0_java
     * @param request
     * @return
     * @throws WalletApiException
     */
    @RequestMapping(value = {"/withdraw"}, method = {RequestMethod.POST})
    @ResponseBody
    public WalletRsaResponse withdraw(@RequestBody WalletCommonApiRequest request) throws WalletApiException {
        log.info("enter method withdraw ... ");
        return consumer.withdraw(request);
    }

    /**
     *  重置账户的秘钥
     * @param request
     * @return
     * @throws WalletApiException
     */
    @RequestMapping(value = {"/wallet-update"}, method = {RequestMethod.POST})
    @ResponseBody
    public WalletApiResponse<WalletApiRegisterResponseDTO> resetWalletAccount(@RequestBody WalletCommonApiRequest request) throws WalletApiException {
        log.info("enter method resetWalletAccount ... ");
        return consumer.restWalletAccountPass(request);
    }

}
