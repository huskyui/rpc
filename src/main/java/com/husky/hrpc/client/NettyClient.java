package com.husky.hrpc.client;

import com.husky.hrpc.client.handler.ClientHandler;
import io.netty.bootstrap.Bootstrap;
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

/**
 * @author huskyui
 */
@Slf4j
public class NettyClient {

    private String host;
    private Integer port;
    private Bootstrap bootstrap;
    private ClientHandler clientHandler;


    public NettyClient(String host, Integer port, ClientHandler clientHandler) {
        this.host = host;
        this.port = port;
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
                }).connect(host, port)
                .addListener(future -> log.info("connect server success :{} ", future.isSuccess()));
    }


}
