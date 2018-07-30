package com.blockchain.wallet.api.internal.util.codec;

import java.nio.charset.StandardCharsets;

/**
 * Created by Administrator on 2018/6/25.
 */
public class JdkBase64 {

    public static String encryptBase64(String content) {
        return java.util.Base64.getEncoder().encodeToString(content.getBytes(StandardCharsets.UTF_8));
    }

    public static String decryptBase64(String content) {
        return new String(java.util.Base64.getDecoder().decode(content), StandardCharsets.UTF_8);
    }

    public static String encryptBase64(byte[] content) {
        return java.util.Base64.getEncoder().encodeToString(content);
    }
}
