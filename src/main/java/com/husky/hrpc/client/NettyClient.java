package com.husky.hrpc.client;

import com.husky.hrpc.client.future.RpcFuture;
import com.husky.hrpc.client.handler.ClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

/**
 * @author huskyui
 */
@Slf4j
public class NettyClient {

    private Bootstrap bootstrap;
    private ClientHandler clientHandler;


    public NettyClient(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }


    public void start() {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap
                .group(eventLoopGroup)
                .channel(NioSocketChannel.class)
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

    public Channel connectServer(String host, int port) throws ExecutionException, InterruptedException {
        // 通过连接，将channel保存起来


        // get channel is async,so we should get channel in callback.
        //        // method1 use CompletableFuture
        //        //        CompletableFuture<Channel> completableFuture = new CompletableFuture();
        //        //        bootstrap.connect(host, port)
        //        //                .addListener((ChannelFutureListener) future -> {
        //        //                    if (future.isSuccess()) {
        //        //                        completableFuture.complete(future.channel());
        //        //                    }
        //        //                }).channel();
        //        //        return completableFuture.get();

        // method2 use RpcFuture
        RpcFuture rpcFuture = new RpcFuture();
        bootstrap.connect(host, port)
                .addListener((ChannelFutureListener) future -> {
                    if (future.isSuccess()) {
                        rpcFuture.success(future.channel());
                    }
                }).channel();
        // todo 參考其他继承future，实现长时间无法连接，直接报错
        return (Channel) rpcFuture.get();
    }


}
