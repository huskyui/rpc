package com.huskyui.rpc.core.eventbus.server;

import com.huskyui.rpc.netty.NettyClient;
import com.huskyui.rpc.netty.ServerHolder;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author 王鹏
 */
public class ServerRetryConnector {

    public static void retryConnectServer() {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(ServerRetryConnector::reConnectServer, 10, 5, TimeUnit.SECONDS);
    }


    private static void reConnectServer() {
        List<String> allUnConnectedChannel = ServerHolder.getAllUnConnectedChannel();
        System.out.println("found unConnected channel" + allUnConnectedChannel);
        if (allUnConnectedChannel.size() > 0) {
            NettyClient.getInstance().connect(allUnConnectedChannel);
        }
    }
}
