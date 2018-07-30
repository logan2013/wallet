package com.blockchain.wallet.api.internal.util;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by shujun on 2018/6/26.
 */
@Getter
@Setter
public class KeyType {

    private String name;
    private int length;

    public KeyType(String name, int length) {
        this.name = name;
        this.length = length;
    }

    public static final KeyType MD5 = new KeyType("MD5", 32);
    public static final KeyType DSA1024 = new KeyType("DSA", 1024);
    public static final KeyType RSA1024 = new KeyType("RSA", 1024);
    public static final KeyType DSA2048 = new KeyType("DSA", 2048);
    public static final KeyType RSA2048 = new KeyType("RSA", 2048);

}
