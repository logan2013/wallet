package com.wallet.base.test.runner;

import com.blockchain.wallet.api.WalletApiConstants;
import com.blockchain.wallet.api.WalletTypeConstants;
import com.blockchain.wallet.api.dto.WalletApiRegisterRequestDTO;
import com.blockchain.wallet.api.internal.util.*;
import com.blockchain.wallet.api.request.WalletApiResponse;
import com.blockchain.wallet.api.request.WalletCommonApiRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.json.JSONObject;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.*;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Created by Administrator on 2018/7/26.
 */
@Slf4j
public final class HttpsUtil {





    public static CloseableHttpClient acceptsUntrustedCertsHttpClient() throws NoSuchAlgorithmException,
            KeyManagementException, KeyStoreException {
        SSLContext sslcontext = SSLContexts.custom().setSecureRandom(new SecureRandom())
                .loadTrustMaterial(null, new TrustStrategy() {

                    public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        return true;
                    }
                }).build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,
                SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(5000)
                .setConnectTimeout(5000).setStaleConnectionCheckEnabled(true).build();

        return HttpClients.custom().setSSLSocketFactory(sslsf).setDefaultRequestConfig(defaultRequestConfig).build();
    }


    public static RestTemplate restTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        RestTemplate restTemplate = new RestTemplate();

        CloseableHttpClient httpClient = acceptsUntrustedCertsHttpClient();
        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        restTemplate.setRequestFactory(requestFactory);
        return restTemplate;
    }


    static final String url ="https://47.105.119.229/wallet-api/api/register";
    static final String url_local ="http://192.168.2.82:7070/wallet-api/api/test";





    public static void main(String[] args) throws Exception {


        KeyExchangeAlgorithmEncrypt local = new KeyExchangeAlgorithmEncrypt();
        log.info("KeyExchangeAlgorithmEncrypt.privateKey : {} " , local.getPrivateKey());
        log.info("KeyExchangeAlgorithmEncrypt.publicKey : {} " , local.getPublicKey());
        WalletCommonApiRequest request = new WalletCommonApiRequest();
        WalletApiRegisterRequestDTO dto = new WalletApiRegisterRequestDTO();
        dto.setUserId("usdt");
        dto.setAccUserName("jacky_ttt1111");
        dto.setSignType("RSA");
        dto.setWalletType(WalletTypeConstants.WALLET_TYPE_USDT); //BTC
        String[] keys = KeyTool.generateKey(KeyType.RSA1024);
        dto.setWalletType(WalletTypeConstants.WALLET_TYPE_USDT);
        log.info("KeyTool.privateKey : {} " , keys[0]);
        log.info("KeyTool.publicKey : {} " , keys[1]);
        dto.setAppPublicKey(keys[1]);
        dto.setExchageUserPublicKey(local.getPublicKey());
        String secrtKey = JSON.toJSONString(dto);
        log.info(secrtKey);
        secrtKey = WalletSignature.rsaEncrypt(secrtKey , WalletApiConstants.CHARSET_UTF8 );
        log.error(secrtKey);
        request.setRequestContent(secrtKey);
        request.setTimestamp(System.currentTimeMillis());
        System.out.println(JSON.toJSONString(request));


        RestTemplate t = restTemplate();

        // /home/hadoop/jdk1.8.0_181/jre/lib/security
     //   WalletApiResponse obj = t.postForObject(url_local , request ,  WalletApiResponse.class);
        String obj = t.postForObject(url_local , "UUAGMOA8krf16esFvdf1AoavdocmZpP7Gaoxtklmj8B3xnluZOehwCdD0QxfBh6lIOLq7TOUeb3Z00nm87y4IptKlIn2frYmojAqiXGg5BOIxo1rZOsKY1saRWCo8nZW1ksTR7uxpzM8N0dCBJqZaYbAo9EkRxS0s1xV2vrAmTc=" ,  String.class);
       // WalletApiResponse obj = t.postForObject(url , request ,  WalletApiResponse.class);
       log.info(obj.toString());


        String c ="{\"code\":200,\"message\":\"操作成功\",\"data\":{\"walletAddress\":\"mpBXaEgQSuUfgamzKxwNuJf1gh3Lphf8yG\",\"timestamp\":null,\"accUserName\":\"C7981C5E1086B189\",\"walletType\":null,\"userId\":null,\"platPublicKey\":\"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCJ0MySl4AVDamcdm0GbyytKohHPZoR8cqrPHvS3ckazPMILX/nH1Q//ETF2IRRwfC+C+RKufpgvkTKWWaoSoN2gu6eLgZys5o6PRQ2x08TQ+NkY6MDLh55Uxr09+BnmHlnWLcAVxGnycmYapBK10ieC7s66jJ6VYaeecpLSrj41wIDAQAB\",\"exchageWaPublicKey\":\"MIHgMIGXBgkqhkiG9w0BAwEwgYkCQQD8poLOjhLKuibvzPcRDlJtsHiwXt7LzR60ogjzrhYXrgHzW5Gkfm32NBPF4S7QiZvNEyrNUNmRUb3EPuc3WS4XAkBnhHGyepz0TukaScUUfbGpqvJE8FpDTWSGkx0tFCcbnjUDC3H9c9oXkGmzLik1Yw4cIGI1TQ2iCmxBblC+eUykAgIBgANEAAJBAKpcGLhafe72+VuozDaKnaBi2RitJKrGerYhTb6anXY/Ddo1j8/BU5m06Hed0xcBeOWLwIFTKS/vNAA/NVgCAbo=\"},\"signType\":\"RSA\",\"sign\":\"gblIqdMJmw7OfhpP5YmD/b09A5j7bpk2coPaaX1MuwtzS8y6supcgJwr3Lx6OPlG7Jr8fu8vb81ZE0sSKgR0q4X+7ansZg75aw7au/XwNdaR3+DZbsKRh1XQa8GH/MiWvuSmmeXm5Vdws8P3J0eFN2Y1WYEFJT3cwHhqhFQVcCU=\",\"timestamp\":1532676049772}";
        WalletApiRegResponse kkk = JSON.parseObject(c,WalletApiRegResponse.class);
        log.info("sign={}" , kkk.getSign());
        log.info("data={}",JSON.toJsonString(kkk.getData()));

        boolean f = WalletSignature.rsaCheckContent(JSON.toJsonString(kkk.getData()),kkk.getSign(),RSAKey.getPublicKeyString(),"UTF-8");
        log.info(f+"");

      // log.info( t.postForObject("https://47.105.119.229/wallet-api/api/test" , "UUAGMOA8krf16esFvdf1AoavdocmZpP7Gaoxtklmj8B3xnluZOehwCdD0QxfBh6lIOLq7TOUeb3Z00nm87y4IptKlIn2frYmojAqiXGg5BOIxo1rZOsKY1saRWCo8nZW1ksTR7uxpzM8N0dCBJqZaYbAo9EkRxS0s1xV2vrAmTc=" ,  String.class));


      //  log.info(WalletSignature.rsaDecrypt("UUAGMOA8krf16esFvdf1AoavdocmZpP7Gaoxtklmj8B3xnluZOehwCdD0QxfBh6lIOLq7TOUeb3Z00nm87y4IptKlIn2frYmojAqiXGg5BOIxo1rZOsKY1saRWCo8nZW1ksTR7uxpzM8N0dCBJqZaYbAo9EkRxS0s1xV2vrAmTc=","UTF-8"));







    }



}
