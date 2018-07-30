package com.blockchain.wallet.api.dto;

import com.blockchain.wallet.api.internal.util.JSON;
import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author Administrator
 * @date 2018/6/28
 */
@Data
public class WalletApiCashTradeResponseDTO implements Serializable {

    private static final long serialVersionUID = 7857290446898710253L;
    private String txid;
    private Long timestamp;
    // 返回汇款人账户余额信息
    private FromWalletAccount fromWalletAccount;
    //返回被交易方账户余额信息
    private FromWalletAccount toWalletAccount;

    @Data
    public static class FromWalletAccount implements Serializable{

        private static final long serialVersionUID = 4878902703108411765L;
        // 钱包 hash地址
        private String walletAddress;
        private String accUserName;
        private String walletType;
        private Long balance;
        private Long freezeBalance;
    }

    public static void main(String args[]){
        WalletApiCashTradeResponseDTO dto = new WalletApiCashTradeResponseDTO();
        dto.setTimestamp(12312312L);
        dto.setTxid("HJNMIJH");
        FromWalletAccount a = new FromWalletAccount();
        a.setAccUserName("zga");
        a.setBalance(345L);
        dto.setFromWalletAccount(a);
        FromWalletAccount b = new FromWalletAccount();
        b.setAccUserName("lisi");
        b.setBalance(789L);
        dto.setToWalletAccount(b);
        String c = JSON.toJSONString(dto);
        System.out.println(c);
        WalletApiCashTradeResponseDTO d = JSON.parseObject(c , WalletApiCashTradeResponseDTO.class);
        System.out.println(d.toString());
        System.out.println(JSON.toJSONString(d));

        Long t1 = 250L;
        Long t2 = 100L;
        Long t3=200L;

        System.out.println(t1 - t2- t3);
        System.out.println((t1 - t2- t3) < 0);
    }

}
