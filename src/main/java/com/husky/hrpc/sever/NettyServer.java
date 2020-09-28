package com.husky.hrpc.sever;

import com.husky.hrpc.common.config.ZkConfig;
import com.husky.hrpc.sever.handler.InvokeHandler;
import com.husky.hrpc.util.NetUtil;
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
    private ZkConfig zkConfig;

    public NettyServer(ZkConfig zkConfig) {
        this.zkConfig = zkConfig;
    }

    private ZkClient zkClient;

    private void initZookeeper() {
        this.zkClient = new ZkClient(zkConfig.getHostAddress() + ":" + zkConfig.getPort());
    }


    public void start() {
        int idlePort = -1;
        try {
            idlePort = NetUtil.getIdlePort();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e.getMessage());
        }
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
                .bind(idlePort)
                .addListener(future -> log.info("server start success : {}", future.isSuccess()));
        initZookeeper();
        int finalIdlePort = idlePort;
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
                    serverInfo.append(finalIdlePort);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                zkClient.createEphemeral("/rpc/" + serverInfo.toString());
            }
        }).start();
    }


}
