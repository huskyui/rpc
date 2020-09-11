package com.husky.hrpc.client.proxy;

import com.husky.hrpc.client.NettyClient;
import com.husky.hrpc.client.handler.ClientHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.Future;

/**
 * @author huskyui
 */

public class DynamicProxy {

    public static <T> T newProxy(Class clazz) {
        T t = (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                ClientHandler handler = new ClientHandler();
                // 此处实现一个 zookeeper,服务治理
                NettyClient nettyClient = new NettyClient("127.0.0.1", 10243, handler);
                nettyClient.start();
                method.getReturnType();
                handler.sendMsg(clazz.getName(), method.getName(), args, method.getParameterTypes(),method.getReturnType());
                return null;
            }
        });
        return t;
    }
}
