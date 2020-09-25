package com.husky.hrpc.client;

import com.husky.hrpc.client.handler.ClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huskyui
 */
@Slf4j
public class NettyClient {

    private Bootstrap bootstrap;
    private ClientHandler clientHandler;
    public static ConcurrentHashMap<String,Channel> serverList = new ConcurrentHashMap<>(64);


    public NettyClient( ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }


    public void start() {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        clientHandler.setEventLoopGroup(eventLoopGroup);
        bootstrap = new Bootstrap();
        bootstrap
                .group(eventLoopGroup)
                .channel(NioSocketChannel.class)
//                .option(ChannelOption.TCP_NODELAY, true)
//                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.SO_REUSEADDR, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new ObjectEncoder())
                                .addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)))
                                .addLast(clientHandler);
                    }
                });
    }

    public void connectServer(String host,int port) {
        // 通过连接，将channel保存起来
        Channel channel = bootstrap.connect(host, port)
                .addListener(future -> {
                    log.info("connect server success!");
                }).channel();

        serverList.put(host+"_"+port,channel);
    }


}
