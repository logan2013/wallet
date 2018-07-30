package com.blockchain.wallet.api.internal.util;

import com.blockchain.wallet.api.WalletApiConstants;
import com.blockchain.wallet.api.WalletApiException;
import com.blockchain.wallet.api.internal.util.codec.Base64;
import lombok.extern.slf4j.Slf4j;
import javax.crypto.*;
import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
/**
 * Created by shujun on 2018/6/25.
 */
@Slf4j
public final class KeyExchangeAlgorithmEncrypt {
    /**
     * 非对称加密密钥算法
     */
    private static final String KEY_EXCHANGE_ALGORITHM = "DH";
    /**
     * 本地密钥算法，即对称加密密钥算法 可选DES、DESede或者AES
     */
    private static final String SELECT_SYMMETRIC_ALGORITHM = "AES";
    /**
     * 密钥长度 512 - 2048 之间
     */
    private static final int KEY_SIZE = 2048;

    // 密钥对
    private KeyPair localKeyPair;

    public KeyExchangeAlgorithmEncrypt() {
        try {
            this.localKeyPair = initKey();
        } catch (WalletApiException e) { log.error(e.getMessage());}
    }

    public KeyExchangeAlgorithmEncrypt(String otherPublicKey) {
        try {
            this.localKeyPair = initKey(otherPublicKey , WalletApiConstants.CHARSET_UTF8);
        } catch (WalletApiException e) {
            log.error(e.getMessage());
        }
    }

