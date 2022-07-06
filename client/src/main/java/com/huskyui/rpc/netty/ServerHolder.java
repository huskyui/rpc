package com.huskyui.rpc.netty;

import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 王鹏
 */
public class ServerHolder {

    private static Map<String, Channel> HOLDER = new ConcurrentHashMap<>();

    public synchronized static void addServer(String address,Channel channel){
        HOLDER.put(address,channel);
    }

    private static boolean channelIsOk(Channel channel){
        return channel != null && channel.isActive();
    }

    public static void dealChannelInactive(String address){
        HOLDER.remove(address);
    }

    public static List<String> getAllUnConnectedChannel() {
        List<String> list = new ArrayList<>();
        for (String address : HOLDER.keySet()) {
            Channel channel = HOLDER.get(address);
            if (!channelIsOk(channel)) {
                list.add(address);
            }
        }
        return list;
    }
    


}
