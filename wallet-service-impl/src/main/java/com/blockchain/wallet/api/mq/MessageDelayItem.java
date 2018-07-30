package com.blockchain.wallet.api.mq;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author shujun
 * @date 2018/7/24
 */
public class MessageDelayItem<T> implements Delayed{

   public MessageDelayItem(){}

    public MessageDelayItem(Long expireTime, T msg) {
        this.expireTime = expireTime;
        this.msg = msg;
    }

    private Long expireTime;
    private T msg;

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public T getMsg() {
        return msg;
    }

    public void setMsg(T msg) {
        this.msg = msg;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return expireTime - System.currentTimeMillis();
    }

    @Override
    public int compareTo(Delayed o) {
        return expireTime.compareTo(((MessageDelayItem)o).getExpireTime());
    }
}
