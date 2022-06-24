package com.huskyui.rpc.netty;

import com.huskyui.rpc.enums.MessageType;
import com.huskyui.rpc.model.Message;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 王鹏
 */
@ChannelHandler.Sharable
@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler<Message> {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent){
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if(idleStateEvent.state() == IdleState.ALL_IDLE){
                // send heartbeat to sever
                ctx.writeAndFlush(new Message(MessageType.HEART_BEAT,"hello"));
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {

    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // send msg to notify server
        ctx.writeAndFlush(new Message(MessageType.ONLINE,"i am online"));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // post event
    }
}
