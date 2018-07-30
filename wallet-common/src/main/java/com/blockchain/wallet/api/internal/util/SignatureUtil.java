package com.blockchain.wallet.api.internal.util;

import com.blockchain.wallet.api.WalletApiException;
import com.blockchain.wallet.api.internal.util.codec.Base64;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by Administrator on 2018/6/26.
 */
public class SignatureUtil {

    private static final String RSAAlgName = "RSA";
    private static final String DSAAlgName = "DSA";
    private SignType signType;

    public SignatureUtil(String algName) {
        assert ((algName.equalsIgnoreCase("RSA")) || (algName.equalsIgnoreCase("DSA")));
        if (algName.equalsIgnoreCase("RSA")) {
            this.signType = SignType.RSA;
        } else if (algName.equalsIgnoreCase("DSA")) {
            this.signType = SignType.DSA;
        }
    }

    public SignatureUtil(SignType signType) {
        this.signType = signType;
    }

    public String sign(String text, String privateKeyData, String charset) throws WalletApiException {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyData.getBytes()));
            PrivateKey privateKey = KeyFactory.getInstance(this.signType.getName()).generatePrivate(keySpec);
            return sign(text, privateKey, charset);
        } catch (Exception e) {
            throw new WalletApiException(e);
        }
    }

    public String sign(String text, PrivateKey privateKey, String charset) throws WalletApiException {
        try {
            Signature signature  = Signature.getInstance(this.signType.getSigAlg());
            signature.initSign(privateKey);
            signature.update(text.getBytes(charset));
            return new String(Base64.encodeBase64(signature.sign()));
        } catch (Exception e) {
            throw new WalletApiException(e);
        }
    }


    public boolean verify(String text, String publicKeyData, String sign, String charset) throws WalletApiException {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyData.getBytes()));
            PublicKey publicKey = KeyFactory.getInstance(this.signType.getName()).generatePublic(keySpec);
            return verify(text, publicKey, sign, charset);
        } catch (Exception e) {
            throw new WalletApiException(e);
        }

    }

    public boolean verify(String text, PublicKey publicKey, String sign, String charset) throws WalletApiException {
        try {
            Signature signatureChecker = Signature.getInstance(this.signType.getSigAlg());
            signatureChecker.initVerify(publicKey);
            signatureChecker.update(text.getBytes(charset));
            return signatureChecker.verify(Base64.decodeBase64(sign.getBytes()));
        } catch (Exception e) {
            throw new WalletApiException(e);
        }
    }

    public boolean verify(String text, Certificate cert, String sign) throws WalletApiException {
        try {
            Signature sig = Signature.getInstance(this.signType.getSigAlg());
            sig.initVerify(cert);
            sig.update(text.getBytes());
            return sig.verify(Base64.decodeBase64(sign.getBytes()));
        } catch (Exception e) {
            throw new WalletApiException(e);
        }

    }

}
