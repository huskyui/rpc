package com.huskyui.rpc.fixedlength;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author 王鹏
 */
public class Client {
    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup(2);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                // 这里将fixedLengthFrameDecoder添加到pipeline,指定长度为20
                                .addLast(new FixedLengthFrameDecoder(20))
                                // 将粘包半包处理的数据，为字符串
                                .addLast(new StringDecoder())
                                // 将客户端发送的消息进行空格补齐，保证长度
                                .addLast(new FixedLengthFrameEncoder(20))
                                .addLast(new NettyClientHandler());
                    }
                });
        try {
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 15788).sync();
            Channel channel = channelFuture.channel();
            channel.writeAndFlush("hello server");
            System.out.println("client 连接 server 成功");
        }catch (Exception e){
            System.out.println("连接失败");
        }

        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(()->{
            if(ClientChannelHolder.serverChannel!=null && ClientChannelHolder.serverChannel.isActive()){
                ClientChannelHolder.serverChannel.writeAndFlush("hello server sch");
            }
        },10,1,TimeUnit.SECONDS);

    }


}
