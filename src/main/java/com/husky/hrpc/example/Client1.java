package com.husky.hrpc.example;

import com.husky.hrpc.client.rpc.CallClient;
import com.husky.hrpc.common.config.ZkConfig;
import com.husky.hrpc.common.service.HelloService;

/**
 * @author huskyui
 */

public class Client1 {
    public static void main(String[] args) {
        ZkConfig zkConfig = ZkConfig.builder()
                .hostAddress("121.36.241.65")
                .port(2181)
                .build();
        HelloService helloService = (HelloService) CallClient.call(HelloService.class, zkConfig);
        // sync run method

        for (int i = 0; i < 10; i++) {
            System.out.println(helloService.sayHello("husky" + i));
        }


    }
}
