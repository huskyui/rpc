package com.husky.hrpc.client;

import com.husky.hrpc.client.proxy.DynamicProxy;
import com.husky.hrpc.common.service.HelloService;

/**
 * @author huskyui
 */

public class NettyClient2 {
    public static void main(String[] args) {
        HelloService helloService = (HelloService) DynamicProxy.newProxy(HelloService.class);
        for (int i = 0; i < 100; i++) {
            helloService.sayHello("adios");
        }
    }
}
