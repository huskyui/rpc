package com.huskyui.rpc;

import com.huskyui.rpc.core.eventbus.server.ChannelOfflineSubscriber;
import com.huskyui.rpc.core.eventbus.EventBusCenter;
import lombok.Builder;
import lombok.Data;

import static com.huskyui.rpc.core.eventbus.server.ServerRetryConnector.retryConnectServer;

/**
 * @author 王鹏
 */
@Data
@Builder
public class ClientStarter {
    private String appName;

    private String etcdAddress;

    public void startPipeline(){
        // 处理不同的事件
        registerEventBus();

        // 重新连接服务器
        retryConnectServer();

    }


    private void registerEventBus(){
        EventBusCenter.getInstance().register(new ChannelOfflineSubscriber());
    }


}
