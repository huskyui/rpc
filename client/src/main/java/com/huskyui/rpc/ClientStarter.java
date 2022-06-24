package com.huskyui.rpc;

import com.huskyui.rpc.core.eventbus.server.ChannelOfflineSubscriber;
import com.huskyui.rpc.core.eventbus.EventBusCenter;
import lombok.Builder;
import lombok.Data;

/**
 * @author 王鹏
 */
@Data
@Builder
public class ClientStarter {
    private String appName;

    private String etcdAddress;

    public void startPipeline(){
        registerEventBus();
    }


    private void registerEventBus(){
        EventBusCenter.getInstance().register(new ChannelOfflineSubscriber());
    }


}
