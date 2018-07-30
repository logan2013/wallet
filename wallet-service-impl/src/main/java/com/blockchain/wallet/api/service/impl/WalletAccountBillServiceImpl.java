package com.blockchain.wallet.api.service.impl;


import com.blockchain.wallet.api.dao.WalletAccountBillPOMapper;

import com.blockchain.wallet.api.po.WalletAccountBillPO;
import com.blockchain.wallet.api.service.WalletAccountBillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author shujun
 * @date 2018/6/28
 */

@Component
@Slf4j
public class WalletAccountBillServiceImpl implements WalletAccountBillService {

    @Autowired
    WalletAccountBillPOMapper mapper;

    @Override
    public int selectWalletAccBillCount(String username, String addr, String accType, String orderNo) {
        return mapper.selectWalletAccBillByPo(username ,addr ,accType ,orderNo);
    }

    @Override
    public int insertChargeBill(WalletAccountBillPO po) {
        return mapper.insertSelective(po);
    }

    @Override
    public WalletAccountBillPO selectByBlockId(String blockTxid) {
        return mapper.selectByBlockId(blockTxid);
    }
}
