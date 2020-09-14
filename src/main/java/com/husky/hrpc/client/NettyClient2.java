package com.husky.hrpc.client;

import com.husky.hrpc.client.proxy.DynamicProxy;
import com.husky.hrpc.common.service.HelloService;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.concurrent.*;

/**
 * @author huskyui
 */

public class NettyClient2 {
    public static void main(String[] args) {

        HelloService helloService = (HelloService) DynamicProxy.newProxy(HelloService.class);
        ThreadFactory namedThreadFactory = new DefaultThreadFactory("hrpc");

        ExecutorService executorService = new ThreadPoolExecutor(10, 200, 0L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());


        for (int i = 0; i < 100; i++) {
            int finalI = i;
            executorService.execute(() -> {
                helloService.sayHello("adios" + finalI);
            });
        }
    }
}
