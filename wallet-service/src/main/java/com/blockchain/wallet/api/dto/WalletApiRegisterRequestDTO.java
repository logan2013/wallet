package com.blockchain.wallet.api.dto;

import com.blockchain.wallet.api.internal.util.StringUtils;
import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author shujun
 * @date 2018/6/28
 */
@Data
public class WalletApiRegisterRequestDTO extends WalletApiRequestDTO {

    private static final long serialVersionUID = 3427378578495637070L;
    private String userId;
    private String appPublicKey;
    private String exchageUserPublicKey;
    private String signType;

    @Override
    public boolean check() {
        if(super.check() == false){
            return false;
        }

        if(StringUtils.isEmpty(appPublicKey)){
            return false;
        }

        if(StringUtils.isEmpty(exchageUserPublicKey)){
            return false;
        }

        return true;
    }
}
