package com.blockchain.wallet.api.dto;

import com.blockchain.wallet.api.internal.util.StringUtils;
import lombok.Data;

/**
 * 提现 需要传手续费
 *   一期只支持同种币种之间的交易 ，一期不计算手续费
 * @author shujun
 * @date 2018/6/28
 */
@Data
public class WalletApiWithdrawRequestDTO extends WalletApiRequestDTO {

    private static final long serialVersionUID = 8486036115946493414L;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public Long getAbsentFee() {
        return absentFee;
    }

    public void setAbsentFee(Long absentFee) {
        this.absentFee = absentFee;
    }

    public String getToAccAddress() {
        return toAccAddress;
    }

    public void setToAccAddress(String toAccAddress) {
        this.toAccAddress = toAccAddress;
    }

    public String getBizNo() {
        return bizNo;
    }

    public void setBizNo(String bizNo) {
        this.bizNo = bizNo;
    }

    private String orderNo;
    private Long value;
    // 手续费费用
    private Long absentFee;
    private String toAccAddress;
    //private String toAccUserName;

    private String bizNo;

    @Override
    public boolean check() {
        boolean res =  super.check();
        if(res==false){
            return false;
        }
        if(StringUtils.isEmpty(orderNo)){
            return false;
        }
        if(value==null || new Long(0).compareTo(value) >=0){
            return false;
        }
        if(StringUtils.isEmpty(toAccAddress)){
            return false;
        }

        return true;
    }
}
