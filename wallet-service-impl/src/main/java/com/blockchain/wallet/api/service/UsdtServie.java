package com.blockchain.wallet.api.service;

import com.alibaba.fastjson.JSONObject;
import com.blockchain.wallet.api.dto.WalletUsdtResponseDTO;

import java.util.List;

public interface UsdtServie {

    // step1 Construct payload
    public String createpayload(String amount);
    // step2 Construct transaction base
    public String createrawtransaction(String address_from);
    // step3 Attach payload output
    public String attachPayloadOutput(String txHex, String payload);
    // step4 Attach reference/receiver output
    public String txReference(String rawtx, String destination);
    // step5 Specify miner fee and attach change output
    public String txCharge(String tx, String changeAddress);
    // step6 sign
    public JSONObject signrawtransaction(String rawtx);
    // step7 send
    public String sendrawtransaction(String signtx);

    //导入私钥
    public JSONObject importPrivkey(String address);
    // 列出区块所有usdt交易
    public JSONObject listBlockUsdttx(long height);
    // usdt 交易详情
    public JSONObject txDetails(String txid);

    public int getBlockCount();
    //查询address的usdt
    public JSONObject balanceByAddress(String address);

    //扫块usdt
    public List<WalletUsdtResponseDTO> blockScanUsdt();

    // usdt简单发送交易
    public String send(String receiveAddress,String amt, String sendAddress);

    //归集资金
    public void merge();

    public int lastBlockHeight();

    public boolean parseBlock(int index);

    public String getNewAddress();
    public void walletLock();
    public void walletPassphrase(String password,int second);
    public void walletpassphrasechange(String currentpasswd, String newpasswd);

    public int sendRabbitMq();

}
