package com.blockchain.wallet.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author zhangkai
 * @date 2018/7/13
 */
@Data
public class WalletUsdtResponseDTO implements Serializable {
    private static final long serialVersionUID = 5924055361543516180L;
    private String txid;
    private Double fee;
    private String  sendingaddress;
    private String referenceaddress;
    private Boolean ismine;
    private Integer version;
    private String type_int;
    private String type;
    private Long propertyid;
    private Boolean divisible;
    private Double amount;
    private Boolean valid;
    private String blockhash;
    private Long blocktime;
    private Long positioninblock;
    private Long block;
    private Integer confirmations;

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public String getSendingaddress() {
        return sendingaddress;
    }

    public void setSendingaddress(String sendingaddress) {
        this.sendingaddress = sendingaddress;
    }

    public String getReferenceaddress() {
        return referenceaddress;
    }

    public void setReferenceaddress(String referenceaddress) {
        this.referenceaddress = referenceaddress;
    }

    public Boolean getIsmine() {
        return ismine;
    }

    public void setIsmine(Boolean ismine) {
        this.ismine = ismine;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getType_int() {
        return type_int;
    }

    public void setType_int(String type_int) {
        this.type_int = type_int;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getPropertyid() {
        return propertyid;
    }

    public void setPropertyid(Long propertyid) {
        this.propertyid = propertyid;
    }

    public Boolean getDivisible() {
        return divisible;
    }

    public void setDivisible(Boolean divisible) {
        this.divisible = divisible;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public String getBlockhash() {
        return blockhash;
    }

    public void setBlockhash(String blockhash) {
        this.blockhash = blockhash;
    }

    public Long getBlocktime() {
        return blocktime;
    }

    public void setBlocktime(Long blocktime) {
        this.blocktime = blocktime;
    }

    public Long getPositioninblock() {
        return positioninblock;
    }

    public void setPositioninblock(Long positioninblock) {
        this.positioninblock = positioninblock;
    }

    public Long getBlock() {
        return block;
    }

    public void setBlock(Long block) {
        this.block = block;
    }

    public Integer getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(Integer confirmations) {
        this.confirmations = confirmations;
    }
}
