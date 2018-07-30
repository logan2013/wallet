package com.blockchain.wallet.api.pojo;

import java.util.List;

public class ScriptPubKey {

    private List<String> addresses ;

    private String asm;

    private String hex;

    private String type;

    private int reqSigs;

    public List<String> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<String> addresses) {
        this.addresses = addresses;
    }

    public String getAsm() {
        return asm;
    }

    public void setAsm(String asm) {
        this.asm = asm;
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getReqSigs() {
        return reqSigs;
    }

    public void setReqSigs(int reqSigs) {
        this.reqSigs = reqSigs;
    }
}
