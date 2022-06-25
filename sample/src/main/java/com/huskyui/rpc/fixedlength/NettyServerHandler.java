package com.huskyui.rpc.fixedlength;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author 王鹏
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("client=>server [msg]"+msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client 连接 server 成功");
        ctx.writeAndFlush("hello c Active");
        ServerChannelHolder.clientChannel = ctx.channel();
    }
}
