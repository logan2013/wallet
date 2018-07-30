package com.blockchain.wallet.api.service.impl;

import com.blockchain.wallet.api.dao.WalletAccountPOMapper;
import com.blockchain.wallet.api.exception.BizException;
import com.blockchain.wallet.api.po.WalletAccountPO;
import com.blockchain.wallet.api.service.WalletAccountService;
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
public class WalletAccountServiceImpl implements WalletAccountService {

    @Autowired
    WalletAccountPOMapper mapper;

    @Override
    public int insertWalletAccount(WalletAccountPO record) {
        return mapper.insertSelective(record);
    }

    @Override
    public boolean isExsitAccount(String userName, String walletAddress, String walletType) {
        WalletAccountPO p = new WalletAccountPO();
        p.setAccountType(walletType);
        p.setUserName(userName);
        p.setWalletAddressHash(walletAddress);
        int i = mapper.selectAccountByPo(p);
        if(i==0){
            return false;
        }
        return true;
    }

    @Override
    public List<WalletAccountPO> selectWalletAccount(String userName, String accountType, String walletAddressHash)
            throws BizException {
        List<WalletAccountPO> res = mapper.selectWalletAccount(userName,  accountType,  walletAddressHash);
        if(res.isEmpty()){
            log.warn("no account . walletAddressHash = [{}] " , walletAddressHash);
            throw BizException.ACCOUNT_IS_NOT_EXSITS;
        }
        if(res.size()>1){
            log.warn("record error  walletAddressHash : {}  ,   size =  [{}] " , walletAddressHash ,res.size());
            BizException biz = BizException.ACCOUNT_VALIDATE_ERROR;
            biz.setMessage("walletAddressHash = "+ walletAddressHash + " , db record size > 1 ");
            throw biz;
        }
        return res;
    }

    @Override
    public WalletAccountPO selectByPrimaryKeyLockRow(Long id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(WalletAccountPO record) {
        return mapper.updateByPrimaryKeySelective(record);
    }
}
