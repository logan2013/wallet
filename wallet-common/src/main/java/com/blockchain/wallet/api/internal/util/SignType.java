package com.blockchain.wallet.api.internal.util;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by shujun on 2018/6/26.
 */
@Getter
@Setter
public class SignType {

    private final String name;
    private final String sigAlg;

    private SignType(String name, String sigAlg) {
        this.name = name;
        this.sigAlg = sigAlg;
    }

    public static final SignType MD5 = new SignType("MD5", "MD5");
    public static final SignType DSA = new SignType("DSA",
            "SHA1withDSA");
    public static final SignType DSA_DOTNET = new SignType("DSA_DOTNET",
            "SHA1withDSA");
    public static final SignType RSA = new SignType("RSA", "SHA1withRSA");
    public static final SignType RSA2 = new SignType("RSA2", "SHA256WithRSA");

}
