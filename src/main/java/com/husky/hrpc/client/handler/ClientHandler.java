package com.husky.hrpc.client.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.husky.hrpc.common.RequestInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huskyui
 */
@Slf4j
public class ClientHandler extends SimpleChannelInboundHandler<String> {

    private ConcurrentHashMap<String, Object> resultAsyncMap = new ConcurrentHashMap<>(64);


    private ChannelHandlerContext context;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        log.debug("msg {}", msg);
        ObjectMapper objectMapper = new ObjectMapper();
        RequestInfo requestInfo = objectMapper.readerFor(RequestInfo.class).readValue(msg);
        Class resultType = requestInfo.getResultType();
        Object result = objectMapper.readerFor(resultType).readValue(requestInfo.getResult());
        resultAsyncMap.put(requestInfo.getRequestId(), result);
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

    /**
     * 返回requestId
     */
    public String sendMsg(String className, String methodName, Object[] parameters, Class[] parameterTypes, Class returnType) throws JsonProcessingException {
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setClassName(className);
        requestInfo.setMethodName(methodName);
        requestInfo.setParameters(parameters);
        requestInfo.setParameterTypes(parameterTypes);
        requestInfo.setRequestId(UUID.randomUUID().toString());
        requestInfo.setResultType(returnType);
        ObjectMapper mapper = new ObjectMapper();
        // 发送信息给服务端
        context.channel().eventLoop().execute(() -> {
            try {
                context.writeAndFlush(mapper.writeValueAsString(requestInfo));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        return requestInfo.getRequestId();
    }

    public Object getResult(String requestId) {
        // 异步结果map
        // fixme 这里写的不好
        while (!resultAsyncMap.containsKey(requestId)) {
        }
        return resultAsyncMap.get(requestId);
    }

}
