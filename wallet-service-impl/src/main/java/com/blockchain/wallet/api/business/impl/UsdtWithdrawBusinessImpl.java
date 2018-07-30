package com.blockchain.wallet.api.business.impl;

import com.alibaba.fastjson.JSONObject;
import com.blockchain.wallet.api.business.UsdtWithdrawBusiness;
import com.blockchain.wallet.api.dao.walletPlatformMapper;
import com.blockchain.wallet.api.dao.walletPlatformOutMapper;
import com.blockchain.wallet.api.po.walletPlatform;
import com.blockchain.wallet.api.po.walletPlatformOut;
import com.blockchain.wallet.api.service.UsdtServie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Slf4j
@Service
public class UsdtWithdrawBusinessImpl implements UsdtWithdrawBusiness {

    @Autowired
    private walletPlatformOutMapper walletPlatformOutMapper;
    @Autowired
    private UsdtServie usdtServie;
    @Autowired
    private walletPlatformMapper walletPlatformMapper;

    @Value("${spring.usdt.withdrawmin}")
    private double withdrawmin; //最大提现金额

    @Value("${spring.usdt.withdrawmax}")
    private double withdrawmax; //最大提现金额

    @Override
    public int withdraw(String receiveAddress, String amount, String withdrawNo, int limit) {

        List<walletPlatform> wpList = walletPlatformMapper.selectwalletPlatformList(new walletPlatform());
        if(wpList.size() < 1 ) return 0;
        walletPlatform wp = wpList.get(0);
        String sendAddress = wp.getWalletAddressHash();
        JSONObject balance = usdtServie.balanceByAddress(sendAddress);
        if (balance.isEmpty()) {
            // todo 平台地址没有导入钱包
            log.error("平台钱包地址没有导入钱包");
            return 0;
        }
        if (balance.getJSONObject("result").getDoubleValue("balance") < Double.parseDouble(amount)) {
            //todo 平台金额不足
            log.error("平台钱包地址没有足够的余额");
            return 0;
        }

        walletPlatformOut wo = walletPlatformOutMapper.selectByWithdrawNo(withdrawNo);
        if(wo == null){
            //添加提现申请记录
            walletPlatformOut wpo = new walletPlatformOut();
            wpo.setWalletAddressHash(sendAddress);
            wpo.setAccountType("USDT");
            wpo.setToWalletAddress(receiveAddress);
            wpo.setCreateTime(System.currentTimeMillis());
            wpo.setWithdrawNo(withdrawNo);//对应提现交易号
            wpo.setAmount(amount);
            wpo.setVersion("1");
            wpo.setWithdrawState("1");
            walletPlatformOutMapper.insertSelective(wpo);
            wo = walletPlatformOutMapper.selectByWithdrawNo(withdrawNo);
        }

        //提币
        String txid= "";
        try{
            txid = usdtServie.send(receiveAddress, amount, sendAddress);
            if(txid.equals("") || txid ==null){
                log.error("提币交易失败");
                return 0;
            }
        }catch (Exception e ){
            log.error("提币交易失败 : "+e.getMessage());
        }
        JSONObject jo = usdtServie.txDetails(txid);
        walletPlatformOut update = new walletPlatformOut();
        update.setOutId(wo.getOutId());
        update.setTxid(txid);
        update.setActualFee(jo.getJSONObject("result").getString("fee"));
        update.setWithdrawState("2");// 交易发送成功
        update.setWithdrawLimit(++limit);
        int res = walletPlatformOutMapper.updateByPrimaryKeySelective(update);
        return res;
    }

    @Override
    public walletPlatformOut withdrawLimit(String withdrawNo){
        walletPlatformOut wpo = walletPlatformOutMapper.selectByWithdrawNo(withdrawNo);
        return wpo;
    }

}
