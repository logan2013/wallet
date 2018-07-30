package com.blockchain.wallet.api.business.impl;

import com.blockchain.wallet.api.WalletApiException;
import com.blockchain.wallet.api.business.WalletAccountBusiness;
import com.blockchain.wallet.api.common.BizBillFlowType;
import com.blockchain.wallet.api.common.BizTypeConstants;
import com.blockchain.wallet.api.dto.*;
import com.blockchain.wallet.api.internal.util.JSON;
import com.blockchain.wallet.api.internal.util.SnowflakeIdWorker;
import com.blockchain.wallet.api.mq.WalletWithdrawApply;
import com.blockchain.wallet.api.po.WalletAccountBillPO;
import com.blockchain.wallet.api.po.WalletAccountFreezePO;
import com.blockchain.wallet.api.po.WalletAccountPO;
import com.blockchain.wallet.api.request.WalletRsaResponse;
import com.blockchain.wallet.api.service.*;
import com.blockchain.wallet.api.utils.CreateAddress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * @author shujun
 * @date 2018/7/12
 */
@Component
@Slf4j
@Transactional(readOnly = true)
public class WalletAccountBusinessImpl implements WalletAccountBusiness {

    @Autowired
    WalletAccountService walletAccountService;

    @Autowired
    WalletHotService walletHotService;

    @Autowired
    WalletAccountFreezeService freezeService;

    @Autowired
    WalletAccountBillService walletAccountBillService;

    @Autowired
    CreateAddress address;

    @Autowired
    SnowflakeIdWorker uuid;

    @Override
    @Transactional(readOnly = false , propagation = Propagation.REQUIRED)
    public void initWalletAccount(WalletAccountPO po) {
       boolean flg = walletAccountService.isExsitAccount(po.getUserName(),po.getWalletAddressHash(),po.getAccountType());
       if(flg==false){
           walletAccountService.insertWalletAccount(po);
       }
    }

    /**
     *  1. 校验用户合法性
     *  2. 检查订单是否已经处理过
     *  3. 处理订单
     * @param request
     * @return
     * @throws WalletApiException
     */
    @Override
    @Transactional(readOnly = false , rollbackFor =WalletApiException.class , propagation = Propagation.REQUIRED)
    public WalletRsaResponse rechargeOnLine(WalletApiRechargeRequestDTO request) throws WalletApiException {
        int i = walletAccountBillService.selectWalletAccBillCount(request.getAccUserName() ,request.getWalletAddress(),
                request.getWalletType() ,request.getOrderNo());
        if(i>0){
            log.warn("orderNo already process. orderNo={} " , request.getOrderNo());
            // 重复订单 处理
            throw new WalletApiException(5001 ,"orderNo already process.");
        }
        boolean flg = walletHotService.isExsitWalletHot(request.getAccUserName(),request.getWalletAddress(),request.getWalletType());
        if(flg == false){
            log.warn("invalid user message. username={} , walletAddress = {} , walletType={} " , request.getAccUserName(),request.getWalletAddress(),request.getWalletType());
            throw new WalletApiException("invalid user message.");
        }
        // 需要将此记录加锁
        List<WalletAccountPO> res = walletAccountService.selectWalletAccount(request.getAccUserName() ,request.getWalletType() ,request.getWalletAddress());
        Long walletId = res.get(0).getWalletId();
       // Long freezeBalance = res.get(0).getFreezBalance();
        res = null;
        WalletAccountPO po = walletAccountService.selectByPrimaryKeyLockRow(walletId);
        WalletAccountBillPO bill = new WalletAccountBillPO();
        bill.setAccountType(request.getWalletType());
        bill.setWalletId(walletId);
        bill.setBizType(BizTypeConstants.BIZ_CHARGE.getCode());
        Long timeS = System.currentTimeMillis();
        bill.setCreateTime(timeS);
        bill.setTxid(BizTypeConstants.BIZ_CHARGE.getMessage() + uuid.nextId());
        bill.setOrderNo(request.getOrderNo());
        bill.setOrderAmount(request.getValue());
        bill.setFlowType(BizBillFlowType.FLOWTYPE_INCOME);
        bill.setSource(BizBillFlowType.FLOWTYPE_INCOME_SOURCE_PT);
        bill.setBeforeBalance(po.getBalance());
        bill.setBalance(po.getBalance() + request.getValue());
        walletAccountBillService.insertChargeBill(bill);
        WalletAccountPO acc = new WalletAccountPO();
        acc.setWalletId(walletId);
        acc.setBalance(bill.getBalance());
        acc.setLastModifyTime(timeS);
        walletAccountService.updateByPrimaryKeySelective(acc);
       // WalletHotAccountDTO hot = walletHotAccountService.selectWalletHotByAddr(request.getWalletAddress());
        WalletRsaResponse resp = WalletRsaResponse.success();
        WalletApiRechargeResponseDTO dto = new WalletApiRechargeResponseDTO();
        dto.setTxid(bill.getTxid());
        dto.setBalance(acc.getBalance());
        dto.setFreezeBalance(po.getFreezBalance());
        dto.setAccUserName(request.getAccUserName());
        dto.setWalletAddress(request.getWalletAddress());
        dto.setWalletType(request.getWalletType());
        resp.setTimestamp(timeS);
        resp.setSignType(request.getSignType());
        String jsonString = JSON.toJsonString(dto);
        //jsonString = KeyExchangeAlgorithmEncrypt.encryptContent(jsonString,hot.getSecretKey(),WalletApiConstants.CHARSET_UTF8);
        resp.setBizContent(jsonString);
        //String sign = WalletSignature.rsaSign(JSON.toJsonString(resp) , hot.getWalletPrivateKey(), WalletApiConstants.CHARSET_UTF8, request.getSignType());
        //resp.setSign(sign);
        return resp;
    }

