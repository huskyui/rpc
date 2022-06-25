package com.huskyui.rpc.fixedlength;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author 王鹏
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("server=>client [msg] " + msg);
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client connect server successfully ! remote address" + ctx.channel().remoteAddress().toString());
        ClientChannelHolder.serverChannel = ctx.channel();
    }
}
