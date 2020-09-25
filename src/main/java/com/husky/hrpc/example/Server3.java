package com.husky.hrpc.example;

import com.husky.hrpc.common.service.HelloService;
import com.husky.hrpc.sever.MessageHandlerHolder;
import com.husky.hrpc.sever.NettyServer;
import com.husky.hrpc.sever.service.impl.HelloServiceImpl;

import java.net.UnknownHostException;

/**
 * @author huskyui
 */

public class Server3 {
    public static void main(String[] args) throws UnknownHostException {
        MessageHandlerHolder.add(HelloService.class, new HelloServiceImpl());
        NettyServer nettyServer = new NettyServer(10245);
        nettyServer.start();
        System.out.println("start");
    }
}
