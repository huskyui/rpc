package com.husky.hrpc.example;

import com.husky.hrpc.client.rpc.CallClient;
import com.husky.hrpc.common.config.ZkConfig;
import com.husky.hrpc.common.service.HelloService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author huskyui
 */
@Slf4j
public class Client1 {
    public static void main(String[] args) {
        ZkConfig zkConfig = ZkConfig.builder()
                .hostAddress("121.36.241.65")
                .port(2181)
                .build();
        HelloService helloService = (HelloService) CallClient.call(HelloService.class, zkConfig);
        // sync run method

        for (int i = 0; i < 100; i++) {
            log.info(" {}", helloService.sayHello("husky " + i));
        }


    }
}
