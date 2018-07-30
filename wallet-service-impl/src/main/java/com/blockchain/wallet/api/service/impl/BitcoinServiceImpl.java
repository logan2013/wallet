package com.blockchain.wallet.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blockchain.wallet.api.dao.WalletAccountPOMapper;
import com.blockchain.wallet.api.dao.walletHotEntryMapper;
import com.blockchain.wallet.api.internal.util.HttpUtil;
import com.blockchain.wallet.api.po.WalletAccountPO;
import com.blockchain.wallet.api.po.walletHotEntry;
import com.blockchain.wallet.api.pojo.ScriptPubKey;
import com.blockchain.wallet.api.service.BitcoinService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * bitcoin操作
 *
 * @author zhangkai
 * @create 2018/6/7
 **/
@Component
@Service
public class BitcoinServiceImpl implements BitcoinService {

    private  String url = "http://47.104.203.101:8332";
    private  String username = "zk";
    private  String password = "zk";

    private Logger log = LoggerFactory.getLogger(BitcoinServiceImpl.class);

    private final static String RESULT = "result";
    private final static String METHOD_GET_BLOCK_COUNT = "getblockcount";
    private final static String METHOD_NEW_ADDRESS = "getnewaddress";
    private final static String METHOD_GET_BALANCE = "getbalance";
    private final static String METHOD_SEND_RAWTRANSACTION = "sendrawtransaction";
    private final static String ACCOUNT_TYPE = "1";
    private final static String TX = "tx";
    private final static String NEXT_BLOCK_HASH = "nextblockhash";
    private final static String HEIGHT = "height";


    private final static String METHOD_GET_BEST_BLOCKBESTHASH = "getbestblockhash";
    private final static String METHOD_GET_BLOCK = "getblock";
    private final static String METHOD_GET_RAWTRANSACTION = "getrawtransaction";
    private final static String METHOD_GET_DECODERAWTRANSACTION = "decoderawtransaction";
    private final static String METHOD_GET_BLOCKHASH = "getblockhash";

    @Autowired
    private walletHotEntryMapper walletHotEntryMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private WalletAccountPOMapper  WalletAccountPOMapper;

    /**
     * 发送请求
     * @param method
     * @param params
     * @return
     */
    private JSONObject doRequest(String method,Object... params){
        JSONObject param = new JSONObject();
        param.put("id",System.currentTimeMillis()+"");
        param.put("jsonrpc","2.0");
        param.put("method",method);
        if(params != null){
            param.put("params",params);
        }
        String creb = Base64.encodeBase64String((username+":"+password).getBytes());
        Map<String,String> headers = new HashMap<>(2);
        headers.put("Authorization","Basic "+creb);
        return JSON.parseObject(HttpUtil.jsonPost(url,headers,param.toJSONString()));
    }
    private boolean isError(JSONObject json){
        if( json == null || (StringUtils.isNotEmpty(json.getString("error")) && json.get("error") != "null")){
            return true;
        }
        return false;
    }

    public JSONObject getInfo() throws Exception {
        return doRequest("getwalletinfo");
    }

    /**
     * 产生地址
     * @return
     */
    public String getNewAddress(){
        JSONObject json = doRequest(METHOD_NEW_ADDRESS);
        if(isError(json)){
            log.error("获取bitcoin地址失败:{}",json.get("error"));
            return "";
        }
        return json.getString(RESULT);
    }

    /**
     * 查询余额
     * @return
     */
    public JSONObject getBalance(String addr){
        JSONObject json = doRequest(METHOD_GET_BALANCE,addr);
        if(isError(json)){
            log.error("获取bitcoin余额:{}",json.get("error"));
            return json;
        }
        return json;
    }

    /**
     * 验证地址的有效性
     * @param address
     * @return
     * @throws Exception
     */
    @Override
    public boolean vailedAddress(String address) {
        JSONObject json  = doRequest("validateaddress",address);
        if(isError(json)){
            log.error("bitcoin验证地址失败:",json.get("error"));
            return false;
        }else{
            return json.getJSONObject(RESULT).getBoolean("isvalid");
        }
    }

