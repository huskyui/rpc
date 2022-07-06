package com.huskyui.rpc.core.eventbus.server;

import com.google.common.eventbus.Subscribe;
import com.huskyui.rpc.netty.ServerHolder;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.net.SocketAddress;

/**
 * @author 王鹏
 */
@Slf4j
public class ChannelOfflineSubscriber {

    @Subscribe
    public void offline(OfflineEvent event){
        Channel channel = event.getChannel();
        SocketAddress socketAddress = channel.remoteAddress();
        System.out.println("remote server offline "+ socketAddress.toString());
        ServerHolder.dealChannelInactive(socketAddress.toString().substring(1));
        channel.close();
    }


}
