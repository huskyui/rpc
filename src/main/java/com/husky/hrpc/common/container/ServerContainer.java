package com.husky.hrpc.common.container;


import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huskyui
 */
@Slf4j
public class ServerContainer {
    private final static ConcurrentHashMap<String, Channel> SERVER_CONTAINER = new ConcurrentHashMap<>(64);

    public static void add(String serverPath, Channel channel) {
        log.info("加入 serverPath {} channel {} 到服务器列表中", serverPath, channel);
        SERVER_CONTAINER.put(serverPath, channel);
    }

    public static Channel getChannel(String serverPath) {
        return SERVER_CONTAINER.get(serverPath);
    }

    public static Collection<Channel> getAllChannels() {
        return SERVER_CONTAINER.values();
    }

    public static void remove(String serverPath) {
        log.info("监听到服务器节点失去连接：{}", serverPath);
        SERVER_CONTAINER.remove(serverPath);
    }
}
