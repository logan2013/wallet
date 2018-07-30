package com.blockchain.wallet.api.dto;

import com.blockchain.wallet.api.internal.util.StringUtils;
import lombok.Data;

/**
 * 交易：
 *   一期只支持同种币种之间的交易 ，一期不计算手续费
 * @author shujun
 * @date 2018/6/28
 */
@Data
public class WalletApiCashTradeRequestDTO extends WalletApiRequestDTO {

    private static final long serialVersionUID = 3427378578495637070L;
    private String orderNo;
    private Long value;
    // 手续费费用
    private Long absentFee;
    private String toAccAddress;
    private String toAccUserName;

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
        if(StringUtils.isEmpty(toAccUserName)){
            return false;
        }
        return true;
    }
}
