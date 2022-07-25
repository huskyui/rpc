package com.huskyui.rpc;

import com.google.common.collect.ListMultimap;
import com.huskyui.rpc.configcenter.IConfigCenter;
import com.huskyui.rpc.configcenter.etcd.EtcdBuilder;
import com.ibm.etcd.api.KeyValue;

import java.util.Arrays;
import java.util.List;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
//        ClientStarter clientStarter = new ClientStarter("client","127.0.0.1:2379");
//
//        clientStarter.startPipeline();
//
//
//        NettyClient nettyClient = NettyClient.getInstance();
//        nettyClient.connect(Lists.newArrayList("127.0.0.1:15789"));
        IConfigCenter configCenter = EtcdBuilder.build("127.0.0.1:2379");
        List<KeyValue> keyValueList = configCenter.getPrefix("/server");
        for (KeyValue keyValue : keyValueList) {
            String key = keyValue.getKey().toStringUtf8();
            String[] split = key.split("/");
            if(split.length != 4){
                System.out.println("found error");
            }
            String value = keyValue.getValue().toStringUtf8();
            System.out.println("[key]" + key + "[value]" + value);
        }

    }
}
