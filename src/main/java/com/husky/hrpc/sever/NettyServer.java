package com.husky.hrpc.sever;

import com.husky.hrpc.common.service.HelloService;
import com.husky.hrpc.sever.handler.InvokeHandler;
import com.husky.hrpc.sever.service.impl.HelloServiceImpl;
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

/**
 * @author huskyui
 */
@Slf4j
public class NettyServer {
    private int port;

    public NettyServer(int port) {
        this.port = port;
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
    }


    public static void main(String[] args) {
        MessageHandlerHolder.add(HelloService.class, new HelloServiceImpl());
        NettyServer nettyServer = new NettyServer(10243);
        nettyServer.start();
        System.out.println("start");
        System.out.println(System.getProperty("java.class.path"));
    }
}
