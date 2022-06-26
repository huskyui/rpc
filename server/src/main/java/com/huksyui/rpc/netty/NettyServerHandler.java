package com.huksyui.rpc.netty;

import com.huskyui.rpc.model.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author 王鹏
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<Message> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        System.out.println("client=>server msg"+ msg.toString());
        Message message = Message.newBuilder()
                .setMessageType(Message.MessageType.RESPONSE)
                .setBody("你好，this is from server").build();
        ctx.writeAndFlush(message);
    }
}
