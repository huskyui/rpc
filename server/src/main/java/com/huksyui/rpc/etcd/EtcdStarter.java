package com.huksyui.rpc.etcd;

import com.huskyui.rpc.configcenter.IConfigCenter;

import java.security.SecureRandom;
import java.util.Random;
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

    private String randomStr = "";


    public EtcdStarter() {
        this.getRandomStr();
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
        return SERVER_PATH + "/" + this.serverName + "/" + this.randomStr;
    }

    private String generateIpAndPort() {
        // fixme get localIp
        return "127.0.0.1:" + port;
    }

    private void  getRandomStr() {
        SecureRandom random = new SecureRandom();
        this.randomStr = System.currentTimeMillis() + "" + random.nextInt(10000);
    }

}
