package com.huksyui.rpc;

import com.huksyui.rpc.etcd.EtcdStarter;
import com.huskyui.rpc.configcenter.IConfigCenter;
import com.huskyui.rpc.configcenter.etcd.EtcdBuilder;

/**
 *
 */
public class App 
{
    public static void main( String[] args ) throws InterruptedException {
        // netty port
        int port = 15789;

        IConfigCenter configCenter = EtcdBuilder.build("127.0.0.1:2379");
//
//        // start server
//        NettyServer nettyServer = new NettyServer();
//        nettyServer.startNettyServer(port);

        // etcd upload info
        EtcdStarter etcdStarter = new EtcdStarter();
        etcdStarter.setServerName("orderCenter");
        etcdStarter.setPort(port);
        etcdStarter.setConfigCenter(configCenter);
        etcdStarter.start();


    }
}
