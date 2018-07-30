package com.blockchain.wallet.api.pojo;

import java.util.Date;

/**
 * Created by zhangkai on 2018/6/7.
 */
public class UserCoinRecordEntity {

    private  String coinType;

    private String  address;

    private int comfirmCount;

    private String pType;

    private String status;

    private String txId;


    private Double value;

    private String txid;

    public String getCoinType() {
        return coinType;
    }

    public void setCoinType(String coinType) {
        this.coinType = coinType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getComfirmCount() {
        return comfirmCount;
    }

    public void setComfirmCount(int comfirmCount) {
        this.comfirmCount = comfirmCount;
    }

    public String getpType() {
        return pType;
    }

    public void setpType(String pType) {
        this.pType = pType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Date getCreateTm() {
        return createTm;
    }

    public void setCreateTm(Date createTm) {
        this.createTm = createTm;
    }

    public String getUerId() {
        return uerId;
    }

    public void setUerId(String uerId) {
        this.uerId = uerId;
    }

    private Date createTm;

    private String uerId;


    public void setTxid(String txid) {
        this.txid = txid;
    }
}
