package com.husky.hrpc.client.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.husky.hrpc.client.InvokerHandlerImpl;
import com.husky.hrpc.client.future.RpcFuture;
import com.husky.hrpc.common.RequestInfo;
import io.netty.channel.*;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huskyui
 */
@Slf4j
@ChannelHandler.Sharable
public class ClientHandler extends SimpleChannelInboundHandler<String> {

    private EventLoopGroup eventLoopGroup;




    private ChannelHandlerContext context;

    private Channel channel;

    public void setEventLoopGroup(EventLoopGroup eventLoopGroup) {
        this.eventLoopGroup = eventLoopGroup;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        log.debug("msg {}", msg);
        ObjectMapper objectMapper = new ObjectMapper();
        RequestInfo requestInfo = objectMapper.readerFor(RequestInfo.class).readValue(msg);
        Class resultType = requestInfo.getResultType();
        Object result = objectMapper.readerFor(resultType).readValue(requestInfo.getResult());
        RpcFuture lock = InvokerHandlerImpl.resultAsyncMap.remove(requestInfo.getRequestId());
        lock.success(result);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("channel active");
        this.context = ctx;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("channel inactive");
    }




}
