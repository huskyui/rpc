package com.huskyui.rpc;

import com.huskyui.rpc.configcenter.IConfigCenter;
import com.huskyui.rpc.configcenter.etcd.EtcdBuilder;
import com.ibm.etcd.api.KeyValue;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author 王鹏
 */
public class ConfigCenterTest {
    private IConfigCenter configCenter;

    @Before
    public void before() {
        configCenter = EtcdBuilder.build("127.0.0.1:2379");
    }


    @Test
    public void putTest() {
        configCenter.put("test_key", "test_value");
    }

    @Test
    public void getPrefix() {
        configCenter.put("/rpc/server1", "127.0.0.1");
        configCenter.put("/rpc/server2", "127.0.0.1");
        List<KeyValue> kvsList = configCenter.getPrefix("/rpc");
        System.out.println("get all kvsList");
        for (KeyValue kv : kvsList) {
            System.out.println("kv" + " [key]" + kv.getKey() + "[value]" + kv.getValue());
        }
    }

}
