package com.huksyui.rpc.netty;

import com.huskyui.rpc.enums.MessageType;
import com.huskyui.rpc.model.Message;
import com.huskyui.rpc.utils.JsonUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author 王鹏
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<Message> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        System.out.println("client=>server msg"+ JsonUtils.toJson(msg));
        ctx.writeAndFlush(new Message(MessageType.RESPONSE,"你好，this is from server"));
    }
}
