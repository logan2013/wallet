package com.blockchain.wallet.api.utils;

import com.blockchain.wallet.api.po.WalletBlockHeightPO;
import com.blockchain.wallet.api.service.UsdtServie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class blockscanjob {

    private Logger log = LoggerFactory.getLogger(blockscanjob.class);
    @Autowired
    private UsdtServie usdtServie;

    @Autowired
    private com.blockchain.wallet.api.dao.WalletBlockHeightPOMapper WalletBlockHeightPOMapper;

    public void run(){
        usdtJob();
    }

    /**
     * USDT处理
     */
    private void usdtJob(){
        Long startTime = System.currentTimeMillis();

        //这一步获取自己系统存的钱包同步的高度
       int blockParseedCount = usdtServie.lastBlockHeight();
       if(blockParseedCount == 0)return;
        //获取钱包的高度
        int blockCount = usdtServie.getBlockCount();

        log.info("USDT当前高度是：{} 处理记录高度：{}",blockCount,blockParseedCount);
        WalletBlockHeightPO wbp=new WalletBlockHeightPO();
        wbp.setId(1);
        if(blockCount>blockParseedCount){
            //还有没有处理完的区块， 继续处理
            int index = blockParseedCount+1;
            while(index <= blockCount){
                try {
                    if(usdtServie.parseBlock(index)){
                        index++;
                    }else{
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if(index == blockCount){
                //扫块完成
                wbp.setBlockHeigth(index);
                wbp.setUpdateTime(System.currentTimeMillis());
                WalletBlockHeightPOMapper.updateByPrimaryKeySelective(wbp);
                log.info("扫块结束时间：{}",System.currentTimeMillis() - startTime);
            }else{
                wbp.setBlockHeigth(index-1);
                wbp.setUpdateTime(System.currentTimeMillis());
                WalletBlockHeightPOMapper.updateByPrimaryKeySelective(wbp);
            }
        }
    }


}
