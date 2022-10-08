package com.huskyui.rpc;

import com.huskyui.rpc.configcenter.IConfigCenter;
import com.huskyui.rpc.configcenter.etcd.EtcdBuilder;
import com.huskyui.rpc.model.Message;
import com.huskyui.rpc.netty.NettyClient;
import com.huskyui.rpc.netty.ServerHolder;
import com.ibm.etcd.api.KeyValue;
import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        ClientStarter clientStarter = new ClientStarter("client","127.0.0.1:2379");

        clientStarter.startPipeline();
        // todo 将这边的方法封装到后台
        IConfigCenter configCenter = EtcdBuilder.build("127.0.0.1:2379");
        List<KeyValue> keyValueList = configCenter.getPrefix("/server");
        List<ServerHolder.ServerInfo> addressList = new ArrayList<>();
        for (KeyValue keyValue : keyValueList) {
            String key = keyValue.getKey().toStringUtf8();
            key = key.substring(0 ,key.lastIndexOf("/"));
            String[] split = key.split("/");
            if(split.length != 4){
                System.out.println("found error");
            }
            String value = keyValue.getValue().toStringUtf8();
            ServerHolder.ServerInfo serverInfo = new ServerHolder.ServerInfo(key,value,null);
            addressList.add(serverInfo);
        }
        NettyClient nettyClient = NettyClient.getInstance();
        nettyClient.connect(addressList);

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Collection<ServerHolder.ServerInfo> serverInfoCollection = ServerHolder.getServerInfo("/server/" + "orderCenter");
        // serverInfoCollection is empty
        if (!serverInfoCollection.isEmpty()){
            for (ServerHolder.ServerInfo serverInfo : serverInfoCollection) {
                Channel channel = serverInfo.getChannel();
                Message message = Message.newBuilder()
                        .setMessageType(Message.MessageType.RESPONSE)
                        .setBody("hello, send message from client to all server")
                        .build();
                channel.writeAndFlush(message);
            }
        }


    }
}
