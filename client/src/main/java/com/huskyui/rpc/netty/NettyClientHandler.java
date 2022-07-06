package com.huskyui.rpc.netty;

import com.huskyui.rpc.core.eventbus.EventBusCenter;
import com.huskyui.rpc.core.eventbus.server.OfflineEvent;
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
                Message message = Message.newBuilder()
                        .setMessageType(Message.MessageType.HEART_BEAT)
                        .setBody("hello")
                        .build();
                ctx.writeAndFlush(message);
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        System.out.println("server=>client msg"+"[msgBody]"+msg.getBody()+"[msgType]"+msg.getMessageType().toString());
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // send msg to notify server
        Message message = Message.newBuilder()
                .setMessageType(Message.MessageType.ONLINE)
                .setBody("i am online")
                .build();
        ctx.writeAndFlush(message);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // post offline event
        EventBusCenter.getInstance().post(new OfflineEvent(ctx.channel()));
    }
}