    @Override
    @Transactional(readOnly = false , rollbackFor =WalletApiException.class , propagation = Propagation.REQUIRED)
    public WalletRsaResponse cashTrade(WalletApiCashTradeRequestDTO request) throws WalletApiException {
        int i = walletAccountBillService.selectWalletAccBillCount(request.getAccUserName() ,request.getWalletAddress(),
                request.getWalletType() ,request.getOrderNo());
        if(i>0){
            log.warn("orderNo already process. orderNo={} " , request.getOrderNo());
            // 重复订单 处理
            throw new WalletApiException(5001 ,"orderNo already process.");
        }
        boolean flg = walletHotService.isExsitWalletHot(request.getAccUserName(),request.getWalletAddress(),request.getWalletType());
        if(flg == false){
            log.warn("invalid user message. username={} , walletAddress = {} , walletType={} " , request.getAccUserName(),request.getWalletAddress(),request.getWalletType());
            throw new WalletApiException("invalid user message.");
        }

        flg = walletHotService.isExsitWalletHot(request.getToAccUserName(),request.getToAccAddress(),request.getWalletType());
        if(flg == false){
            log.warn("invalid user message. username={} , walletAddress = {} , walletType={} " , request.getAccUserName(),request.getWalletAddress(),request.getWalletType());
            throw new WalletApiException("invalid user message.");
        }
        List<WalletAccountPO> res = walletAccountService.selectWalletAccount(request.getAccUserName() ,request.getWalletType() ,request.getWalletAddress());
        Long walletId = res.get(0).getWalletId();
        WalletAccountPO po = walletAccountService.selectByPrimaryKeyLockRow(walletId);
        if(po.getBalance() - po.getFreezBalance() - request.getValue() <0){
            log.warn(" username={} , balance is not enough " , request.getAccUserName());
            throw new WalletApiException("username:"+request.getAccUserName()+" , balance is not enough.");
        }

        List<WalletAccountPO> other = walletAccountService.selectWalletAccount(request.getToAccUserName() ,request.getToAccAddress() ,request.getWalletAddress());
        WalletAccountPO toAccount = walletAccountService.selectByPrimaryKeyLockRow(other.get(0).getWalletId());

        // 账户支出
        WalletAccountBillPO bill = new WalletAccountBillPO();
        bill.setAccountType(request.getWalletType());
        bill.setWalletId(po.getWalletId());
        // 交易
        bill.setBizType(BizTypeConstants.BIZ_TRADE_ONLINE.getCode());
        Long timeS = System.currentTimeMillis();
        bill.setCreateTime(timeS);
        bill.setTxid(BizTypeConstants.BIZ_TRADE_ONLINE.getMessage() + uuid.nextId());
        bill.setOrderNo(request.getOrderNo());
        bill.setOrderAmount(request.getValue());
        bill.setFlowType(BizBillFlowType.FLOWTYPE_OUTCOME);
        bill.setBeforeBalance(po.getBalance());
        bill.setBalance(po.getBalance() - request.getValue());
        walletAccountBillService.insertChargeBill(bill);

        po.setBalance(po.getBalance() - request.getValue());
        po.setLastModifyTime(timeS);
        walletAccountService.updateByPrimaryKeySelective(po);


        // 账户收入
        WalletAccountBillPO bill2 = new WalletAccountBillPO();
        bill2.setAccountType(request.getWalletType());
        bill2.setWalletId(toAccount.getWalletId());
        // 交易
        bill2.setBizType(BizTypeConstants.BIZ_TRADE_ONLINE.getCode());
        bill2.setCreateTime(timeS);
        bill2.setTxid(bill.getTxid());
        bill2.setOrderNo(request.getOrderNo());
        bill2.setOrderAmount(request.getValue());
        bill2.setFlowType(BizBillFlowType.FLOWTYPE_INCOME);
        bill2.setSource(BizBillFlowType.FLOWTYPE_INCOME_SOURCE_JY);
        bill2.setBeforeBalance(toAccount.getBalance());
        bill2.setBalance(toAccount.getBalance() + request.getValue());
        walletAccountBillService.insertChargeBill(bill2);

        toAccount.setBalance(toAccount.getBalance() + request.getValue());
        toAccount.setLastModifyTime(timeS);
        walletAccountService.updateByPrimaryKeySelective(toAccount);


        WalletRsaResponse resp = WalletRsaResponse.success();
        WalletApiCashTradeResponseDTO dto = new WalletApiCashTradeResponseDTO();
        dto.setTxid(bill.getTxid());
        dto.setTimestamp(timeS);

        WalletApiCashTradeResponseDTO.FromWalletAccount from = new WalletApiCashTradeResponseDTO.FromWalletAccount();
        from.setBalance(po.getBalance());
        from.setFreezeBalance(po.getFreezBalance());
        from.setAccUserName(request.getAccUserName());
        from.setWalletAddress(request.getWalletAddress());
        from.setWalletType(request.getWalletType());
        dto.setFromWalletAccount(from);


        WalletApiCashTradeResponseDTO.FromWalletAccount to = new WalletApiCashTradeResponseDTO.FromWalletAccount();
        to.setBalance(toAccount.getBalance());
        to.setFreezeBalance(toAccount.getFreezBalance());
        to.setAccUserName(request.getToAccUserName());
        to.setWalletAddress(request.getToAccAddress());
        to.setWalletType(request.getWalletType());
        dto.setToWalletAccount(to);
        resp.setTimestamp(timeS);
        resp.setSignType(request.getSignType());
        String jsonString = JSON.toJsonString(dto);
        resp.setBizContent(jsonString);
        // 加密签名算法放在 api层面
        return resp;
    }

