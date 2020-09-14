package com.husky.hrpc.sever;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huskyui
 */

public class MessageHandlerHolder {
    private static ConcurrentHashMap<String, Object> IMPL_MAP = new ConcurrentHashMap<>(64);

    public static Object get(String clazzName) {
        return IMPL_MAP.get(clazzName);
    }

    public static void add(Class clazz, Object implObj) {
        IMPL_MAP.put(clazz.getName(), implObj);
    }
}
