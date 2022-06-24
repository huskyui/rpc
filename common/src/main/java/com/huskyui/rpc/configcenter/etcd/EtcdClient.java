package com.huskyui.rpc.configcenter.etcd;

import com.google.protobuf.ByteString;
import com.huskyui.rpc.configcenter.IConfigCenter;
import com.ibm.etcd.api.KeyValue;
import com.ibm.etcd.api.LeaseGrantResponse;
import com.ibm.etcd.api.RangeResponse;
import com.ibm.etcd.client.KvStoreClient;
import com.ibm.etcd.client.kv.KvClient;
import com.ibm.etcd.client.lease.LeaseClient;
import com.ibm.etcd.client.lease.PersistentLease;
import com.ibm.etcd.client.lock.LockClient;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author 王鹏
 */
public class EtcdClient implements IConfigCenter {

    private KvClient kvClient;
    private LeaseClient leaseClient;

    private LockClient lockClient;


    public EtcdClient(KvStoreClient kvStoreClient) {
        this.kvClient = kvStoreClient.getKvClient();
        this.leaseClient = kvStoreClient.getLeaseClient();
        this.lockClient = kvStoreClient.getLockClient();
    }

    @Override
    public void put(String key, String value) {
        kvClient.put(ByteString.copyFromUtf8(key),ByteString.copyFromUtf8(value)).sync();
    }

    @Override
    public void put(String key, String value, long leaseId) {
        kvClient.put(ByteString.copyFromUtf8(key),ByteString.copyFromUtf8(value),leaseId).sync();
    }

    @Override
    public long putAndGrant(String key, String value, long ttl) {
        LeaseGrantResponse lease = leaseClient.grant(ttl).sync();
        put(key,value,lease.getID());
        return lease.getID();
    }

    @Override
    public void setLease(String key, long leaseId) {
        kvClient.setLease(ByteString.copyFromUtf8(key),leaseId);
    }

    @Override
    public void delete(String key) {
        kvClient.delete(ByteString.copyFromUtf8(key)).sync();
    }

    @Override
    public String get(String key) {
        RangeResponse rangeResponse = kvClient.get(ByteString.copyFromUtf8(key)).sync();
        List<KeyValue> kvsList = rangeResponse.getKvsList();
        if (kvsList == null || kvsList.isEmpty()){
            return null;
        }
        return kvsList.get(0).getValue().toStringUtf8();
    }

    @Override
    public List<KeyValue> getPrefix(String key) {
        RangeResponse rangeResponse = kvClient.get(ByteString.copyFromUtf8(key)).asPrefix().sync();
        return rangeResponse.getKvsList();
    }

    @Override
    public KvClient.WatchIterator watch(String key) {
        return kvClient.watch(ByteString.copyFromUtf8(key)).start();
    }

    @Override
    public KvClient.WatchIterator watchPrefix(String key) {
        return kvClient.watch(ByteString.copyFromUtf8(key)).asPrefix().start();
    }

    @Override
    public long keepAlive(String key, String value, int frequencySecs, int minTtl) throws Exception {
        PersistentLease lease = leaseClient.maintain().leaseId(System.currentTimeMillis()).keepAliveFreq(frequencySecs).minTtl(minTtl).start();
        long newId = lease.get(3, TimeUnit.SECONDS);
        put(key,value,newId);
        return newId;
    }

    @Override
    public KeyValue getKv(String key) {
        RangeResponse sync = kvClient.get(ByteString.copyFromUtf8(key)).sync();
        List<KeyValue> kvsList = sync.getKvsList();
        if(kvsList.isEmpty()){
            return null;
        }
        return kvsList.get(0);
    }


}
