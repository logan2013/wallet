package com.blockchain.wallet.api.internal.util;

import com.blockchain.wallet.api.WalletApiConstants;
import com.blockchain.wallet.api.WalletApiException;
import com.blockchain.wallet.api.internal.util.codec.Base64;
import com.blockchain.wallet.api.request.WalletCommonApiRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Cipher;
import java.io.*;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 *
 * @author shujun
 * @date 2018/6/25
 */
@Slf4j
public class WalletSignature {

    /** RSA最大加密明文大小  */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /** RSA最大解密密文大小   */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     *  rsa内容签名
     *
     * @param content
     * @param privateKey
     * @param charset
     * @return
     * @throws WalletApiException
     */
    public static String rsaSign(String content, String privateKey, String charset,
                                 String signType) throws WalletApiException {
        if (WalletApiConstants.SIGN_TYPE_RSA.equals(signType)) {
            return rsaSign(content, privateKey, charset);
        } else if (WalletApiConstants.SIGN_TYPE_RSA2.equals(signType)) {
            return rsa256Sign(content, privateKey, charset);
        } else {
            log.error("Sign Type is Not Support : signType = {} " , signType );
            throw new WalletApiException("Sign Type is Not Support : signType=" + signType);
        }
    }


    public static String rsaSignData(String content, String charset,
                                 String signType) throws WalletApiException {
        String privateKey =  RSAKey.getPrivateKeyString();

        if (WalletApiConstants.SIGN_TYPE_RSA.equals(signType)) {
            return rsaSign(content, privateKey, charset);
        } else if (WalletApiConstants.SIGN_TYPE_RSA2.equals(signType)) {
            return rsa256Sign(content, privateKey, charset);
        } else {
            log.error("Sign Type is Not Support : signType = {} " , signType );
            throw new WalletApiException("Sign Type is Not Support : signType=" + signType);
        }
    }

    /**
     * sha256WithRsa 加签
     *
     * @param content
     * @param privateKey
     * @param charset
     * @return
     * @throws WalletApiException
     */
    public static String rsa256Sign(String content, String privateKey,
                                    String charset) throws WalletApiException {
        try {
            PrivateKey priKey = getPrivateKeyFromPKCS8(WalletApiConstants.SIGN_TYPE_RSA,
                    new ByteArrayInputStream(privateKey.getBytes()));
            java.security.Signature signature = java.security.Signature
                    .getInstance(WalletApiConstants.SIGN_SHA256RSA_ALGORITHMS);
            signature.initSign(priKey);
            if (StringUtils.isEmpty(charset)) {
                signature.update(content.getBytes());
            } else {
                signature.update(content.getBytes(charset));
            }
            byte[] signed = signature.sign();
            return new String(Base64.encodeBase64(signed));
        } catch (Exception e) {
            log.error("RSAcontent = {}  , charset = {} , cause : {} " , content ,charset , e.getMessage());
            throw new WalletApiException("RSAcontent = " + content + "; charset = " + charset, e);
        }

    }

    /**
     * sha1WithRsa 加签
     *
     * @param content
     * @param privateKey
     * @param charset
     * @return
     * @throws WalletApiException
     */
    public static String rsaSign(String content, String privateKey,
                                 String charset) throws WalletApiException {
        try {
            PrivateKey priKey = getPrivateKeyFromPKCS8(WalletApiConstants.SIGN_TYPE_RSA,
                    new ByteArrayInputStream(privateKey.getBytes()));

            java.security.Signature signature = java.security.Signature
                    .getInstance(WalletApiConstants.SIGN_ALGORITHMS);

            signature.initSign(priKey);

            if (StringUtils.isEmpty(charset)) {
                signature.update(content.getBytes());
            } else {
                signature.update(content.getBytes(charset));
            }

            byte[] signed = signature.sign();

            return new String(Base64.encodeBase64(signed));
        } catch (InvalidKeySpecException ie) {
            throw new WalletApiException("RSA私钥格式不正确，请检查是否正确配置了PKCS8格式的私钥", ie);
        } catch (Exception e) {
            throw new WalletApiException("RSAcontent = " + content + "; charset = " + charset, e);
        }
    }

//    public static String rsaSign(Map<String, String> params, String privateKey,
//                                 String charset) throws WalletApiException {
//        String signContent = getSignContent(params);
//        return rsaSign(signContent, privateKey, charset);
//
//    }

