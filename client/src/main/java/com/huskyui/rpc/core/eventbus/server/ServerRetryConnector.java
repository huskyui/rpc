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
        List<ServerHolder.ServerInfo> disconnectedServerList = ServerHolder.getAllUnConnectedChannel();
        System.out.println("found disconnected server" + disconnectedServerList);
        if (disconnectedServerList.size() > 0) {
            NettyClient.getInstance().connect(disconnectedServerList);
        }
    }
}
