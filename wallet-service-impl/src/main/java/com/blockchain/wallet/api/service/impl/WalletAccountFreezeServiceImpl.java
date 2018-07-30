package com.blockchain.wallet.api.service.impl;

import com.blockchain.wallet.api.dao.WalletAccountFreezePOMapper;
import com.blockchain.wallet.api.po.WalletAccountFreezePO;
import com.blockchain.wallet.api.service.WalletAccountFreezeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 *
 * @author shujun
 * @date 2018/6/28
 */
@Component
@Slf4j
public class WalletAccountFreezeServiceImpl implements WalletAccountFreezeService {

    @Autowired
    WalletAccountFreezePOMapper mapper;

    @Override
    public WalletAccountFreezePO selectByBizNo(String bizNo) {
        return mapper.selectByBizNo(bizNo);
    }

    @Override
    public int insertSelective(WalletAccountFreezePO record) {
        return mapper.insertSelective(record);
    }

    @Override
    public int updateByfreezeNo(WalletAccountFreezePO record) {
        return mapper.updateByfreezeNo(record);
    }

    @Override
    public WalletAccountFreezePO selectByFreezeNo(String freezeNo) {
        return mapper.selectByFreezeNo(freezeNo);
    }

    @Override
    public WalletAccountFreezePO selectByPrimaryKeyLock(Long freezeId) {
        return mapper.selectByPrimaryKeyLock(freezeId);
    }

    @Override
    public int updateByPrimaryKeySelective(WalletAccountFreezePO record) {
        return mapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public List<WalletAccountFreezePO> selectWithdrawBillRetryPos(Long times, String msgState) {
        return mapper.selectWithdrawBillRetryPos(times , msgState);
    }
}
