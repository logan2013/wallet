package com.blockchain.wallet.api.internal.util;

import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;

/**
 * Created by Administrator on 2018/6/26.
 */
@Slf4j
public final class RSAKey {

    /**
     *  ras 1024
     */
    private static final String PUBLIC_KEY_PEM_LOCATION = "/register/rsa_public_key.pem";
    private static final String PRIVATE_KEY_PKCS8_PEM_LOCATION = "/register/rsa_private_key_pkcs8.pem";
    private static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDWT2gam6qSMOdj8q3oUFyyHepbmIy3DY6bkq7DZtdJsQ7GXLxolV3Vp030HsWtpAJ+DaMMXX2jErhuuxEXEImGHFZfnBK1v6VY8rdDqcAy8KG90jPP6jersKo7In5ylNEepY5BWErBkkQBn4ijaXnvOkM7IBh44tqbuK7+2+/DNQIDAQAB";
    private static final String PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANZPaBqbqpIw52PyrehQXLId6luYjLcNjpuSrsNm10mxDsZcvGiVXdWnTfQexa2kAn4NowxdfaMSuG67ERcQiYYcVl+cErW/pVjyt0OpwDLwob3SM8/qN6uwqjsifnKU0R6ljkFYSsGSRAGfiKNpee86QzsgGHji2pu4rv7b78M1AgMBAAECgYEA0lZJNKRHuwfocr5CVVpaQC8ATzngEeFqb+J9SCbtcdQnTiTCflrrJ8D2z3xMQ+pOz+0OdBpmLuE2xucvFNYUQhEvi3fF2sKo/XCpdSmXfZdLJx44wwXU7Jlth9hB6DJd9QzKjubp+q9gIHqzicb8QhkU/gQlMUmT9jcHY5p13pkCQQD7RcZAaT6lheYQ7O1nTuovinRY9iYj+lTawHRCaJgjvJWPAa5z7p5Tzl3KViDcHp2mADykyC5GNA2yoqPnvPQnAkEA2lebcDWVB4eGVFzCziMWwTMOwWoK/KVl3twSnXgyWa9wQAOZpOhak4Abnmq2maeaP4gZFlhFA1Q5z7UOVP5bQwJAI1qU41krhFSLo1QCryduUdQUPLQdd7BwIfs0IGfhCB4vmNhuuEcZpccCcUafBvmTyth7r9+uixgl2T2C/EccvwJAWUsOxJsFMakZJJMzD/6FpclttWfaymwR90xlSIUJgmgfLe7K/QgRUtVHdkgKGVjT2juclujOgIgHhIx1KglD5QJBAMV7cnGJHx0gbEm2yzeopvnv8kwfT5LQ8fkokq4B1U9Shr91f3GXMH96DUEiEcL2CR+hHhQ9A+XtyAdn6c8B/bE=";

    public static String getKeyFromFile(String file)  {
        String key = "";
        try {
            key = SupportUtil.readFileAsString(file);
        } catch (Exception ex) {
            ex.printStackTrace();
            return key;
        }
        key = getOriginalKey(key);
        return key;
    }

    public static String getOriginalKey(String key) {
        key = key.replace("-----BEGIN RSA PRIVATE KEY-----", "")
                .replace("-----END RSA PRIVATE KEY-----", "")
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "");
        key = SupportUtil.filterSpaceTab(key);
        key = SupportUtil.filterLineSeparator(key);
        return key;
    }


    public static String getPublicKeyString(){
        String path = RSAKey.class.getClassLoader().getResource(PUBLIC_KEY_PEM_LOCATION).getPath();
        String key = getKeyFromFile(path);
        String t = getOriginalKey(key);
        log.error("PublicKeyString={}" , t);
        return t;
     //   return PUBLIC_KEY;
    }


    public static String getPrivateKeyString(){
        String path = RSAKey.class.getClassLoader().getResource(PRIVATE_KEY_PKCS8_PEM_LOCATION).getPath();
        String key = getKeyFromFile(path);
        String t = getOriginalKey(key);
        log.error("PrivateKeyString={}" , t);
       System.out.println(t);
        return t;
      //  return PRIVATE_KEY;
    }








}

