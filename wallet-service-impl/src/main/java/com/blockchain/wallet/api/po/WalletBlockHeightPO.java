package com.blockchain.wallet.api.po;

public class WalletBlockHeightPO {
    private Integer id;

    private Integer blockHeigth;

    private Long updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBlockHeigth() {
        return blockHeigth;
    }

    public void setBlockHeigth(Integer blockHeigth) {
        this.blockHeigth = blockHeigth;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
}