package com.blockchain.wallet.api.dto;

import com.blockchain.wallet.api.internal.util.StringUtils;
import lombok.Data;
/**
 * 充值接口请求参数
 * @author shujun
 * @date 2018/6/28
 */
@Data
public class WalletApiRechargeRequestDTO extends WalletApiRequestDTO {

    private static final long serialVersionUID = 3427378578495637070L;
    private String orderNo;
    private Long value;
    // 手续费费用
    private Long absentFee;

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
        return true;
    }
}
