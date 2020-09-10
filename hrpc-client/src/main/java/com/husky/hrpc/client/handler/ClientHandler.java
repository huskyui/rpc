package com.husky.hrpc.client.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.husky.hrpc.common.RequestInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author huskyui
 */
@Slf4j
public class ClientHandler extends SimpleChannelInboundHandler<String> {


    private ChannelHandlerContext context;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        log.info("get message {}",msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.context = ctx;
    }

    public void sendMsg() throws JsonProcessingException {
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setClassName("com.husky.hrpc.common.service.HelloService");
        requestInfo.setMethodName("sayHello");
        Object[] parameters = new Object[]{"huskyui"};
        Class[] parameterTypes = new Class[]{String.class};

        requestInfo.setParameters(parameters);
        requestInfo.setParameterTypes(parameterTypes);
        ObjectMapper mapper = new ObjectMapper();
        context.writeAndFlush(mapper.writeValueAsString(requestInfo));
    }
}
