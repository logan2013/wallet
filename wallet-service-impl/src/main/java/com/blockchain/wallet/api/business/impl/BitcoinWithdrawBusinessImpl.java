package com.blockchain.wallet.api.business.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.blockchain.wallet.api.business.BitcoinWithdrawBusiness;
import com.blockchain.wallet.api.service.BitcoinService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.*;

//bitcoin 提现业务逻辑
public class BitcoinWithdrawBusinessImpl implements BitcoinWithdrawBusiness {

    @Autowired
    private BitcoinService bitcoinService;

    // btc提现
    public String withdraw(String receiveAddress, double amount, String sendAddress) {
        Map<String, Object> map = new HashMap<>();// vin 详情
        ArrayList<Map<String, Object>> inputs = new ArrayList<>();//输入列表
        Map<String, Object> outputs = new HashMap<>(); // 输出列表
        ArrayList<Map<String, Object>> prevtxs = new ArrayList<>();//utxo 详情
        ArrayList<String> privkeys = new ArrayList<>();// 私钥

        JSONObject unspent = bitcoinService.listUnspent(sendAddress);
        JSONArray ja = unspent.getJSONArray("result");
        if (ja.size() < 1) {
            // todo 平台钱包没有钱
            return null;
        }
        if (ja.size() == 1) {
            if(ja.getJSONObject(0).getDoubleValue("amount") < amount + 0.00001) {
                // todo 平台钱包没有钱
                return null;
            }
            map.put("txid", ja.getJSONObject(0).getString("txid"));
            map.put("vout", ja.getJSONObject(0).getIntValue("vout"));
            inputs.add(map);
            outputs.put(receiveAddress, amount);
            outputs.put(sendAddress, ja.getJSONObject(0).getDoubleValue("amount") - amount - 0.00001);// change amount
            String hex = bitcoinService.createRawTransaction(inputs, outputs);
            map.put("scriptPubKey", ja.getJSONObject(0).getString("scriptPubKey"));
            prevtxs.add(map);
            // todo 数据库读取私钥
            privkeys.add(""); //
            JSONObject signature = bitcoinService.signRawTransaction(hex, prevtxs, privkeys);
            String broadcastHex = bitcoinService.sendRawTx(signature.getString("hex"));
            return broadcastHex;
        }

        // 遍历vin 并从小到排序
        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
        for (int i = 0; i < ja.size(); i++) {
            jsonValues.add(ja.getJSONObject(i));
        }
        Collections.sort(jsonValues, new Comparator<JSONObject>() {
            private static final String KEY_NAME = "amount";

            @Override
            public int compare(JSONObject a, JSONObject b) {
                Double valA;
                Double valB;
                valA = a.getDouble(KEY_NAME);
                valB = b.getDouble(KEY_NAME);

                return valA.compareTo(valB);
                //if you want to change the sort order, simply use the following: return -valA.compareTo(valB);
            }
        });

        // address中总余额
        double balance = 0.0;
        for (JSONObject tmp : jsonValues) {
            balance += tmp.getDoubleValue("amount");
        }
        if (balance < amount + 1000) {
            // todo 余额不足
            return null;
        }
        // 选取合适vin
        int size = jsonValues.size();
        double min = jsonValues.get(0).getDoubleValue("amount");// min point
        double max = jsonValues.get(size).getDoubleValue("amount");// max point
        List<JSONObject> selectedVin = new ArrayList<JSONObject>();
        // todo 选取合适的vin
        //case 1  min< amount < max
        if(amount + 0.00001> min && amount+0.00001 < max){
            for(int i = 0; i<jsonValues.size();i++){
                if(amount+ 0.00001 > jsonValues.get(i).getDoubleValue("amount") ){
                    selectedVin.add(jsonValues.get(i));
                }
            }
        }
        // case2 amount > max
        double added = 0.0;
        for(int j = 0; j< jsonValues.size()-1 ; j++){
            added = max+jsonValues.get(j).getDoubleValue("amount");
            if(added < amount+0.001){
                selectedVin.add(jsonValues.get(j));
            }
        }
        // selectedVin
        double total = 0.0;
        for(int y =0; y< selectedVin.size(); y++){
            total += selectedVin.get(y).getDoubleValue("amount");
            Map vin = new HashMap();
            vin.put("txid", selectedVin.get(y).getString("txid"));
            vin.put("vout", selectedVin.get(y).getIntValue("vout"));
            inputs.add(vin);
            vin.put("scriptPubKey", selectedVin.get(y).getString("scriptPubKey"));
            prevtxs.add(map);
        }

        outputs.put(receiveAddress, amount);
        outputs.put(sendAddress,  total - amount - txFee(selectedVin.size(),1,6));// change amount
        String hex = bitcoinService.createRawTransaction(inputs, outputs);
        privkeys.add(""); //
        JSONObject signature = bitcoinService.signRawTransaction(hex, prevtxs, privkeys);
        String broadcastHex = bitcoinService.sendRawTx(signature.getString("hex"));
        return broadcastHex;
    }


    // 计算花费的手续费
    private double txFee(int vin, int vout, int delayBlock) {
        BigDecimal feePerKB;
        if (vin < 1 || vout < 1) {
            return 0.0;
        }

        double fee = bitcoinService.estimateFee(delayBlock);
        if (fee == 0.0) {
            // todo 读取配置文件中的费用, 两个费用做比较区取小值
        }
        feePerKB = new BigDecimal(fee + "");
        double totalFee = BigDecimal.valueOf(0.14 * vin + 0.037 * vout).multiply(feePerKB).doubleValue();
        return totalFee;
    }


}
