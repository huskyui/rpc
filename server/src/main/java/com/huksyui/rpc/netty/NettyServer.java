package com.huksyui.rpc.netty;

import com.huskyui.rpc.codec.MsgDecoder;
import com.huskyui.rpc.codec.MsgEncoder;
import com.huskyui.rpc.constant.Constant;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;

/**
 * @author 王鹏
 */
public class NettyServer {

    public void startNettyServer(int port) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup(10);

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup,workGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.DEBUG))
                .option(ChannelOption.SO_BACKLOG,1024)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ByteBuf delimiterByteBuf = Unpooled.copiedBuffer(Constant.DELIMITER.getBytes(CharsetUtil.UTF_8));
                        NettyServerHandler nettyServerHandler = new NettyServerHandler();
                        ch.pipeline()
                                .addLast(new DelimiterBasedFrameDecoder(Constant.MAX_LENGTH,delimiterByteBuf))
                                .addLast(new MsgEncoder())
                                .addLast(new MsgDecoder())
                                .addLast(nettyServerHandler);
                    }
                });
        ChannelFuture channelFuture = bootstrap.bind(port).sync();
    }
}
