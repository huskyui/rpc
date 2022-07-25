package com.huskyui.rpc.etcd;

import com.huskyui.rpc.configcenter.IConfigCenter;
import com.ibm.etcd.api.KeyValue;

import java.util.List;

/**
 * @author 王鹏
 */
public class EtcdStarter {

    private static final String SERVER_PATH = "/server";

    private IConfigCenter configCenter;

    public EtcdStarter() {
    }

    public void setConfigCenter(IConfigCenter configCenter) {
        this.configCenter = configCenter;
    }

    private void start(){

    }



    private void fetchServerInfo(){
        List<KeyValue> prefix = configCenter.getPrefix(SERVER_PATH);
    }
}
