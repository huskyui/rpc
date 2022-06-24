package com.huskyui.rpc;

import com.google.common.collect.Lists;
import com.huskyui.rpc.netty.NettyClient;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        ClientStarter clientStarter = new ClientStarter("client","127.0.0.1:2379");

        clientStarter.startPipeline();


        NettyClient nettyClient = NettyClient.getInstance();
        nettyClient.connect(Lists.newArrayList("127.0.0.1:15789"));
    }
}
