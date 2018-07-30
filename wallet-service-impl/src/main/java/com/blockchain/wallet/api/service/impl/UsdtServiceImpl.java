package com.blockchain.wallet.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blockchain.wallet.api.dao.*;
import com.blockchain.wallet.api.dto.WalletUsdtMsgDTO;
import com.blockchain.wallet.api.dto.WalletUsdtResponseDTO;
import com.blockchain.wallet.api.internal.util.HttpUtil;
import com.blockchain.wallet.api.mq.WalletBlockScanApply;
import com.blockchain.wallet.api.po.*;
import com.blockchain.wallet.api.service.UsdtServie;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.*;

/**
 * usdt操作
 * @author zhangkai
 * @create 2018/6/7
 **/
@Service
public class UsdtServiceImpl implements UsdtServie {

   // private  String  ADDRESS = "13ZG51mgV9sJ9MHsQYG7dxmsEShT5pUUrX";
    private Logger log = LoggerFactory.getLogger(UsdtServiceImpl.class);

    private final static String RESULT = "result";
    private final static String METHOD_SEND_TO_ADDRESS = "omni_send";
    private final static String METHOD_GET_TRANSACTION = "omni_gettransaction";
    private final static String METHOD_GET_BLOCK_COUNT = "getblockcount";
    private final static String METHOD_NEW_ADDRESS = "getnewaddress";
    private final static String METHOD_GET_BALANCE = "omni_getbalance";
    private final static String METHOD_GET_LISTBLOCKTRANSACTIONS = "omni_listblocktransactions";
    private final static String METHOD_GETTRADE= "omni_gettrade";
    private final static String CONTINER_USDT= "USDT";

  //  OmnicoreProperties op=new OmnicoreProperties();


    @Autowired
    private walletHotEntryMapper walletHotEntryMapper;

    @Autowired
    private walletHotOutMapper walletHotOutMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private WalletAccountPOMapper WalletAccountPOMapper;


    @Autowired
    private WalletHotPOMapper walletHotPOMapper ;

    @Autowired
    private WalletColdMapper walletColdMapper;

    @Autowired
    private walletPlatformEntryMapper walletPlatformEntryMapper;

    @Autowired
    private WalletBlockHeightPOMapper WalletBlockHeightPOMapper;

    @Autowired
    private walletPlatformOutMapper walletPlatformOutMapper;


    @Autowired
    private walletPlatformMapper walletPlatformMapper;

    @Autowired
    private WalletBlockScanApply WalletBlockScanApply;

    @Autowired
    private  WalletPreinstallAddressMapper walletPreinstallAddressMapper;

    @Value("${spring.usdt.combinelimit}")
    private double combinelimit;

    @Value("${spring.usdt.propertyid}")
    private int propertyId = 31;

    @Value("${spring.usdt.username}")
    private String username;

    @Value("${spring.usdt.password}")
    private String password;

    @Value("${spring.usdt.url}")
    private String url;


