package com.blockchain.wallet.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.blockchain.wallet.api.Base.PublicResult;
import com.blockchain.wallet.api.Base.PublicResultConstant;
import com.blockchain.wallet.api.config.BitcoinProperties;
import com.blockchain.wallet.api.consumer.WalletApiConsumer;
import com.blockchain.wallet.api.internal.util.ComUtil;
import com.blockchain.wallet.api.internal.util.HttpUtil;
import com.blockchain.wallet.api.internal.util.JsonConvert;
import com.blockchain.wallet.api.response.Response;
import com.blockchain.wallet.api.service.impl.RedisService;
import com.blockchain.wallet.api.service.impl.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/bitcoinRpc")
public class BitcoinRpcController {

    @Autowired
    private WalletApiConsumer consumer;

    @Autowired
    private RedisService redisService;

    @Autowired
    private Sender sender;

    private final String APIURL = "https://blockexplorer.com/";


    /**
     * 广播交易
     * recipient 接收Address
     * satoshi   output free
     * privateKey
     **/
    @PostMapping("/bcSend")
    public PublicResult<Map<String, String>> btcSend(@RequestBody JSONObject requestJson){
        Map<String, String> result= new HashMap<>();
        String str="";
        String mes="";
        String recipientAddress = requestJson.getString("recipientAddress");
        String privateKey = requestJson.getString("privateKey");
        long satoshi=Long.parseLong(requestJson.getString("satoshi"));

        if (ComUtil.isEmpty(satoshi) || ComUtil.isEmpty(recipientAddress)) {
            result.put("mgs","接收地址或交易数量为空");
            return  new PublicResult<>(PublicResultConstant.SUCCESS, result);
        }
        if(!consumer.vailedAddress(recipientAddress)){
            result.put("mgs","无效的接收地址");
            return  new PublicResult<>(PublicResultConstant.SUCCESS, result);
        }
        try{
            str= consumer.sendRawTx(consumer.signedTx(recipientAddress,satoshi,privateKey));
        }catch(Exception e){
            mes=e.getMessage();
        }
        if(str.equals("")){
            result.put("mgs",mes);
            return  new PublicResult<>(PublicResultConstant.ERROR, result);
        }
        result.put("mgs",str);
        return  new PublicResult<>(PublicResultConstant.SUCCESS, result);
    }

    /**
     * 生成钱包和地址
     */
    @GetMapping("/produceAddress")
    @Deprecated
    public PublicResult<Map<String, String>>  produceAddress(){

        //new wallet address from redis
        Object walletObj = redisService.getMember("wallet");
        String walletStr = String.valueOf(walletObj);
        JSONObject jsonObject = JSONObject.parseObject(walletStr);
        String address = String.valueOf(jsonObject.get("address"));
        String wif = String.valueOf(jsonObject.get("wif"));
        String privateKey = String.valueOf(jsonObject.get("private-key"));
        String publicKey = String.valueOf(jsonObject.get("public-key"));

        //address使用后从redis中删除
        Long delFlag = redisService.delMember("wallet",walletObj);

        Map<String, String> result= new HashMap<>();

        return new PublicResult<>(PublicResultConstant.SUCCESS, result);

    }



    /**
     * 充值查询
     */
     @GetMapping("/depositQuery")
     public PublicResult<Map<String, String>> depositQuery(@RequestBody JSONObject requestJson){
//         String userId = requestJson.getString("userId");
         String userId = requestJson.getString("address");
        return null;
    }


    @GetMapping("/unspent")
    private PublicResult<Map<String, String>> unspent(String address){
        BitcoinProperties Bp=new BitcoinProperties();
        Map<String, String> result= new HashMap<>();
        String s = HttpUtil.get(APIURL+"api/addr/"+address+"/utxo",new HashMap<>(),new HashMap<>());
        result.put("res",s);
        return new PublicResult<>(PublicResultConstant.SUCCESS, result);
    }

    @GetMapping("/withdraw")
    public PublicResult<Map<String, String>> withdraw(String txid){
        Map<String, String> result= new HashMap<>();
        String s = HttpUtil.get(APIURL+"api/tx/"+txid,new HashMap<>(),new HashMap<>());
        result.put("res",s);
        return new PublicResult<>(PublicResultConstant.SUCCESS, result);
    }

    @GetMapping("/saveJson")
    public PublicResult<Map<String, String>> saveJson(){
        Map<String, String> result= new HashMap<>();
        JsonConvert.saveJson();
        return new PublicResult<>(PublicResultConstant.SUCCESS, result);
    }

    @RequestMapping(value = {"/txInfo"}, method = {RequestMethod.GET})
    @ResponseBody
    public Response txInfo() {
        return null;
    }

    @PostMapping("/notify")
    public PublicResult<Map<String, String>> notifyq(@RequestBody JSONObject requestJson){

        //sender.sendMsg(requestJson.getString("hash"));
        redisService.set("usdtHeight",532143);
         return null;
//        return new PublicResult<>(PublicResultConstant.SUCCESS,consumer.blockscan(requestJson.getString("hash")));
    }

}
