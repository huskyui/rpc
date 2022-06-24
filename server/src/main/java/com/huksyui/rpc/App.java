package com.huksyui.rpc;

import com.huksyui.rpc.netty.NettyServer;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws InterruptedException {
        NettyServer nettyServer = new NettyServer();
        nettyServer.startNettyServer(15789);
    }
}
