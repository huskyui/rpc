package com.huksyui.rpc.etcd;

import com.huskyui.rpc.configcenter.IConfigCenter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author 王鹏
 */
public class EtcdStarter {
    private IConfigCenter configCenter;

    private String serverName;

    private int port;

    private final static String SERVER_PATH = "/server";


    public EtcdStarter() {
    }

    public void setConfigCenter(IConfigCenter configCenter) {
        this.configCenter = configCenter;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void start() {
        updateSelfInfo();
    }

    private void updateSelfInfo() {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            configCenter.putAndGrant(generateAppName(), generateIpAndPort(), 8);
        }, 1, 5, TimeUnit.SECONDS);
    }

    private String generateAppName() {
        // fixme 127.0.0.1改成hostname
        return SERVER_PATH + "/" + this.serverName+"/"+"127.0.0.1";
    }

    private String generateIpAndPort() {
        // fixme get localIp
        return "127.0.0.1:" + port;
    }

}