    /**
     *  初始化本地密钥对
     * @return
     * @throws Exception
     */
    private  KeyPair initKey() throws WalletApiException {
        KeyPairGenerator keyPairGenerator = null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance(KEY_EXCHANGE_ALGORITHM);
            keyPairGenerator.initialize(KEY_SIZE);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            log.error("not support algorithm , algorithm:{}" ,KEY_EXCHANGE_ALGORITHM);
            throw new WalletApiException("not support algorithm , algorithm: = " + KEY_EXCHANGE_ALGORITHM , e);
        }
    }

    /**
     *  根据对方的公钥构建本地的秘钥对
     * @param otherPublicKey
     * @return
     * @throws WalletApiException
     */
    private KeyPair initKey(String otherPublicKey , String charset) throws WalletApiException {
        try {
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(otherPublicKey.getBytes(charset)));
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_EXCHANGE_ALGORITHM);
            // 产生公钥
            PublicKey pubKey  = keyFactory.generatePublic(x509KeySpec);
            // 由甲方公钥构建乙方密钥
            DHParameterSpec dhParameterSpec = ((DHPublicKey) pubKey).getParams();
            // 实例化密钥对生成器
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_EXCHANGE_ALGORITHM);
            // 初始化密钥对生成器
            keyPairGenerator.initialize(dhParameterSpec);
            // 产生密钥对
            return keyPairGenerator.generateKeyPair();
        }catch (Exception e) {
            log.error("AES encrypt failure otherPublicKey : {} ,charset : {} , cause : {} " , otherPublicKey , charset , e.getMessage());
            throw new WalletApiException("DH init failure otherPublicKey = " +otherPublicKey+ ",charset ="+charset+" , cause :  " + e.getMessage() , e);
        }

    }

    /**
     *  获取公钥，并通过base64编码
     * @return
     */
    public String getPublicKey(){
        if(this.localKeyPair == null){
            log.error("keyPair is null");
            return null;
        }
        DHPublicKey publicKey = (DHPublicKey)localKeyPair.getPublic();
        return new String(Base64.encodeBase64(publicKey.getEncoded())) ;
    }

    /**
     *  获取私钥，并通过base64编码
     * @return
     */
    public String getPrivateKey(){
        if(this.localKeyPair == null){
            log.error("keyPair is null");
            return null;
        }
        DHPrivateKey privateKey = (DHPrivateKey)localKeyPair.getPrivate();
        return new String(Base64.encodeBase64(privateKey.getEncoded())) ;
    }

    /**
     * 构建密钥, 最终base64编码
     *
     * @param publicKey 对方公钥
     * @return byte[] 本地密钥
     * @throws Exception
     */
    public String getSecretKey(String publicKey , String charset) throws WalletApiException {
        try {
            KeyFactory  keyFactory = KeyFactory.getInstance(KEY_EXCHANGE_ALGORITHM);
            // 密钥材料转换
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey.getBytes(charset)));
            // 产生公钥
            PublicKey pubKey  = keyFactory.generatePublic(x509KeySpec);
            // 初始化私钥
            // 密钥材料转换
            DHPrivateKey privateKey = (DHPrivateKey)localKeyPair.getPrivate();
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
            // 产生私钥
            PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
            // 实例化
            KeyAgreement keyAgree = KeyAgreement.getInstance(keyFactory.getAlgorithm());
            // 初始化
            keyAgree.init(priKey);
            keyAgree.doPhase(pubKey, true);
            // 生成本地密钥
            SecretKey secretKey = keyAgree.generateSecret(SELECT_SYMMETRIC_ALGORITHM);
            return new String(Base64.encodeBase64(secretKey.getEncoded()));
        } catch (Exception e) {
            log.error("init ase key failure publicKey : {} ,charset : {} , cause : {} " , publicKey , charset , e.getMessage());
            throw new WalletApiException("init ase key failure publicKey = " +publicKey+ ",charset ="+charset+" , cause :  " + e.getMessage() , e);
        }
    }


    /**
     * 加密
     *
     * @param content
     *            待加密数据
     * @param secretKeyStr  对称秘钥 base64编码
     * @return String 加密数据 先通过 对称加密，在base64编码
     * @throws Exception
     */
    public static String encryptContent(String content, String secretKeyStr ,String charset) throws WalletApiException {
        try {
            SecretKey secretKey = new SecretKeySpec(Base64.decodeBase64(secretKeyStr.getBytes()), SELECT_SYMMETRIC_ALGORITHM);
            Cipher cipher = Cipher.getInstance(secretKey.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptBytes = cipher.doFinal(content.getBytes(charset));
            return new String(Base64.encodeBase64(encryptBytes));
        } catch (Exception e) {
            log.error("AES encrypt failure Aescontent : {} ,charset : {} , cause : {} " , content , charset , e.getMessage());
            throw new WalletApiException("AES 加密失败：Aescontent = " + content + "; charset = "
                    + charset, e);
        }
    }



    /**
     * 解密获取明文
     *
     * @param ciphertext
     *            密文， base64编码后的密文
     * @param secretKeyStr
     *            密钥
     * @return byte[] 解密数据
     * @throws Exception
     */
    public static String decryptContent(String ciphertext, String secretKeyStr,String charset) throws WalletApiException {
        try {
            // 数据揭秘
            SecretKey secretKey = new SecretKeySpec(Base64.decodeBase64(secretKeyStr.getBytes()), SELECT_SYMMETRIC_ALGORITHM);
            Cipher cipher = Cipher.getInstance(secretKey.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.decodeBase64(ciphertext.getBytes())), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("AES decrypt failure Aescontent : {} ,charset : {} , cause : {} " , ciphertext , charset, e.getMessage());
            throw new WalletApiException("AES解密失败：Aescontent = " + ciphertext + "; charset = "
                    + charset, e);
        }
    }


    public static void main(String[] args) throws Exception {
        KeyExchangeAlgorithmEncrypt one = new KeyExchangeAlgorithmEncrypt();
        log.info("PublicKey : {} " , one.getPublicKey());
        log.info("PublicKey length : {} " , one.getPublicKey().length());
        log.info("PrivateKey : {} "  , one.getPrivateKey());
        log.info("PrivateKey length : {} "  , one.getPrivateKey().length());
        KeyExchangeAlgorithmEncrypt two = new KeyExchangeAlgorithmEncrypt(one.getPublicKey());
        log.info("PublicKey : {} " , two.getPublicKey());
        log.info("PublicKey length : {} " , two.getPublicKey().length());
        log.info("PrivateKey : {} "  , two.getPrivateKey());
        log.info("PrivateKey length : {} "  , two.getPrivateKey().length());

        String a1m = one.getSecretKey(two.getPublicKey(),WalletApiConstants.CHARSET_UTF8);
        log.info("a1m : {} " ,a1m );
        String a2m = two.getSecretKey(one.getPublicKey(),WalletApiConstants.CHARSET_UTF8);
        log.info("a2m : {} " ,a2m );
        log.info("a2m : {} " ,a2m.length() );
        System.out.println(a1m.equals(a2m));
        String abc = "china , 现在很牛逼!";
        String tmp =  KeyExchangeAlgorithmEncrypt.encryptContent(abc,a1m,WalletApiConstants.CHARSET_UTF8);
        System.out.println(tmp);
        System.out.println(KeyExchangeAlgorithmEncrypt.decryptContent(tmp,a1m,WalletApiConstants.CHARSET_UTF8));

    }


}
