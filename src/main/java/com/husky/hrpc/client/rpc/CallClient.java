package com.husky.hrpc.client.rpc;

import com.husky.hrpc.client.InvokerHandlerImpl;
import com.husky.hrpc.common.config.ZkConfig;

import java.lang.reflect.Proxy;

/**
 * @author huskyui
 */

public class CallClient {


    public static <T> T call(Class clazz, ZkConfig zkConfig) {
        T t = (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new InvokerHandlerImpl(clazz, zkConfig));
        return t;
    }


}
