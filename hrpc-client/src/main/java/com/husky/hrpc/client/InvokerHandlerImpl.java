package com.husky.hrpc.client;


import com.husky.hrpc.client.handler.ClientHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author huskyui
 */
@Slf4j
public class InvokerHandlerImpl implements InvocationHandler {
    private Class clazz;

    private ClientHandler handler;

    public InvokerHandlerImpl(Class clazz) {
        ClientHandler clientHandler = new ClientHandler();
        // 此处实现一个 zookeeper,服务治理
        NettyClient nettyClient = new NettyClient("127.0.0.1", 10243, clientHandler);
        nettyClient.start();
        this.handler = clientHandler;
        log.info("invoker handler init success");
        this.clazz = clazz;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        handler.sendMsg(clazz.getName(), method.getName(), args, method.getParameterTypes(), method.getReturnType());
        return null;
    }
}
