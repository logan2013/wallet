package com.blockchain.wallet.api.service;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public interface BitcoinService {

    // rpc 创建交易
    public String createRawTransaction(ArrayList<Map<String, Object>> inputs, Map<String, Object> outputs);
    // rpc 签名交易
    public JSONObject signRawTransaction(String txHex, ArrayList<Map<String, Object>> prevtxs, ArrayList<String> privkeys);
    //rpc发送交易
    public String sendRawTx(String transaction);
    //验证地址
    boolean vailedAddress(String address);
    // list unspent
    JSONObject listUnspent(String address);
    //import address
    public JSONObject importaddress(String address);
    //获取最新区块hash
    public String getBestBlockHash();
    //根据hash获取区块体信息
    public JSONObject getBlockchaininfo(String hash);
    //根据tx_id获取交易hash
    public String getrawtransaction(String txId);
    // 扫块
    public boolean blockscan();
    // 区块高度
    public Long getBlockCount();
    // 获取内存池未确认的交易
    public JSONObject getRawmempool();

    //资金归集到冷钱包
    String consolidate();
    //fee
    public double estimateFee(int waitingBlock);

    public void walletLock();

    public void walletPassphrase(String password,int second);
    public void walletpassphrasechange(String currentpasswd, String newpasswd);
}