    // USDT产生地址
    @Autowired
    public String getNewAddress(){
        JSONObject json;
        try {
            json = doRequest(METHOD_NEW_ADDRESS);
            if(isError(json)){
                log.error("获取USDT地址失败:{}",json.get("error"));
                return "";
            }
            return json.getString(RESULT);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return "";
    }

    /**
     * USDT查询余额
     * @return
     */
    public JSONObject getBalance(String addr){
        Map<Object,Object> m=new HashMap<>();
        JSONObject json = doRequest(METHOD_GET_BALANCE,addr,propertyId);
        if(isError(json)){
            log.error("获取USDT余额:{}",json.get("error"));
            return json;
        }

        return json;
        //return BigDecimalUtil.inputConvert(json.getJSONObject(RESULT).getDouble("balance"));
    }

    //USDT简单发送交易
    @Override
    public String  send(String receiveAddress,String amt,String sendAddress){
        if(vailedAddress(receiveAddress)){
            JSONObject json = doRequest(METHOD_SEND_TO_ADDRESS,sendAddress,receiveAddress,propertyId,amt);
            if(isError(json)){
                log.error("USDT 转帐给{} value:{}  失败 ：",receiveAddress,amt,json.get("error"));
                return "";
            }else{
                log.info("USDT 转币给{} value:{} 成功",receiveAddress,amt);
                return json.getString(RESULT);
            }
        }else{
            log.error("USDT接受地址不正确");
            return "";
        }
    }

    @Override
    //资金归集
    public void merge() {
        WalletCold record = new WalletCold();
        record.setAccountType("USDT");
        List<WalletCold> walletColdList = walletColdMapper.selectWalletAccount(record);
        if(walletColdList.size() < 1 ) return;
        WalletCold walletCold = walletColdList.get(0);//取第一个作为冷钱包地址
        List<WalletHotPO> walletList = walletHotPOMapper.selectWalletHot();//归集来源
        if(walletList.size() < 1) return;

        try{
            for(WalletHotPO WalletHotPO : walletList){
                JSONObject jo = this.balanceByAddress(WalletHotPO.getWalletAddressHash());//获取余额
                if(!jo.isEmpty()){
                    String balance = jo.getJSONObject("result").getString("balance");
                    if(Double.parseDouble(balance) > combinelimit){
                        String txid = this.send(walletCold.getWalletAddressHash(),balance,WalletHotPO.getWalletAddressHash());
                        if(txid != null){
                            JSONObject info = this.txDetails(txid);
                            walletHotOut who = new walletHotOut();
                            who.setWalletAddressHash(WalletHotPO.getWalletAddressHash());
                            who.setAccountType("USDT");
                            who.setCreateTime(System.currentTimeMillis());
                            who.setTxid(txid);
                            who.setToWalletAddress(walletCold.getWalletAddressHash());
                            who.setAmount(balance);
                            who.setActualFee(info.getJSONObject("result").getString("fee"));
                            who.setSource("1");
                            int res = walletHotOutMapper.insertSelective(who);
                            log.info("添加资金归集记录： "+res);
                        }
                    }
                }
            }
        }catch (Exception e){
            log.error("资金归集error : "+e.getMessage());
        }
    }

    //验证地址的有效性
    private boolean vailedAddress(String address) {
        JSONObject json  = doRequest("validateaddress",address);
        if(isError(json)){
            log.error("USDT验证地址失败:",json.get("error"));
            return false;
        }else{
            return json.getJSONObject(RESULT).getBoolean("isvalid");
        }
    }


    //区块高度
    @Override
    public int getBlockCount(){
        JSONObject json = null;
        try {
            json = doRequest(METHOD_GET_BLOCK_COUNT);
            if(!isError(json)){
                return json.getInteger("result");
            }else{
                log.error(json.toString());
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
/*
    public boolean parseBlock(int index) {
        //doRequest("omni_listblocktransactions",279007);
        //{"result":["63d7e22de0cf4c0b7fd60b4b2c9f4b4b781f7fdb8be4bcaed870a8b407b90cf1","6fb25ab84189d136b95d7f733b0659fa5fbd63f476fb1bca340fb4f93de6c912","d54213046d8be80c44258230dd3689da11fdcda5b167f7d10c4f169bd23d1c01"],"id":"1521454868826"}
        JSONObject jsonBlock = doRequest(METHOD_GET_LISTBLOCKTRANSACTIONS, index);
        if (isError(jsonBlock)) {
            log.error("访问USDT出错");
            return false;
        }

        JSONArray jsonArrayTx = jsonBlock.getJSONArray(RESULT);
        if (jsonArrayTx == null || jsonArrayTx.size() == 0) {
            //没有交易
            return true;
        }
        List<UserCoinAddressEntity> userList = userCoinService.getAllUserCoinAddress(CoinConstant.COIN_USDT);
        if(userList == null || userList.size() == 0){
            return true;
        }

        Iterator<Object> iteratorTxs = jsonArrayTx.iterator();
        while(iteratorTxs.hasNext()){
            String txid = (String) iteratorTxs.next();
            parseTx(txid,userList);
        }
        return true;

    }*/

    /**
     *查询UTXO
     * @param address
     * @return
     */
    public JSONObject listunspent(String address){
        String[] addr = {address};
        JSONObject json  = doRequest("listunspent",0,999999,addr);
        if(isError(json)){
            log.error("查询utxo出错");
        }
        return json;
    }

    public void parseTx(String txid,List<WalletAccountPO> list){
        /**
         * {"result":{"amount":"0.10000000","ecosystem":"test","propertyname":"Bazz",
         * "data":"BazzyFoo","divisible":true,"fee":"0.00010000","propertytype":"divisible",
         * "txid":"63d7e22de0cf4c0b7fd60b4b2c9f4b4b781f7fdb8be4bcaed870a8b407b90cf1",
         * "ismine":false,"type":"Create Property - Manual","confirmations":453133,
         * "version":0,"url":"www.bazzcoin.info","sendingaddress":"2N1WnASsjgwrzbucBHhMd6gHLqpipq7kUZM",
         * "valid":true,"blockhash":"000000002101ea0da161b6a63f4d1a0e37a2bd58c5aee49a3b8fd80640b09662",
         * "blocktime":1409941113,"positioninblock":19,"block":279007,"category":"FooMANAGED",
         * "subcategory":"Bar","propertyid":2147483664,"type_int":54},"id":"1521456310559"}
         */
        JSONObject jsonTransaction = doRequest(METHOD_GET_TRANSACTION, txid);
        if(isError(jsonTransaction)) {
            log.error("处理USDT tx出错");
            return;
        }
        JSONObject jsonTResult = jsonTransaction.getJSONObject(RESULT);
        if (!jsonTResult.getBoolean("valid")) {
            log.info("不是有效数据");
            return;
        }

        int propertyidResult = jsonTResult.getIntValue("propertyid");
        if (propertyidResult!=propertyId) {
            log.info("非USDT数据");
            return;
        }
        WalletUsdtResponseDTO wrd = JSON.parseObject(jsonTransaction.getString("result"), WalletUsdtResponseDTO.class);
       // int coinfirm = jsonTResult.getIntValue("confirmations");
        double value = jsonTResult.getDouble("amount");
        if(value >0) {
            String address = jsonTResult.getString("referenceaddress");
            for (WalletAccountPO addressModel : list) {
                //如果有地址是分配给用记的地址， 则说明用户在充值
                if (address.equals(addressModel.getWalletAddressHash())) {
                    //添加充值记录
                    walletHotEntry whe=new walletHotEntry();
                    whe.setWalletAddressHash(wrd.getReferenceaddress());
                    whe.setCreateTime(System.currentTimeMillis());
                    whe.setHeight(wrd.getBlock().intValue());
                    whe.setAmount(wrd.getAmount().toString());
                    whe.setTimestamp(wrd.getBlocktime());
                    whe.setAccountType("USDT");
                    BeanUtils.copyProperties(wrd,whe);
                    //存入数据库
                    try {
                        if(getList(whe) !=null && getList(whe).size()>0){
                            whe.setEntryId(this.getList(whe).get(0).getEntryId());
                            walletHotEntryMapper.updateByPrimaryKey(whe);
                        }else{
                            int w=walletHotEntryMapper.insert(whe);
                            if(w<0){
                                log.error("扫块数据插入失败:{}",whe);
                            }
                        }
                    }catch (Exception e){
                      log.error("这个用户{}的充值已经处理了 币：{}",addressModel.getUserName(),addressModel);
                    }
                }
            }
        }
    }


    // error 处理
    private boolean isError(JSONObject json){
        if( json == null || (StringUtils.isNotEmpty(json.getString("error")) && json.get("error") != "null")){
            return true;
        }
        return false;
    }
    // 发送请求
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

    @Override
    public String createpayload(String amount){
        // todo
        // 判断用户是否有足够 amount usdt

        JSONObject json = doRequest("omni_createpayload_simplesend",propertyId,amount);
        if(isError(json)){
            log.error("创建payload失败：",amount,json.get("error"));
            return "";
        }else {
            return json.getString(RESULT);
        }
    }
    @Override
    public String createrawtransaction(String address_from){
        JSONObject info = this.listunspent(address_from);

        JSONArray jsonArr = JSONArray.parseArray(info.getString(RESULT));
        if(jsonArr.isEmpty()){
            return "";
        }
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        for(int i =0; i< jsonArr.size(); i++){
            JSONObject job = jsonArr.getJSONObject(i);
            Map<String, Object> map = new HashMap<>();
            String txid = job.getString("txid");
            int vout = job.getIntValue("vout");
            map.put("txid", txid);
            map.put("vout", vout);
            list.add(map);
        }

        JSONObject json = doRequest("createrawtransaction",list,new Object());
        if(isError(json)){
            log.error("创建交易：",json.get("error"));
            return "";
        }else {
            return json.getString(RESULT);
        }
    }

    @Override
    public String attachPayloadOutput(String txHex, String payload){
        JSONObject json = doRequest("omni_createrawtx_opreturn",txHex,payload);
        if(isError(json)){
            log.error("负载输出贴到原始交易：",json.get("error"));
            return "";
        }else {
            return json.getString(RESULT);
        }
    }
    @Override
    public String txReference(String rawtx, String destination){
        JSONObject json = doRequest("omni_createrawtx_reference",rawtx,destination);
        if(isError(json)){
            log.error("负载输出贴到原始交易：",json.get("error"));
            return "";
        }else {
            return json.getString(RESULT);
        }
    }

    @Override
    public JSONObject signrawtransaction(String rawtx){
        JSONObject json = doRequest("signrawtransaction",rawtx);
        if(isError(json)){
            log.error("负载输出贴到原始交易：",json.get("error"));
            return new JSONObject();
        }
        return json;
    }
    @Override
    public String sendrawtransaction(String signtx){
        JSONObject json = doRequest("sendrawtransaction",signtx);
        if(isError(json)){
            log.error("负载输出贴到原始交易：",json.get("error"));
            return "";
        }else {
            return json.getString(RESULT);
        }
    }
    @Override
    public String txCharge(String tx, String changeAddress){

        JSONObject info = this.listunspent(changeAddress);
        JSONArray jsonArr = JSONArray.parseArray(info.getString(RESULT));
        if(jsonArr.isEmpty()){
            return "";
        }
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        for(int i =0; i< jsonArr.size(); i++){
            JSONObject job = jsonArr.getJSONObject(i);
            Map<String, Object> map = new HashMap<>();
            String txid = job.getString("txid");
            int vout = job.getIntValue("vout");
            double amount = job.getDoubleValue("amount");
            String scriptPubKey = job.getString("scriptPubKey");
            map.put("txid", txid);
            map.put("vout", vout);
            map.put("scriptPubKey", scriptPubKey);
            map.put("value", amount);
            list.add(map);
        }

        JSONObject json = doRequest("omni_createrawtx_change", tx, list, changeAddress, 0.00002);
        if(isError(json)){
            log.error("负载输出贴到原始交易：",json.get("error"));
            return "";
        }else {
            return json.getString(RESULT);
        }
    }

    @Override
    //导入私钥
    public JSONObject importPrivkey(String wif){
        boolean rescan = false;
        JSONObject json  = doRequest("importprivkey",wif,"",rescan);
        if(isError(json)){
            log.error("importprivkey出错");
        }
        return json;
    }

    @Override
    // 列出区块所有usdt交易
    public JSONObject listBlockUsdttx(long height){
        JSONObject json  = doRequest(METHOD_GET_LISTBLOCKTRANSACTIONS,height);
        if(isError(json)){
            log.error("omni_listblocktransactions error");
        }
        return json;
    }


    /**
     * usdt 交易详情
     */
    @Override
    public JSONObject txDetails(String txid){
        JSONObject json  = doRequest(METHOD_GETTRADE,txid);
        if(isError(json)){
            log.error("omni_gettrade error");
        }
        return json;
    }

    @Override
    //地址对应的usdt
    public JSONObject balanceByAddress(String address){
        int propertyid = propertyId;
        JSONObject json  = doRequest("omni_getbalance",address, propertyid);
        if(isError(json)){
            log.error("omni_getbalance error");
        }
        return json;
    }



    /**
     * USDT 扫块
     * 交易信息格式
     *{
     *   "txid": "82f61d8bcd0ccc2a66d33e3704c29e1e87942e0e30c2f07d15b2baea8fb3c64d",
     *   "fee": "0.00002000",
     *   "sendingaddress": "37Tm3Qz8Zw2VJrheUUhArDAoq58S6YrS3g",
     *   "referenceaddress": "14AwtvmKGxpxcA2Ey2jcNxGk9TpQMgqHtT",
     *   "ismine": false,
     *   "version": 0,
     *   "type_int": 0,
     *   "type": "Simple Send",
     *   "propertyid": 31,
     *   "divisible": true,
     *   "amount": "28320.44700000",
     *   "valid": true,
     *   "blockhash": "0000000000000000000c3f4576d6798b65c19b6ea728c45d79d703ae3b322cc1",
     *   "blocktime": 1531738223,
     *   "positioninblock": 2080,
     *   "block": 532143,
     *   "confirmations": 74
     * }
     */
    @Override
    @Transactional
    public List<WalletUsdtResponseDTO> blockScanUsdt() {

        Long startTime = System.currentTimeMillis();
        Map<String,Object> addressmap=new HashMap<>();
        Map<String,Object> addressColdMap=new HashMap<>();
        Map<String,Object> addressPlatformMap=new HashMap<>();
        //平台预设地址
        Map<String,Object> addressPreinstallMap=new HashMap<>();
        List<WalletUsdtResponseDTO> listusdt=new ArrayList<>();
        WalletAccountPO wap =new WalletAccountPO();

        try {
            //平台中热钱包的地址
            addressmap=this.getHotAddress();
            addressColdMap=this.getColdAddress();
            addressPlatformMap=this.getPlatAddress();
            addressPreinstallMap=getPreinstall();
            //当前区块高度
            int height=getBlockCount();
            int lastBlockHeight=lastBlockHeight();
            log.info("bte当前高度是：{} 处理记录高度：{}",height,lastBlockHeight);
            int blockNumber=height-lastBlockHeight;

            for(int k=0;k<blockNumber;k++){
                lastBlockHeight++;
                //根据高度查询区块信息
                JSONObject Usdtobject=listBlockUsdttx(lastBlockHeight);
                JSONArray UsdtJsonArray=Usdtobject.getJSONArray("result");
                if(!Usdtobject.isEmpty() && UsdtJsonArray.size()>0 && Usdtobject.getString("result")!=null && StringUtils.isNotEmpty(Usdtobject.getString("result"))){
                    //遍历tx_id
                    for (int i=0;i<UsdtJsonArray.size();i++){
                        //更具tx_id查询交易信息
                        JSONObject listObject=txDetails(UsdtJsonArray.get(i).toString());

                        //判断是否是USDT数据
                        if(isError(listObject)) {
                            log.error("处理USDT tx出错");
                            continue;
                        }
                        JSONObject jsonTResult = listObject.getJSONObject(RESULT);
                        if (!jsonTResult.getBoolean("valid")) {
                            log.info("不是有效数据");
                            continue;
                        }
                        int propertyidResult = jsonTResult.getIntValue("propertyid");
                        if (propertyidResult!=propertyId) {
                            log.info("非USDT数据");
                            continue;
                        }
                        //更爱wallet_platform_out的提现状态

                        WalletUsdtResponseDTO wrd = JSON.parseObject(listObject.getString("result"), WalletUsdtResponseDTO.class);
                        //存入热钱包记录
                        if(wrd!=null && wrd.getReferenceaddress()!=null && addressmap.get(wrd.getReferenceaddress()) !=null){
                          insertHotWallet(wrd);
                            getblockscanList(wrd);
                        }
                        //clod->平台预设地址 冷钱包地址vin中，冷钱包和热钱包地址都不在vout中
                        if(wrd!=null && wrd.getSendingaddress()!=null && addressColdMap.get(wrd.getSendingaddress()) !=null && addressColdMap.get(wrd.getReferenceaddress()) ==null && addressPlatformMap.get(wrd.getReferenceaddress()) ==null && addressPreinstallMap.get(wrd.getSendingaddress()) ==null &&
                                addressPreinstallMap.get(wrd.getReferenceaddress())!=null){
                            wrd.setType("1");
                            insertColdWallet(wrd);
                        }
                        //clod->提币热钱包  冷钱包在vin中，热钱包在vout中
                        if(wrd!=null && wrd.getSendingaddress()!=null && addressColdMap.get(wrd.getSendingaddress()) !=null && addressPlatformMap.get(wrd.getReferenceaddress()) !=null){
                            wrd.setType("2");
                            insertColdWallet(wrd);
                        }
                        //未知地址->cold 未知地址在vin中,冷钱包地址在vout中
                        if(wrd!=null && wrd.getReferenceaddress()!=null && addressColdMap.get(wrd.getReferenceaddress()) !=null && addressColdMap.get(wrd.getSendingaddress()) ==null && addressPlatformMap.get(wrd.getSendingaddress()) ==null){
                            wrd.setType("3");
                            insertColdWallet(wrd);
                        }
                        //未知地址->提币热钱包 未知地址在vin中,热钱包地址在vout中
                        if(wrd!=null && wrd.getReferenceaddress()!=null && addressPlatformMap.get(wrd.getReferenceaddress()) !=null && addressColdMap.get(wrd.getSendingaddress()) ==null && addressPlatformMap.get(wrd.getSendingaddress()) ==null){
                             wrd.setType("4");
                            insertColdWallet(wrd);
                        }

                    }
                }
                //getblockscanList(lastBlockHeight);
                //更新记录的区块高度
               if(blockheight(lastBlockHeight)) {
                   log.info("剩余多少块未扫：{}",height - lastBlockHeight);
               }
            }
            log.info("扫块结束时间：{}",System.currentTimeMillis() - startTime);
            return listusdt;
        }catch (Exception e){
            e.printStackTrace();
            log.error("扫块数据插入失败:{}",listusdt);
            return listusdt;
        }

    }

    /**
     *查询walletHotEntry
     */
    private List<walletHotEntry> getList(walletHotEntry whe){
        List<walletHotEntry> list=new ArrayList<>();
        try {
            list=walletHotEntryMapper.selectWalletHotEntry(whe);
            return list;
        }catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return list;
        }
    }

    /**
     *查询walletcoldEntry
     */
    private List<walletPlatformEntry> getColdList(walletPlatformEntry whe){
        List<walletPlatformEntry> list=new ArrayList<>();
        try {
            list=walletPlatformEntryMapper.selectWalletColdEntry(whe);
            return list;
        }catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return list;
        }
    }

    /**
     *查询walletHotEntry最后的高度
     */
    @Override
    public int lastBlockHeight(){
        try {
            walletHotEntry whe = new walletHotEntry();
          //  walletHotEntry info = walletHotEntryMapper.selectWalletHotEntryInfo(whe);
            WalletBlockHeightPO info=WalletBlockHeightPOMapper.selectByPrimaryKey(1);
            return info.getBlockHeigth();
        }catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return 0;
        }
    }

    /**
     * 查询数据库tx_id数否重复
     */
    private Boolean duplicate(){
        try {
            walletHotEntry whe = new walletHotEntry();
            walletHotEntry entry= walletHotEntryMapper.selectWalletHotEntryByCount(whe);
            if(entry !=null){
                return false;
            }else{
                return true;
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return false;
        }

    }

    /**
     * 获取tx_id 对应的确认数
     */
    private int getConfirmations(String txid){
        try {
            int confirmations=0;
            //当前区块高度
            int height=this.getBlockCount();
            //更具tx_id查询交易信息
            JSONObject listObject=this.txDetails(txid);
            if(listObject !=null && listObject.getString("result") !=null){
                WalletUsdtResponseDTO wrd = JSON.parseObject(listObject.getString("result"), WalletUsdtResponseDTO.class);
                 confirmations=height-(wrd.getBlock().intValue())+1;
            }
            return confirmations;
        }catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return 0;
        }
    }

    public static void main(String args[]) {
       // UsdtServiceImpl us = new UsdtServiceImpl();
     //   String s = us.send("14qyp7htJrQmxQiWfKJqpstryuMobKPwVY", "1.0", "13ZG51mgV9sJ9MHsQYG7dxmsEShT5pUUrX");

        System.out.println("=========ceshi kaishi ====");

        for(int i=0;i<6;i++){
            if(i==3) continue;
            System.out.println("i="+i);
        }

        //System.out.println(s);
    }


    /**
     * /doRequest("omni_listblocktransactions",279007);
     *         //{"result":["63d7e22de0cf4c0b7fd60b4b2c9f4b4b781f7fdb8be4bcaed870a8b407b90cf1","6fb25ab84189d136b95d7f733b0659fa5fbd63f476fb1bca340fb4f93de6c912","d54213046d8be80c44258230dd3689da11fdcda5b167f7d10c4f169bd23d1c01"],"id":"1521454868826"}
     * @param index
     * @return
     */
    @Override
    public boolean parseBlock(int index) {

        JSONObject jsonBlock = doRequest(METHOD_GET_LISTBLOCKTRANSACTIONS, index);
        if (isError(jsonBlock)) {
            log.error("访问USDT出错");
            return false;
        }
        JSONArray jsonArrayTx = jsonBlock.getJSONArray(RESULT);
        if (jsonArrayTx == null || jsonArrayTx.size() == 0) {
            //没有交易
            return true;
        }
        Iterator<Object> iteratorTxs = jsonArrayTx.iterator();
        while(iteratorTxs.hasNext()){
            String txid = (String) iteratorTxs.next();
            parseTx(txid,null);
        }
        return true;
    }

    /**
     * 数据库中的高度
     */

    private Boolean blockheight(int lastBlockHeight){

        WalletBlockHeightPO wbp=new WalletBlockHeightPO();
        wbp.setBlockHeigth(lastBlockHeight);
        wbp.setUpdateTime(System.currentTimeMillis());
        wbp.setId(1);
        try {
            WalletBlockHeightPO bpo= WalletBlockHeightPOMapper.selectByPrimaryKey(1);
            if(bpo !=null){
                WalletBlockHeightPOMapper.updateByPrimaryKey(wbp);
            }else{
                WalletBlockHeightPOMapper.insertSelective(wbp);
            }
            return  true;
        }catch (Exception e){
            e.printStackTrace();
            log.error("更新区块高度失败：{}",e.getMessage());
            return  false;
        }

    }


    /**
     * wallet_hot block扫块信息
     */
    public WalletUsdtMsgDTO getblockscanList( WalletUsdtResponseDTO wrd){
        WalletUsdtMsgDTO dto=new WalletUsdtMsgDTO();
        WalletHotPO record =new WalletHotPO();
        walletHotEntry whe=new walletHotEntry();
        List<WalletHotPO> list =new ArrayList<>();
        try {
           // dto.setHeight(wrd.getBlock().intValue());
            dto.setTxid(wrd.getTxid());
            dto.setTimestamp(wrd.getBlocktime());
            dto.setWalletAddressHash(wrd.getReferenceaddress());
            dto.setAmount(Long.parseLong(new DecimalFormat().format(wrd.getAmount()*Math.pow(10,8)).replaceAll(",","")));
            dto.setTimestamp(wrd.getBlocktime());
            list=walletHotPOMapper.selectWalletAccount(record.getUserName(),record.getAccountType(),wrd.getReferenceaddress());
            if(list !=null &&list.size()>0){
                dto.setUserName(list.get(0).getUserName());
                dto.setAccountType(list.get(0).getAccountType());
            }
            WalletBlockScanApply.sendBlockScan(dto);
            return dto;
        }catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

        return dto;

    }


    /**
     * 处理未成功放入队列中的消息
     *
     */
    @Override
    public int sendRabbitMq(){
        walletHotEntry record =new walletHotEntry();
        List<WalletUsdtMsgDTO> listdto=new ArrayList<>();
        int count=0;
        try {
            record.setMsgState("1");
            listdto=walletHotEntryMapper.selectWalletHotEntryByblockScan(record);
            if(listdto !=null && listdto.size()>0){
                for (WalletUsdtMsgDTO dto: listdto) {
                    dto.setAmount(Long.parseLong(new DecimalFormat().format(dto.getAmount()*Math.pow(10,8)).replaceAll(",","")));
                    WalletBlockScanApply.sendBlockScan(dto);
                }
            }
            count=listdto.size();
            return listdto.size();
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
        }

        return count;
    }


    /**
     * @param wrd
     */
    private void insertHotWallet(WalletUsdtResponseDTO wrd){

        walletHotEntry whe=new walletHotEntry();
        try{
            whe.setWalletAddressHash(wrd.getReferenceaddress());
            whe.setCreateTime(System.currentTimeMillis());
            whe.setHeight(wrd.getBlock().intValue());
            whe.setAmount(wrd.getAmount().toString());
            whe.setTimestamp(wrd.getBlocktime());
            whe.setFromWalletAddress(wrd.getSendingaddress().trim());
            whe.setAccountType("USDT");
            BeanUtils.copyProperties(wrd,whe);
            whe.setTxid(wrd.getTxid().trim());

            //存入数据库
            if(this.getList(whe) !=null && this.getList(whe).size()>0){
                whe.setEntryId(this.getList(whe).get(0).getEntryId());
                walletHotEntryMapper.updateByPrimaryKey(whe);
            }else{
                walletHotEntryMapper.insert(whe);
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("存入数据失败：{}",e.getMessage());
        }

    }

    /**
     *
     * @param wrd
     */
    private void insertColdWallet(WalletUsdtResponseDTO wrd){

        walletPlatformEntry wpn=new walletPlatformEntry();
        try{
            wpn.setWalletAddressHash(wrd.getReferenceaddress());
            wpn.setCreateTime(System.currentTimeMillis());
            wpn.setAmount(wrd.getAmount().toString());
            wpn.setTimestamp(wrd.getBlocktime());
            wpn.setAccountType("USDT");
            wpn.setFromWalletAddress(wrd.getSendingaddress());
            wpn.setSource("2");
            wpn.setType(wrd.getType());
            wpn.setActualFee(Long.parseLong(new DecimalFormat().format(wrd.getFee()*Math.pow(10,8)).replace(",","")));
            BeanUtils.copyProperties(wrd,wpn);

            //wpn
            if(this.getColdList(wpn) !=null && this.getColdList(wpn).size()>0){
                wpn.setEntryId(this.getColdList(wpn).get(0).getEntryId());
                walletPlatformEntryMapper.updateByPrimaryKeySelective(wpn);
            }else{
                walletPlatformEntryMapper.insert(wpn);
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("存入数据失败：{}",e.getMessage());
        }

    }

    /**
     * 更改wallet_platform_out的提现状态
     */
    public void  UpdateWalletPlatformOut(String txid){
        walletPlatformOut walletPlatformOut =new walletPlatformOut();
        walletPlatformOut.setTxid(txid);
        try {
            walletPlatformOutMapper.updateByPrimaryKey(walletPlatformOut);
        }catch (Exception e){
            e.printStackTrace();
            log.error("更新数据失败：{}",e.getMessage());
        }

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

    //wallet_hot
    private  Map<String,Object> getHotAddress(){
        Map<String,Object> addressmap=new HashMap<>();
        WalletAccountPO wap =new WalletAccountPO();
        List<WalletHotPO> list=walletHotPOMapper.selectWalletAccount(wap.getUserName(),"USDT",wap.getWalletAddressHash());
        //平台中的地址
        if(list !=null && list.size()>0){
            for (WalletHotPO wp: list) {
                addressmap.put(wp.getWalletAddressHash(),wp);
            }
        }

        return addressmap;
    }

    //wallet_cold
    private Map<String,Object> getColdAddress(){
        Map<String,Object> addressColdMap=new HashMap<>();
        List<WalletCold> listCold=walletColdMapper.selectWalletAccount(new WalletCold());
        //平台中的地址
        if(listCold !=null && listCold.size()>0){
            for (WalletCold w: listCold) {
                addressColdMap.put(w.getWalletAddressHash(),w);
            }
        }
       return  addressColdMap;
    }

    //wallet_plate
    private Map<String,Object> getPlatAddress(){
        Map<String,Object> addressPlatformMap=new HashMap<>();
        List<walletPlatform> listPlat=walletPlatformMapper.selectwalletPlatformList(new walletPlatform());
        //平台中的地址
        if(listPlat !=null && listPlat.size()>0){
            for (walletPlatform wp: listPlat) {
                addressPlatformMap.put(wp.getWalletAddressHash(),wp);
            }
        }
        return  addressPlatformMap;

    }

    private Map<String,Object> getPreinstall(){
        Map<String,Object> addressPreinstllMap=new HashMap<>();
        List<WalletPreinstallAddress> listPlat=walletPreinstallAddressMapper.selectWalletPreinstallAddressmList(new WalletPreinstallAddress());
        //平台中的地址
        if(listPlat !=null && listPlat.size()>0){
            for (WalletPreinstallAddress wp: listPlat) {
                addressPreinstllMap.put(wp.getWalletAddressHash(),wp);
            }
        }
        return  addressPreinstllMap;

    }
}