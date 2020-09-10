package com.husky.hrpc.sever;

import com.husky.hrpc.sever.service.impl.HelloServiceImpl;

import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huskyui
 */

public final class MessageHandlerHolder {
    private static ConcurrentHashMap<String, Object> IMPL_MAP = new ConcurrentHashMap<>(64);

    static {
        IMPL_MAP.put("com.husky.hrpc.common.service.HelloService", new HelloServiceImpl());
    }

    public static Object get(String clazzName) {
        return IMPL_MAP.get(clazzName);
    }
}
