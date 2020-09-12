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
        log.info("get message {}", msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("channel active");
        this.context = ctx;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("channel inactive");
    }

    public void sendMsg(String className, String methodName, Object[] parameters, Class[] parameterTypes, Class returnType) throws JsonProcessingException {
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setClassName(className);
        requestInfo.setMethodName(methodName);
        requestInfo.setParameters(parameters);
        requestInfo.setParameterTypes(parameterTypes);
        ObjectMapper mapper = new ObjectMapper();
        // 发送信息给服务端
        context.writeAndFlush(mapper.writeValueAsString(requestInfo));


    }
}
