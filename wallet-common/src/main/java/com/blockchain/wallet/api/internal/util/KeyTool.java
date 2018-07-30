package com.blockchain.wallet.api.internal.util;

import com.blockchain.wallet.api.WalletApiConstants;
import com.blockchain.wallet.api.WalletApiException;
import com.blockchain.wallet.api.internal.util.codec.Base64;
import lombok.extern.slf4j.Slf4j;
import java.security.*;

/**
 * Created by shujun on 2018/6/26.
 */
@Slf4j
public class KeyTool {
    /**
     *  返回数组
     * @param keytype
     * @return
     * @throws WalletApiException
     */
    public static String[] generateKey(KeyType keytype) throws WalletApiException {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(keytype.getName());
            SecureRandom secureRandom = new SecureRandom();
            keyPairGenerator.initialize(keytype.getLength(), secureRandom);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();
            String[] result = new String[2];
            result[0] = new String(Base64.encodeBase64(privateKey.getEncoded()));
            result[1] =  new String(Base64.encodeBase64(publicKey.getEncoded()));
            return result;
        } catch (NoSuchAlgorithmException e) {
            log.error("不支持的算法 : {} " , keytype.getName() );
            throw new WalletApiException("不支持的算法" + keytype.getName());
        }
    }


    public static boolean checkKey(KeyType keytype, String privateKeyData, String publicKeyData) throws WalletApiException {
        String text = "abcdefghijklmknopq";
        String sign = null;
        SignatureUtil sig = new SignatureUtil(keytype.getName());
        try {
            sign = sig.sign(text, privateKeyData, WalletApiConstants.CHARSET_UTF8);
        } catch (WalletApiException e) {
            log.error("私钥内容不正确 : {} " , e.getMessage());
            throw new WalletApiException("私钥内容不正确" + e.getMessage());
        }
        try {
            return sig.verify(text, publicKeyData, sign, WalletApiConstants.CHARSET_UTF8);
        } catch (WalletApiException e) {
            log.error("公钥内容不正确 : {} " , e.getMessage());
            throw new WalletApiException("公钥内容不正确: " + e.getMessage());
        }
    }

}
