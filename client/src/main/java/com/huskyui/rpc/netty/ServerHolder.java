package com.huskyui.rpc.netty;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 王鹏
 */
public class ServerHolder {

    private static Multimap<String/*serverName*/, ServerInfo/*serverInfo*/> HOLDER = HashMultimap.create();

    public synchronized static void addServer(String serverName,String address,Channel channel){
        HOLDER.put(serverName,new ServerInfo(serverName,address,channel));
    }

    private static boolean channelIsOk(Channel channel){
        return channel != null && channel.isActive();
    }

    public static void dealChannelInactive(String address){

    }

    public static List<ServerInfo> getAllUnConnectedChannel() {
        List<ServerInfo> list = new ArrayList<>();
        for (String serverName : HOLDER.keySet()) {
            Collection<ServerInfo> serverInfos = HOLDER.get(serverName);
            for (ServerInfo serverInfo : serverInfos) {
                if (!channelIsOk(serverInfo.getChannel())){
                    list.add(serverInfo);
                }
            }
        }
        return list;
    }


    public static class ServerInfo{
        private String address;
        private Channel channel;

        private String serverName;

        public ServerInfo(String serverName,String address, Channel channel) {
            this.serverName = serverName;
            this.address = address;
            this.channel = channel;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Channel getChannel() {
            return channel;
        }

        public void setChannel(Channel channel) {
            this.channel = channel;
        }

        public String getServerName() {
            return serverName;
        }

        public void setServerName(String serverName) {
            this.serverName = serverName;
        }
    }
    


}
