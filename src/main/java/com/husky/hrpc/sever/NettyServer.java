package com.husky.hrpc.sever;

import com.husky.hrpc.sever.handler.InvokeHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author huskyui
 */
@Slf4j
public class NettyServer {
    private int port;
    private ZkClient zkClient;

    public NettyServer(int port) {
        this.port = port;
    }

    private void initZookeeper() {
        this.zkClient = new ZkClient("121.36.241.65:2181");
    }


    public void start() {
        ServerBootstrap bootstrap = new ServerBootstrap();
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        bootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new ObjectEncoder())
                                .addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)))
                                .addLast(new InvokeHandler());
                    }
                })
                .bind(port)
                .addListener(future -> log.info("server start success : {}", future.isSuccess()));
        initZookeeper();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!zkClient.exists("/rpc")) {
                    zkClient.createPersistent("/rpc");
                }
                StringBuilder serverInfo = new StringBuilder();
                try {
                    serverInfo.append(InetAddress.getLocalHost().getHostAddress());
                    serverInfo.append("_");
                    serverInfo.append(port);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                zkClient.createEphemeral("/rpc/" + serverInfo.toString());
            }
        }).start();
    }


}
