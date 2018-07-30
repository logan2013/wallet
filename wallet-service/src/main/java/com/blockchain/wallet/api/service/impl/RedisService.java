package com.blockchain.wallet.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 缓存service
 * Created by kai on 18-6-22.
 */
@Service
public class RedisService {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 写入缓存设置时效时间
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 批量删除对应的value
     *
     * @param keys
     */
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * 批量删除key
     *
     * @param pattern
     */
    public void removePattern(final String pattern) {
        Set<Serializable> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0)
            redisTemplate.delete(keys);
    }

    /**
     * 删除对应的value
     *
     * @param key
     */
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public Object get(final String key) {
        Object result = null;
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }

    /**
     * 哈希 添加
     *
     * @param key
     * @param hashKey
     * @param value
     */
    public void hmSet(String key, Object hashKey, Object value) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        hash.put(key, hashKey, value);
    }

    /**
     * 哈希获取数据
     *
     * @param key
     * @param hashKey
     * @return
     */
    public Object hmGet(String key, Object hashKey) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        return hash.get(key, hashKey);
    }

    /**
     * 列表添加
     *
     * @param k
     * @param v
     */
    public void lPush(String k, Object v) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        list.rightPush(k, v);
    }

    /**
     * 列表获取
     *
     * @param k
     * @param l
     * @param l1
     * @return
     */
    public List<Object> lRange(String k, long l, long l1) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.range(k, l, l1);
    }

    /**
     * 清除列表的所有数据
     */
    public void removeList(String key) {
        //获取集合的长度
        Long listSize = redisTemplate.opsForList().size(key);
        if (listSize > 0) {
            redisTemplate.opsForList().remove(key, 0, listSize - 1);
        }
    }

    /**
     * 获取列表的所有数据
     */
    public List<Integer> lRange(String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * 批量添加列表数据
     */
    public void rightPushAll(String key, List<Integer> list) {
        redisTemplate.opsForList().rightPushAll(key, list);
    }

    /**
     * 集合添加
     *
     * @param key
     * @param value
     */
    public Long add(String key, Object value) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        return set.add(key, value);
    }

    /**
     * 集合获取
     *
     * @param key
     * @return
     */
    public Set<Object> getMembers(String key) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        return set.members(key);
    }

    /**
     * 集合获取单个元素
     *
     * @param key
     * @return
     */
    public Object getMember(String key) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        return set.randomMember(key);
    }


    /**
     * 移除并返回集合中的一个随机元素
     * @param key
     * @return
     */
    public Object spop(String key) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        return set.pop(key);
    }

    /**
     * 集合总数
     * @param key
     * @return
     */
    public Long countNum(String key) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        return set.size(key);
    }

    /**
     * 删除集合单个元素
     *
     * @param key
     * @return
     */
    public Long delMember(String key, Object obj) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        return set.remove(key,obj);
    }

    /**
     * 有序集合添加
     *
     * @param key
     * @param value
     * @param scoure
     */
    public void zAdd(String key, Object value, double scoure) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        zset.add(key, value, scoure);
    }

    /**
     * 有序集合获取
     *
     * @param key
     * @param scoure
     * @param scoure1
     * @return
     */
    public Set<Object> rangeByScore(String key, double scoure, double scoure1) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.rangeByScore(key, scoure, scoure1);
    }
}