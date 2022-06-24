package com.huskyui.rpc.netty;

import com.huskyui.rpc.codec.MsgDecoder;
import com.huskyui.rpc.codec.MsgEncoder;
import com.huskyui.rpc.constant.Constant;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

import java.nio.charset.StandardCharsets;

/**
 * @author 王鹏
 */
public class NettyClient {

    private static final NettyClient instance = new NettyClient();

    private Bootstrap bootstrap;

    public static NettyClient getInstance(){
        return instance;
    }

    private NettyClient(){
        if (bootstrap == null){

        }
    }

    private Bootstrap initBootstrap(){
        EventLoopGroup group = new NioEventLoopGroup(2);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE,true)
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ByteBuf delimiter = Unpooled.copiedBuffer(Constant.DELIMITER.getBytes(CharsetUtil.UTF_8));
                        ch.pipeline()
                                .addLast(new DelimiterBasedFrameDecoder(Constant.MAX_LENGTH,delimiter))
                                .addLast(new MsgDecoder())
                                .addLast(new MsgEncoder())
                                .addLast(new IdleStateHandler(0,0,30))
                                .addLast(new NettyClientHandler());
                    }
                });
        return bootstrap;
    }


}
