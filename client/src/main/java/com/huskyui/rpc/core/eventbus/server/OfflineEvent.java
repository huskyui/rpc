package com.huskyui.rpc.core.eventbus.server;

import io.netty.channel.Channel;

/**
 * @author 王鹏
 */
public class OfflineEvent {

    private Channel channel;

    public OfflineEvent(Channel channel) {
        this.channel = channel;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
