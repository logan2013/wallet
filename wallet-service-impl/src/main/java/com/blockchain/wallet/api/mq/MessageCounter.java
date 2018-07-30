package com.blockchain.wallet.api.mq;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Administrator
 * @date 2018/7/24
 */
@Slf4j
public class MessageCounter<T> {
    private final DelayQueue<MessageDelayItem<T>> queues = new DelayQueue();
    private final ConcurrentHashMap<T , AtomicInteger> counters = new ConcurrentHashMap();
    private final int MAX_RETRY_COUNT = 3;

    private Object lock = new Object();

    private MessageCounter(){
        MessageDelayItemTask<T> task = new MessageDelayItemTask<T>(queues , counters , lock);
        new Thread(task).start();
    }

    public static MessageCounter getInstance(){
        return MessageCounterHolder.INSTANCE;
    }

    public boolean add(T msg){
        synchronized (lock){
            if(counters.containsKey(msg)){
                AtomicInteger num =  counters.get(msg);
                if(num.intValue() >= MAX_RETRY_COUNT){
                    return false;
                }else{
                    num.incrementAndGet();
                    counters.put(msg , num);
                    return true;
                }
            }
            counters.put(msg , new AtomicInteger(1));
            // 存活期一个小时
            MessageDelayItem<T> item = new MessageDelayItem(new Long(System.currentTimeMillis()+ 60*60*1000) , msg);
            queues.add(item);
            return true;
        }
    }

    public static class MessageDelayItemTask<T> implements Runnable {
        private final DelayQueue<MessageDelayItem<T>> queue;
        private final ConcurrentHashMap<T , AtomicInteger> counters;
        private final Object lock ;

        public MessageDelayItemTask(DelayQueue<MessageDelayItem<T>> queue, ConcurrentHashMap<T, AtomicInteger> counters, Object lock) {
            this.queue = queue;
            this.counters = counters;
            this.lock = lock;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    MessageDelayItem<T> item = queue.take();
                    T msg = item.getMsg();
                    synchronized (lock){
                        counters.remove(msg);
                    }
                } catch (InterruptedException e) {
                    log.error(e.getMessage());
                }
            }
        }
    }


    private static class MessageCounterHolder {
        private static MessageCounter INSTANCE = new MessageCounter();
    }
}
