package com.blockchain.wallet.api.dto;

import com.blockchain.wallet.api.internal.util.StringUtils;
import lombok.Data;
import java.io.Serializable;

/**
 *
 * @author shujun
 * @date 2018/7/12
 */
@Data
public class WalletApiRequestDTO implements Serializable, RequestParamterValidator {

    private static final long serialVersionUID = 5924055361543516180L;

    protected String accUserName;

    protected String walletAddress;

    protected String walletType;

    protected String signType;

    protected Long timestamp;

    @Override
    public boolean check() {
        if(StringUtils.isEmpty(accUserName)){
            return false;
        }
//        if(StringUtils.isEmpty(walletAddress)){
//            return false;
//        }
        if(StringUtils.isEmpty(walletType)){
            return false;
        }
        return true;
    }
}