    @Override
    @Transactional(readOnly = false , rollbackFor =WalletApiException.class , propagation = Propagation.REQUIRED)
    public WalletRsaResponse withdrawBill(WalletApiWithdrawRequestDTO request) throws WalletApiException {
        // 校验地址合法性
        if(address.validateAddress(request.getToAccAddress() , request.getWalletType()) == false ){
            log.warn("invalid wallet address. address={} ." , request.getWalletAddress());
            throw new WalletApiException("invalid wallet address. address="+request.getWalletAddress()+".");
        }
        boolean flg = walletHotService.isExsitWalletHot(request.getAccUserName(),request.getWalletAddress(),request.getWalletType());
        if(flg == false){
            log.warn("invalid user message. username={} , walletAddress = {} , walletType={} " , request.getAccUserName(),request.getWalletAddress(),request.getWalletType());
            throw new WalletApiException("invalid user message.");
        }

        WalletAccountFreezePO fro =  freezeService.selectByBizNo(request.getOrderNo());
        if(fro == null){
            log.warn(" OrderNo={} , OrderNo is already exsits " , request.getOrderNo());
            throw new WalletApiException("OrderNo="+request.getOrderNo()+" , OrderNo is already exsits");
        }

        List<WalletAccountPO> res = walletAccountService.selectWalletAccount(request.getAccUserName() ,request.getWalletType() ,request.getWalletAddress());
        Long walletId = res.get(0).getWalletId();
        WalletAccountPO po = walletAccountService.selectByPrimaryKeyLockRow(walletId);
        if(po.getBalance() - po.getFreezBalance() - request.getValue() - request.getAbsentFee() <0){
            log.warn(" username={} , balance is not enough " , request.getAccUserName());
            throw new WalletApiException("username:"+request.getAccUserName()+" , balance is not enough.");
        }


        WalletAccountFreezePO record = new WalletAccountFreezePO();
        record.setBizNo(request.getOrderNo());
        record.setAccountType(request.getWalletType());
        Long timeS = System.currentTimeMillis();
        record.setCreateTime(timeS);
        record.setFreezBalance(request.getValue()+request.getAbsentFee());
        record.setAbsentFee(request.getAbsentFee());
        record.setFreezeNo(BizTypeConstants.BIZ_WITHDRAW.getMessage() + uuid.nextId());
        record.setToWalletAddress(request.getToAccAddress());
        record.setUserName(request.getAccUserName());
        record.setWalletId(po.getWalletId());
        record.setWalletAddressHash(request.getWalletAddress());
        record.setFreezState(BizTypeConstants.ACCOUNT_BAFREEZ_STATE_1.getCode());
        record.setFreezReason(BizTypeConstants.ACCOUNT_BAFREEZ_REASON_1.getMessage());
        record.setMsgState(BizTypeConstants.MSG_DELIVER_STATE_1.getCode());
        freezeService.insertSelective(record);

       po.setFreezBalance(po.getFreezBalance()+record.getFreezBalance());
       po.setLastModifyTime(timeS);
       walletAccountService.updateByPrimaryKeySelective(po);

        WalletRsaResponse resp = WalletRsaResponse.success();
        WalletApiRechargeResponseDTO dto = new WalletApiRechargeResponseDTO();
        dto.setTxid(record.getFreezeNo());
        dto.setTimestamp(timeS);
        dto.setFreezeBalance(po.getFreezBalance());
        dto.setBalance(po.getBalance());
        dto.setWalletType(request.getWalletType());
        dto.setWalletAddress(request.getWalletAddress());
        dto.setAccUserName(request.getAccUserName());
        resp.setMessage("提现申请提交成功，正在处理。");
        resp.setSignType(request.getSignType());
        String jsonString = JSON.toJsonString(dto);
        resp.setBizContent(jsonString);
        // 加密签名算法放在 api层面
        return resp;

    }

