package com.blockchain.wallet.api.utils;

import com.blockchain.wallet.api.WalletApiException;
import com.blockchain.wallet.api.WalletTypeConstants;
import com.blockchain.wallet.api.internal.util.StringUtils;
import com.blockchain.wallet.api.service.impl.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet3Params;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author Administrator
 * @date 2018/7/9
 */
@Slf4j
@Component
public class CreateAddress {

    @Autowired
    private WalletConfig config;

    /**
     *  默认获取比特币地址
     * @return
     */
    public WalletContainer getWalletAddress() throws WalletApiException {
        return getWalletAddress(WalletTypeConstants.WALLET_TYPE_BTC);
    }

    /**
     * @param walletType
     * @return
     */
    public WalletContainer getWalletAddress(String walletType) throws WalletApiException {
        if(WalletTypeConstants.WALLET_TYPE_BTC.equals(walletType) || WalletTypeConstants.WALLET_TYPE_USDT.equals(walletType)){
            ECKey key = new ECKey();
            NetworkParameters p = NetParams.getInstance(config.getBtcoin().getEnv());
            WalletContainer con = new WalletContainer();
            con.setWalletAddress(key.toAddress(p).toBase58());
            con.setWalletPubKey(key.getPrivateKeyAsHex());
            con.setWalletPrvKey(key.getPrivateKeyAsHex());
            con.setWalletPrivateKeyAsWiF(key.getPrivateKeyAsWiF(p));
            return con;
        }
        log.error("system not support generate {} address ", walletType);
        throw new WalletApiException("system not support generate "+walletType+" address");
    }

    public boolean validateAddress(String address,String walletType) throws WalletApiException {
        if(WalletTypeConstants.WALLET_TYPE_BTC.equals(walletType) || WalletTypeConstants.WALLET_TYPE_USDT.equals(walletType)){
            NetworkParameters p = NetParams.getInstance(config.getBtcoin().getEnv());
            try{
                Address.fromBase58(p ,address );
                return true;
            }catch(AddressFormatException e){
                return false;
            }
        }
        log.error("system not support generate {} address ", walletType);
        throw new WalletApiException("system not support generate "+walletType+" address");
    }


    private static class NetParams {
        private final static NetworkParameters INSTANCE_PROD = MainNetParams.get();
        private final static NetworkParameters INSTANCE_TEST = TestNet3Params.get();
        private final static String ENV_PROD = "prod";
        private final static String ENV_TEST = "test";

        public static NetworkParameters getProdInstance(){
            return INSTANCE_PROD;
        }

        public static NetworkParameters getTestInstance(){
            return INSTANCE_TEST;
        }

        public static NetworkParameters getInstance(String env) throws WalletApiException {
            if(StringUtils.isEmpty(env)){
                return INSTANCE_PROD;
            }else if(ENV_PROD.equals(env)){
                return INSTANCE_PROD;
            }else if(ENV_TEST.equals(env)){
                return INSTANCE_TEST;
            }
            log.error("not support evn: {} for blockchain.", env);
            throw new WalletApiException("not support "+env+" for blockchain .");
        }
    }

    @Value("${spring.redis.usdtSet}")
    private  String usdtSet;

    @Autowired
    private RedisService redisService;


    public String fetchAddressFromPool(){
        String add = (String)redisService.spop(usdtSet);
        return add;
    }





   public static void main(String[] args) throws WalletApiException {
       NetworkParameters p = NetParams.getInstance("test");
      // Address addr =  Address.fromBase58(p ,"14qyp7htJrQmxQiWfKJqpstryuMobKPwVY" );
       Address addr =  Address.fromBase58(p ,"mk6KT4UbnxpmCQ8tDr1WA5cGAULcJGRbwB" );
       System.out.println(addr.toBase58());
   }

}
