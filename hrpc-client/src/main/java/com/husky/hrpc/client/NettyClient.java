package com.husky.hrpc.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.husky.hrpc.client.handler.ClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
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
    public ClientHandler clientHandler;


    public NettyClient(String host, Integer port) {
        this.host = host;
        this.port = port;
    }


    public void start() {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        clientHandler = new ClientHandler();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new ObjectEncoder())
                                .addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)))
                                .addLast(clientHandler);
                    }
                }).connect(host,port)
                .addListener(future -> log.info("connect server success :{} ", future.isSuccess()));
    }

    public void sendMsg(){

    }


    public static void main(String[] args) throws JsonProcessingException {
        NettyClient nettyClient = new NettyClient("127.0.0.1",10243);
        nettyClient.start();
        nettyClient.clientHandler.sendMsg();


    }
}