    public static PrivateKey getPrivateKeyFromPKCS8(String algorithm,
                                                    InputStream ins) throws Exception {
        if (ins == null || StringUtils.isEmpty(algorithm)) {
            return null;
        }

        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);

        byte[] encodedKey = StreamUtil.readText(ins).getBytes();

        encodedKey = Base64.decodeBase64(encodedKey);

        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
    }

    public static String getSignCheckContentV1(Map<String, String> params) {
        if (params == null) {
            return null;
        }

        params.remove("sign");
        params.remove("sign_type");

        StringBuffer content = new StringBuffer();
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            content.append((i == 0 ? "" : "&") + key + "=" + value);
        }
        return content.toString();
    }

    public static String getSignCheckContentV2(Map<String, String> params) {
        if (params == null) {
            return null;
        }

        params.remove("sign");

        StringBuffer content = new StringBuffer();
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            content.append((i == 0 ? "" : "&") + key + "=" + value);
        }

        return content.toString();
    }

    public static boolean rsaCheckV1(Map<String, String> params, String publicKey,
                                     String charset) throws WalletApiException {
        String sign = params.get("sign");
        String content = getSignCheckContentV1(params);

        return rsaCheckContent(content, sign, publicKey, charset);
    }

    public static boolean rsaCheckV1(Map<String, String> params, String publicKey,
                                     String charset,String signType) throws WalletApiException {
        String sign = params.get("sign");
        String content = getSignCheckContentV1(params);

        return rsaCheck(content, sign, publicKey, charset,signType);
    }

    public static boolean rsaCheckV2(Map<String, String> params, String publicKey,
                                     String charset) throws WalletApiException {
        String sign = params.get("sign");
        String content = getSignCheckContentV2(params);

        return rsaCheckContent(content, sign, publicKey, charset);
    }

    public static boolean rsaCheckV2(Map<String, String> params, String publicKey,
                                     String charset,String signType) throws WalletApiException {
        String sign = params.get("sign");
        String content = getSignCheckContentV2(params);

        return rsaCheck(content, sign, publicKey, charset,signType);
    }

    public static boolean rsaCheck(String content, String sign, String publicKey, String charset,
                                   String signType) throws WalletApiException {

        if (WalletApiConstants.SIGN_TYPE_RSA.equals(signType)) {

            return rsaCheckContent(content, sign, publicKey, charset);

        } else if (WalletApiConstants.SIGN_TYPE_RSA2.equals(signType)) {

            return rsa256CheckContent(content, sign, publicKey, charset);

        } else {

            throw new WalletApiException("Sign Type is Not Support : signType=" + signType);
        }

    }

    public static boolean rsa256CheckContent(String content, String sign, String publicKey,
                                             String charset) throws WalletApiException {
        try {
            PublicKey pubKey = getPublicKeyFromX509("RSA",
                    new ByteArrayInputStream(publicKey.getBytes()));

            java.security.Signature signature = java.security.Signature
                    .getInstance(WalletApiConstants.SIGN_SHA256RSA_ALGORITHMS);

            signature.initVerify(pubKey);

            if (StringUtils.isEmpty(charset)) {
                signature.update(content.getBytes());
            } else {
                signature.update(content.getBytes(charset));
            }

            return signature.verify(Base64.decodeBase64(sign.getBytes()));
        } catch (Exception e) {
            throw new WalletApiException(
                    "RSAcontent = " + content + ",sign=" + sign + ",charset = " + charset, e);
        }
    }

    /**
     *
     * @param content
     * @param sign
     * @param publicKey
     * @param charset
     * @return
     * @throws WalletApiException
     */
    public static boolean rsaCheckContent(String content, String sign, String publicKey,
                                          String charset) throws WalletApiException {
        try {
            PublicKey pubKey = getPublicKeyFromX509("RSA",
                    new ByteArrayInputStream(publicKey.getBytes()));

            java.security.Signature signature = java.security.Signature
                    .getInstance(WalletApiConstants.SIGN_ALGORITHMS);

            signature.initVerify(pubKey);

            if (StringUtils.isEmpty(charset)) {
                signature.update(content.getBytes());
            } else {
                signature.update(content.getBytes(charset));
            }
            return signature.verify(Base64.decodeBase64(sign.getBytes()));
        } catch (Exception e) {
            throw new WalletApiException(
                    "RSAcontent = " + content + ",sign=" + sign + ",charset = " + charset, e);
        }
    }

    public static PublicKey getPublicKeyFromX509(String algorithm,
                                                 InputStream ins) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);

        StringWriter writer = new StringWriter();
        StreamUtil.io(new InputStreamReader(ins), writer);

        byte[] encodedKey = writer.toString().getBytes();

        encodedKey = Base64.decodeBase64(encodedKey);

        return keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
    }

    /**
     * 验签并解密
     *
     * </p>
     * @param params
     * @param walletPublicKey 平台公钥
     * @param cusPrivateKey   商户私钥
     * @param isCheckSign     是否验签
     * @param isDecrypt       是否解密
     * @return 解密后明文，验签失败则异常抛出
     * @throws WalletApiException
     */
    public static String checkSignAndDecrypt(Map<String, String> params, String walletPublicKey,
                                             String cusPrivateKey, boolean isCheckSign,
                                             boolean isDecrypt) throws WalletApiException {
        String charset = params.get("charset");
        String bizContent = params.get("biz_content");
        if (isCheckSign) {
            if (!rsaCheckV2(params, walletPublicKey, charset)) {
                throw new WalletApiException("rsaCheck failure:rsaParams=" + params);
            }
        }
        if (isDecrypt) {
            return rsaDecrypt(bizContent, cusPrivateKey, charset);
        }
        return bizContent;
    }

    /**
     * 验签并解密
     * @param params
     * @param walletPublicKey
     * @param cusPrivateKey   商户私钥
     * @param isCheckSign     是否验签
     * @param isDecrypt       是否解密
     * @return 解密后明文，验签失败则异常抛出
     * @throws WalletApiException
     */
    public static String checkSignAndDecrypt(Map<String, String> params, String walletPublicKey,
                                             String cusPrivateKey, boolean isCheckSign,
                                             boolean isDecrypt, String signType) throws WalletApiException {
        String charset = params.get("charset");
        String bizContent = params.get("biz_content");
        if (isCheckSign) {
            if (!rsaCheckV2(params, walletPublicKey, charset,signType)) {
                throw new WalletApiException("rsaCheck failure:rsaParams=" + params);
            }
        }

        if (isDecrypt) {
            return rsaDecrypt(bizContent, cusPrivateKey, charset);
        }

        return bizContent;
    }

    /**
     * 公钥加密
     *
     * @param content   待加密内容
     * @param publicKey 公钥
     * @param charset   字符集，如UTF-8, GBK, GB2312
     * @return 密文内容
     * @throws WalletApiException
     */
    public static String rsaEncrypt(String content, String publicKey,
                                    String charset) throws WalletApiException {
        try {
            PublicKey pubKey = getPublicKeyFromX509(WalletApiConstants.SIGN_TYPE_RSA,
                    new ByteArrayInputStream(publicKey.getBytes()));
            Cipher cipher = Cipher.getInstance(WalletApiConstants.SIGN_TYPE_RSA);
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            byte[] data = StringUtils.isEmpty(charset) ? content.getBytes()
                    : content.getBytes(charset);
            int inputLen = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = Base64.encodeBase64(out.toByteArray());
            out.close();
            return StringUtils.isEmpty(charset) ? new String(encryptedData)
                    : new String(encryptedData, charset);
        } catch (Exception e) {
            e.printStackTrace();
            throw new WalletApiException("EncryptContent = " + content + ",charset = " + charset,
                    e);
        }
    }

    /**
     * 私钥解密
     *
     * @param content    待解密内容
     * @param privateKey 私钥
     * @param charset    字符集，如UTF-8, GBK, GB2312
     * @return 明文内容
     * @throws WalletApiException
     */
    public static String rsaDecrypt(String content, String privateKey,
                                    String charset) throws WalletApiException {
        try {
            PrivateKey priKey = getPrivateKeyFromPKCS8(WalletApiConstants.SIGN_TYPE_RSA,
                    new ByteArrayInputStream(privateKey.getBytes()));
            Cipher cipher = Cipher.getInstance(WalletApiConstants.SIGN_TYPE_RSA);
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            byte[] encryptedData = StringUtils.isEmpty(charset)
                    ? Base64.decodeBase64(content.getBytes())
                    : Base64.decodeBase64(content.getBytes(charset));
            int inputLen = encryptedData.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return StringUtils.isEmpty(charset) ? new String(decryptedData)
                    : new String(decryptedData, charset);
        } catch (Exception e) {
            e.printStackTrace();
            WalletApiException ex = new WalletApiException(" rsaDecrypt fail , EncodeContent = " + content + ",charset = " + charset, e);
            throw ex;
        }
    }


    public static String rsaDecrypt(String content, String charset) throws WalletApiException {
        String privateKry =  RSAKey.getPrivateKeyString();
        return rsaDecrypt(content , privateKry , charset);
    }

    public static String rsaEncrypt(String content, String charset) throws WalletApiException {
        String publicKey =  RSAKey.getPublicKeyString();
        return rsaEncrypt(content , publicKey , charset);
    }



    public static void main(String[] args) throws WalletApiException {
       String pk =  RSAKey.getPrivateKeyString();
       String pu = RSAKey.getPublicKeyString();
       test(pk , pu);
       String[] keys = KeyTool.generateKey(KeyType.RSA1024);
       test(keys[0] , keys[1]);
    }

    static void test(String pk , String pu ) throws WalletApiException {
        WalletCommonApiRequest czr = new WalletCommonApiRequest();
        czr.setTimestamp(System.currentTimeMillis());
        czr.setSignType("RSA");
        czr.setUserName("zhangsan");
        
        // 签名验签
       // String content = "{ \"name\" : \"shujun \"}" ;
        String content = JSON.toJsonString(czr);
        //  String sign = WalletSignature.rsaSign(content,pk, WalletApiConstants.CHARSET_UTF8);
        String sign = WalletSignature.rsaSign(content,pk, WalletApiConstants.CHARSET_UTF8,WalletApiConstants.SIGN_TYPE_RSA);
        log.info(sign);
        log.info("{} ",sign.length());
        String ccs = new String( Base64.encodeBase64(DigestUtils.sha256(sign)));
        log.info( ccs);
        log.info("{} ",ccs.length());
        //boolean a = WalletSignature.rsaCheckContent(content , sign,pu ,WalletApiConstants.CHARSET_UTF8);
        boolean a = WalletSignature.rsaCheck(content , sign,pu ,WalletApiConstants.CHARSET_UTF8 , WalletApiConstants.SIGN_TYPE_RSA);
        log.info(" a: {} " , a);
        // 加密解密
        String tet =  WalletSignature.rsaEncrypt(content , pu  ,WalletApiConstants.CHARSET_UTF8 );
        log.info(tet);
        String l =  WalletSignature.rsaDecrypt(tet , pk  ,WalletApiConstants.CHARSET_UTF8 );
        log.info(l);
    }

}
