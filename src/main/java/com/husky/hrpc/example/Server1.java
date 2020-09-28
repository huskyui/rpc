package com.husky.hrpc.example;

import com.husky.hrpc.common.config.ZkConfig;
import com.husky.hrpc.common.service.HelloService;
import com.husky.hrpc.sever.MessageHandlerHolder;
import com.husky.hrpc.sever.NettyServer;
import com.husky.hrpc.sever.service.impl.HelloServiceImpl;

import java.net.UnknownHostException;

/**
 * @author huskyui
 */

public class Server1 {
    public static void main(String[] args) throws UnknownHostException {
        MessageHandlerHolder.add(HelloService.class, new HelloServiceImpl());
        ZkConfig zkConfig = ZkConfig.builder()
                .hostAddress("121.36.241.65")
                .port(2181)
                .build();
        NettyServer nettyServer = new NettyServer(zkConfig);
        nettyServer.start();
        System.out.println("start");
    }
}
