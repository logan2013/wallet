package com.blockchain.wallet.api.retry;

import com.blockchain.wallet.api.business.WalletAccountBusiness;
import com.blockchain.wallet.api.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

/**
 * @author shujun
 * @date 2018/7/12
 */
@Service
@Slf4j
public class WalletAccountRetry {

    @Autowired
    WalletAccountBusiness business;

    @Retryable(value = {BizException.class},maxAttempts = 4,backoff = @Backoff(delay = 2000,  multiplier = 1))
    public void initializeWalletAccount(){

    }

    @Recover
    public void recover(BizException e){
       // business.initWalletAccount(po);
    }

}