    /**
     * 区块高度
     * @return
     */
    @Override
    public Long getBlockCount(){
        JSONObject json = null;
        try {
            json = doRequest(METHOD_GET_BLOCK_COUNT);
            if(!isError(json)){
                return json.getLong("result");
            }else{
                log.error(json.toString());
                return Long.valueOf(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Long.valueOf(0);
        }
    }

    /**
     *查询UTXO
     * @param address
     * @return
     */
    @Override
    public JSONObject listUnspent(String address){
        String[] addr = {address};
        JSONObject json  = doRequest("listunspent",0,999999,addr);
        if(isError(json)){
            log.error("查询bitcoin-utxo出错");
        }
        return json;
    }


    /**
     * rawTransaction
     * @param txid
     * @return
     */
    public JSONObject getRawTransaction(String txid){
        boolean format = true;
        JSONObject json  = doRequest("getrawtransaction",txid, format);
        if(isError(json)){
            log.error("bitcoin-getrawtransaction:",json.get("error"));
        }
        return json;
    }

    /**
     * import-address
     * @param address
     * @return
     */
    @Override
    public JSONObject importaddress(String address){
        boolean rescan = false;
        JSONObject json  = doRequest("importaddress",address,"watch-only",rescan);
        if(isError(json)){
            log.error("importaddress出错");
        }
        return json;
    }

    /**
     * 获取最新区块hash
     */
    @Override
    public String getBestBlockHash(){

        JSONObject json = doRequest(METHOD_GET_BEST_BLOCKBESTHASH);

        if(isError(json)){
            log.error("获取hash失败:{}",json.get("error"));
            return "";
        }
        return json.getString(RESULT);
    }

    /**
     * 根据hash获取区块体信息
     */
    @Override
    public JSONObject getBlockchaininfo(String hash){

        JSONObject json = doRequest(METHOD_GET_BLOCK,hash);

        if(isError(json)){
            log.error("获取b区块体信息:{}",json.get("error"));
            return json;
        }
        return json;
    }

    /**
     * 根据tx_id获取交易hash
     */
    @Override
    public String getrawtransaction(String txId){

        JSONObject json = doRequest(METHOD_GET_RAWTRANSACTION,txId);

        if(isError(json)){
            log.error("获取交易hash:{}",json.get("error"));
            return "";
        }
        return json.getString("result");
    }


    /**
     * 根据tx_id获取交易hash
     */
    public JSONObject decoderawtranscation(String hash){

        JSONObject json = doRequest(METHOD_GET_DECODERAWTRANSACTION,hash);

        if(isError(json)){
            log.error("获取交易信息:{}",json.get("error"));
            return json;
        }
        return json;
    }

    /**
     * 获取内存池未确认的交易
     * @return
     */
    @Override
    public JSONObject getRawmempool(){
        JSONObject json = doRequest("getrawmempool");
        if(isError(json)){
            log.error("获取交易信息:{}",json.get("error"));
            return json;
        }
        return json;
    }


    /**
     *扫块
     * @param
     * @return
     */
    @Override
    @Transactional
    public boolean blockscan(){

        redisService.set(HEIGHT, 532076);
        Map<String,String> hashmap=new HashMap<>();
        Map<String,Object> addressmap=new HashMap<>();
        int lastBlockHeight;
        if(redisService.get("height") ==null){
            lastBlockHeight=Integer.parseInt(this.getHeigth());
        }else{
            lastBlockHeight=Integer.parseInt(redisService.get("height").toString());
        }

        //平台中所有的地址
        WalletAccountPO wap =new WalletAccountPO();
        List<WalletAccountPO> list=WalletAccountPOMapper.selectWalletAccount(wap.getUserName(),wap.getAccountType(),wap.getWalletAddressHash());
        System.out.println(list);
        for (WalletAccountPO wp: list) {
            addressmap.put(wp.getWalletAddressHash(),wp);
        }

        String heigthNow=this.getHeigth();
        System.out.println("最新区块"+heigthNow);
        System.out.println("当前区块"+lastBlockHeight);
        int blockNumber=Integer.parseInt(heigthNow)-lastBlockHeight;
        for(int k=0;k<=blockNumber;k++){
            String  hash=this.getBlockHash(lastBlockHeight).getString(RESULT);
            //根据区块高度获取区块内容
            JSONObject json =this.getBlockchaininfo(hash);
            walletHotEntry whe=new walletHotEntry();
            if (json != null) {
                //区块json内容
                JSONObject object = json.getJSONObject(RESULT);
                //找到json对象
                JSONArray receiveaddressData = object.getJSONArray(TX);
                //创建时间
                whe.setCreateTime(System.currentTimeMillis());
                //类型
                whe.setAccountType(ACCOUNT_TYPE);
                //区块高度
                String height = object.getString(HEIGHT);
                if (height != null) {
                    whe.setHeight(Integer.parseInt(height));
                }
                hashmap.put(HEIGHT, height);
                Long startTime = System.currentTimeMillis();
                Map<Object, Object> m = new HashMap<>();

                //遍历tx_id
                for (int i = 0; i < receiveaddressData.size(); i++) {
                    //TX_ID
                    String ha = this.getrawtransaction(receiveaddressData.get(i).toString());
                    if (receiveaddressData.get(i) != null) {
                        whe.setTxid(receiveaddressData.get(i).toString());
                    }
                    //根据tx_id查询交易信息
                    JSONObject TxObj = this.decoderawtranscation(ha).getJSONObject(RESULT);
                    //交易时间
                    if (TxObj.getString("time") != null) {
                        whe.setTimestamp(Long.parseLong(TxObj.getString("time")));
                    }
                    //交易确认数
                    if (TxObj.getString("confirmations") != null) {
                        whe.setConfirmations(Integer.parseInt(TxObj.getString("confirmations")));
                    }
                    //输出地址voutaddress
                    JSONArray vout = TxObj.getJSONArray("vout");
                    if (vout.size() > 0) {
                        for (int j = 0; j < vout.size(); j++) {
                            //vout对象
                            JSONObject ob = vout.getJSONObject(j);
                            String sp = ob.getString("scriptPubKey");
                            ScriptPubKey scriptPubKey = JSON.parseObject(sp, ScriptPubKey.class);

                            if (scriptPubKey.getAddresses() != null && addressmap.get((scriptPubKey.getAddresses().toString()).replaceAll("\\[|\\]", "")) !=null) {
                                System.out.println((scriptPubKey.getAddresses().toString()).replaceAll("\\[|\\]", ""));
                                whe.setAmount(ob.getString("value"));                //数量
                                whe.setWalletAddressHash((scriptPubKey.getAddresses().toString()).replaceAll("\\[|\\]", ""));   //地址
                                whe.setVout(Integer.parseInt(ob.getString("n")));    //n
                                walletHotEntryMapper.insert(whe);
                            }
                        }
                    }
                }
                redisService.set(HEIGHT, lastBlockHeight);
                lastBlockHeight++;
                System.out.println(System.currentTimeMillis() - startTime);

            }
        }
        return true;
    }

    // rpc 创建交易
    @Override
    public String createRawTransaction(ArrayList<Map<String, Object>> inputs, Map<String, Object> outputs){
        JSONObject json = doRequest("createrawtransaction", inputs, outputs);
        if(isError(json)){
            return json.getString("result");
        }
        return json.getString("result");
    }

    // rpc 签名交易
    @Override
    public JSONObject signRawTransaction(String txHex, ArrayList<Map<String, Object>> prevtxs, ArrayList<String> privkeys){
        JSONObject json = doRequest("signrawtransaction", txHex, prevtxs, privkeys);
        if(isError(json)){
            return json.getJSONObject("result");
        }
        return json.getJSONObject("result");
    }

    // rpc 发送交易
    @Override
    public String sendRawTx(String signedTx){
        JSONObject json = doRequest("sendrawtransaction",signedTx);
        if(isError(json)){
            return "";
        }else {
            return json.getString(RESULT);
        }
    }

    //资金归集到冷钱包
    @Override
    public String consolidate(){
        // todo
        // 冷钱包地址列表
        // 遍历数据库满足归集的address

        return null;
    }


    //获取最新区块高度
    private String getHeigth() {
        JSONObject blockHash = doRequest(METHOD_GET_BEST_BLOCKBESTHASH);
        JSONObject json = this.getBlockchaininfo(blockHash.getString(RESULT));
        String height=null;
        if (json != null) {
            //区块json内容
            JSONObject object = json.getJSONObject(RESULT);
            //区块高度
            height = object.getString(HEIGHT);

        }
        return height;
    }

    /**
     * 根据height获取交易hash
     */
    public JSONObject getBlockHash(int height){

        JSONObject json = doRequest(METHOD_GET_BLOCKHASH,height);

        if(isError(json)){
            log.error("获取信息:{}",json.get("error"));
            return json;
        }
        return json;
    }

    //fee per kb
    @Override
    public double estimateFee(int waitingBlock){
        if(waitingBlock<2 || waitingBlock > 25) return 0.0;
        JSONObject json = doRequest("estimatefee",waitingBlock);
        if(isError(json)){
            log.error("获取信息:{}",json.get("error"));
            return 0.0;
        }
        return json.getDoubleValue("result");
    }

    //加锁钱包
    @Override
    public void walletLock(){
        JSONObject json = doRequest("walletlock");
        if(isError(json)){
            log.error("获取信息:{}",json.get("error"));
        }
    }
    //解锁钱包
    @Override
    public void walletPassphrase(String password,int second){
        JSONObject json = doRequest("walletpassphrase", password,second);
        if(isError(json)){
            log.error("获取信息:{}",json.get("error"));
        }
    }
    //重置密码
    @Override
    public void walletpassphrasechange(String currentpasswd, String newpasswd){
        JSONObject json = doRequest("walletpassphrasechange", currentpasswd,newpasswd);
        if(isError(json)){
            log.error("获取信息:{}",json.get("error"));
        }
    }
}