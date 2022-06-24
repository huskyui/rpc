package com.huskyui.rpc.configcenter.etcd;



/**
 * @author 王鹏
 */
public class EtcdBuilder {
    public static EtcdClient build(String endPoints){
        return new EtcdClient(com.ibm.etcd.client.EtcdClient.forEndpoints(endPoints).withPlainText().build());
    }
}
