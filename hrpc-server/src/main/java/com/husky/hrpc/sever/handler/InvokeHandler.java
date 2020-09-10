package com.husky.hrpc.sever.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.husky.hrpc.common.RequestInfo;
import com.husky.hrpc.sever.MessageHandlerHolder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.concurrent.*;


/**
 * @author huskyui
 */
@Slf4j
public class InvokeHandler extends SimpleChannelInboundHandler<String> {
    ExecutorService executorService;

    public InvokeHandler() {
        ThreadFactory namedThreadFactory = new DefaultThreadFactory("hrpc");
        this.executorService = new ThreadPoolExecutor(5,200,0L, TimeUnit.SECONDS,new LinkedBlockingQueue<>(1024),namedThreadFactory,new ThreadPoolExecutor.AbortPolicy());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        this.executorService.execute(()->{
            try {
                this.handleRequest(ctx,msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void handleRequest(ChannelHandlerContext ctx, String msg) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        RequestInfo requestInfo = mapper.readerFor(RequestInfo.class).readValue(msg);
        log.info("requestInfo {}",requestInfo);
        Object object = MessageHandlerHolder.get(requestInfo.getClassName());
        Method method = object.getClass().getMethod(requestInfo.getMethodName(),requestInfo.getParameterTypes());
        method.setAccessible(true);
        Object result = method.invoke(object,requestInfo.getParameters());
        ctx.writeAndFlush(mapper.writeValueAsString(result));
    }



}
