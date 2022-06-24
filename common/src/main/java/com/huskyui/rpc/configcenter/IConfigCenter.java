package com.huskyui.rpc.configcenter;

import com.ibm.etcd.api.KeyValue;
import com.ibm.etcd.client.kv.KvClient;

import java.util.List;

/**
 * @author 王鹏
 */
public interface IConfigCenter {
    /**
     * put key value
     */
    void put(String key,String value);

    /**
     * 存入
     */
    void put(String key,String value,long leaseId);

    /**
     * 存入key,value,和过期时间，单位秒
     * @param key
     * @param value
     * @param ttl
     * @return
     */
    long putAndGrant(String key,String value,long ttl);

    /**
     * 给key设置新的leaseId
     * @param key
     * @param leaseId
     */
    void setLease(String key,long leaseId);


    void delete(String key);

    String get(String key);

    List<KeyValue> getPrefix(String key);

    KvClient.WatchIterator watch(String key);

    KvClient.WatchIterator watchPrefix(String key);

    long keepAlive(String key,String value,int frequencySecs,int minTtl) throws Exception;

    KeyValue getKv(String key);


}
