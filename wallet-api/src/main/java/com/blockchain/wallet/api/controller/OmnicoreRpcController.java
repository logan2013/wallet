package com.blockchain.wallet.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.blockchain.wallet.api.annotation.ValidationParam;
import com.blockchain.wallet.api.consumer.WalletApiConsumer;
import com.blockchain.wallet.api.internal.util.ComUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * omni rpc 接口
 */
@RestController
@RequestMapping("/OmnicoreRpc")
public class OmnicoreRpcController {

    @Autowired
    private WalletApiConsumer consumer;

//    /**
//     * USDT查询余额
//     * @return
//     */
//    @PostMapping("/getBalance")
//    public JSONObject getBalance(@ValidationParam("address")@RequestBody JSONObject requestJson){
//        String address = requestJson.getString("address");
//        if(!coinUsdtService.vailedAddress(address)){
//            return requestJson.getJSONObject("地址无效");
//        }
//        return coinUsdtService.getBalance(address);
//    }

//    @PostMapping("/listunspent")
//    public JSONObject listunspent(@ValidationParam("address")@RequestBody JSONObject requestJson){
//        String address = requestJson.getString("address");
//        if(!coinUsdtService.vailedAddress(address)){
//            return requestJson.getJSONObject("地址无效");
//        }
//        return coinUsdtService.listunspent(address);
//    }

//    /**
//     * USDT转帐
//     * @param addr
//     * @param amt
//     * @return
//     * @deprecated
//     */
//    @PostMapping("/send")
//    public String send(String addr,Double amt) {
//        addr = "n1F2CdHef3zDY3j7Jske6U4Hvmq6pJQDYt";
//        amt = 5.0;
//        return coinUsdtService.send(addr, amt);
//    }

    /**
     * 验证地址的有效性
     * @return
     */
    @PostMapping("/vailedAddress")
    public boolean vailedAddress(@ValidationParam("address")@RequestBody JSONObject requestJson) {
        String address = requestJson.getString("address");
        return consumer.vailedAddress(address);
    }

    /**
     * USDT 交易
     * @param requestJson
     * @return
     */
    @PostMapping("/usdtTx")
    public String usdtTx(@ValidationParam("amount,receiveAddress")@RequestBody JSONObject requestJson){
        String receiveAddress = requestJson.getString("receiveAddress");
        String amount = requestJson.getString("amount");
        if (ComUtil.isEmpty(amount) || ComUtil.isEmpty(receiveAddress)) {
            return "接收地址或交易数量为空";
        }
        if(!consumer.vailedAddress(receiveAddress)){
            return "无效的接收地址";
        }
        return consumer.usdtTx(receiveAddress,amount);
    }
}
