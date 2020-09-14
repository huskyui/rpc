package com.husky.hrpc.client.proxy;

import com.husky.hrpc.client.InvokerHandlerImpl;

import java.lang.reflect.Proxy;

/**
 * @author huskyui
 */

public class DynamicProxy {


    public static <T> T newProxy(Class clazz) {
        T t = (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new InvokerHandlerImpl(clazz));



        return t;
    }


}