    @Override
    @Transactional(readOnly = false , rollbackFor =WalletApiException.class , propagation = Propagation.REQUIRED)
    public void withdrawNotifyBill(WalletWithdrawMsgDTO dto) {
        //校验消息是否已经处理过
        WalletAccountFreezePO po = freezeService.selectByFreezeNo(dto.getBizNo());
        if(po.getAccountType().equals(dto.getAccountType()) &&
                po.getWalletAddressHash().equals(dto.getWalletAddressHash()) &&
                po.getFreezState().equals(BizTypeConstants.ACCOUNT_BAFREEZ_STATE_1.getCode())){
            Long timeS = System.currentTimeMillis();
            WalletAccountFreezePO update = freezeService.selectByPrimaryKeyLock(po.getFreezeId());
            WalletAccountFreezePO c = new WalletAccountFreezePO();
            c.setFreezeId(update.getFreezeId());
            c.setMsgState(BizTypeConstants.MSG_DELIVER_STATE_2.getCode());
            c.setFreezState(BizTypeConstants.ACCOUNT_BAFREEZ_STATE_2.getCode());

            freezeService.updateByPrimaryKeySelective(c);

            WalletAccountPO account = walletAccountService.selectByPrimaryKeyLockRow(po.getWalletId());

            // 账户支出
            WalletAccountBillPO bill2 = new WalletAccountBillPO();
            bill2.setAccountType(account.getAccountType());
            bill2.setWalletId(account.getWalletId());
            // 交易
            bill2.setBizType(BizTypeConstants.BIZ_WITHDRAW.getCode());
            bill2.setCreateTime(timeS);
            bill2.setTxid(update.getFreezeNo());
            bill2.setOrderNo(update.getBizNo());
            bill2.setOrderAmount(update.getFreezBalance());
            bill2.setFlowType(BizBillFlowType.FLOWTYPE_OUTCOME);
            bill2.setSource(BizBillFlowType.FLOWTYPE_INCOME_SOURCE_TX);
            bill2.setBeforeBalance(account.getBalance());
            bill2.setBalance(account.getBalance()-update.getFreezBalance()+update.getAbsentFee());
            walletAccountBillService.insertChargeBill(bill2);

            // 账户支出
            WalletAccountBillPO bill = new WalletAccountBillPO();
            bill.setAccountType(account.getAccountType());
            bill.setWalletId(account.getWalletId());
            // 交易
            bill.setBizType(BizTypeConstants.BIZ_FEE_PLATE.getCode());
            bill.setCreateTime(timeS);
            bill.setTxid(update.getFreezeNo());
            bill.setOrderNo(update.getBizNo());
            bill.setOrderAmount(update.getAbsentFee());
            bill.setFlowType(BizBillFlowType.FLOWTYPE_OUTCOME);
            bill.setSource(BizBillFlowType.FLOWTYPE_INCOME_SOURCE_TX);
            bill.setBeforeBalance(bill2.getBalance());
            bill.setBalance(bill2.getBalance()-update.getAbsentFee());
            walletAccountBillService.insertChargeBill(bill);

            account.setBalance(account.getBalance()-update.getFreezBalance());
            account.setFreezBalance(account.getFreezBalance()-update.getFreezBalance());
            account.setLastModifyTime(timeS);
            walletAccountService.updateByPrimaryKeySelective(account);

        }else{
            log.warn("message:{} ,duplicate messages..." , dto.toString());
        }
    }

    /**
     *  扫块成功后回调
     * @param dto
     */
    @Override
    public void rechargeNotifyBill(WalletUsdtMsgDTO dto) {
        WalletAccountBillPO p =walletAccountBillService.selectByBlockId(dto.getTxid());
        if(p!=null){
            log.warn("message:{} ,duplicate messages..." , dto.toString());
            return;
        }
        List<WalletAccountPO> res = walletAccountService.selectWalletAccount(dto.getUserName() ,dto.getAccountType() ,dto.getWalletAddressHash());
        if(res.size()==1){
            Long walletId = res.get(0).getWalletId();
            WalletAccountPO account = walletAccountService.selectByPrimaryKeyLockRow(walletId);
            Long timeS = System.currentTimeMillis();
            // 账户支出
            WalletAccountBillPO bill = new WalletAccountBillPO();
            bill.setAccountType(account.getAccountType());
            bill.setWalletId(account.getWalletId());
            // 交易
            bill.setBizType(BizTypeConstants.BIZ_CHARGE.getCode());
            bill.setBlockTxid(dto.getTxid());
            bill.setCreateTime(timeS);
            bill.setTxid(BizTypeConstants.BIZ_CHARGE.getMessage() + uuid.nextId());
            bill.setOrderAmount(dto.getAmount());
            bill.setFlowType(BizBillFlowType.FLOWTYPE_INCOME);
            bill.setSource(BizBillFlowType.FLOWTYPE_INCOME_SOURCE_SK);
            bill.setBeforeBalance(account.getBalance());
            bill.setBalance(account.getBalance()+dto.getAmount());
            walletAccountBillService.insertChargeBill(bill);

            account.setBalance(account.getBalance()+dto.getAmount());
            account.setLastModifyTime(timeS);
            walletAccountService.updateByPrimaryKeySelective(account);

        }


    }


    @Autowired
    WalletWithdrawApply walletWithdrawApply;


    @Override
    public void withdrawBillRetry(Long delayTimes) {
        Long delay = System.currentTimeMillis() - delayTimes;
        List<WalletAccountFreezePO> pos = freezeService.selectWithdrawBillRetryPos(delay , BizTypeConstants.MSG_DELIVER_STATE_1.getCode());
        for(WalletAccountFreezePO po : pos){
            WalletApiWithdrawRequestDTO message = new WalletApiWithdrawRequestDTO();
            message.setBizNo(po.getFreezeNo());
            message.setOrderNo(po.getBizNo());
            message.setWalletAddress(po.getWalletAddressHash());
            message.setToAccAddress(po.getToWalletAddress());
            message.setWalletType(po.getAccountType());
            message.setValue(po.getFreezBalance()-po.getAbsentFee());
            message.setAbsentFee(po.getAbsentFee());
            walletWithdrawApply.sendMessage(message);
        }
    }
}
