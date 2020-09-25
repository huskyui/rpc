package com.husky.hrpc.client;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.husky.hrpc.client.future.RpcFuture;
import com.husky.hrpc.client.handler.ClientHandler;
import com.husky.hrpc.common.RequestInfo;
import com.husky.hrpc.common.config.ZkConfig;
import com.husky.hrpc.util.RandomUtil;
import com.sun.security.ntlm.Client;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huskyui
 */
@Slf4j
public class InvokerHandlerImpl implements InvocationHandler {
    private Class clazz;
    private ZkConfig zkConfig;

    private ClientHandler handler;

    public static ConcurrentHashMap<String, RpcFuture> resultAsyncMap = new ConcurrentHashMap<>(64);

    public InvokerHandlerImpl(Class clazz, ZkConfig zkConfig) {
        this.zkConfig = zkConfig;
        // 此处实现一个 zookeeper,服务治理
        ZkClient zkClient = new ZkClient(zkConfig.getHostAddress() + ":" + zkConfig.getPort());
        List<String> serverList = zkClient.getChildren("/rpc");
        if (serverList == null || serverList.isEmpty()) {
            throw new RuntimeException("服务器端无端点可访问，请联系管理员");
        }
        // 负载均衡，随机获取一个服务器
        String serverInfo = (String) RandomUtil.getRandomOne(serverList);
        log.info("serverList {} randomPort {}", serverList, serverInfo);
        String hostAddress = serverInfo.split("_")[0];
        Integer port = Integer.parseInt(serverInfo.split("_")[1]);
        // 此处会修改
        this.handler = new ClientHandler();
        log.info("invoker handler init success");
        this.clazz = clazz;
        initNettyClient(this.handler);
    }


    private void initNettyClient(ClientHandler clientHandler) {
        NettyClient nettyClient = new NettyClient(clientHandler);
        nettyClient.start();
        nettyClient.connectServer("localhost", 10243);
        nettyClient.connectServer("localhost", 10244);
        nettyClient.connectServer("localhost", 10245);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcFuture rpcFuture = sendMsg(clazz.getName(), method.getName(), args, method.getParameterTypes(), method.getReturnType());
        return rpcFuture.get();
    }

    /**
     * 返回requestId
     */
    public RpcFuture sendMsg(String className, String methodName, Object[] parameters, Class[] parameterTypes, Class returnType) throws JsonProcessingException {
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setClassName(className);
        requestInfo.setMethodName(methodName);
        requestInfo.setParameters(parameters);
        requestInfo.setParameterTypes(parameterTypes);
        requestInfo.setRequestId(UUID.randomUUID().toString());
        requestInfo.setResultType(returnType);
        ObjectMapper mapper = new ObjectMapper();
        RpcFuture rpcFuture = new RpcFuture();
        resultAsyncMap.put(requestInfo.getRequestId(), rpcFuture);
        Channel channel = (Channel) RandomUtil.getRandomOne(new ArrayList(NettyClient.serverList.values()));
        // send message
        log.info("channel active {}",channel.isActive());
        channel.writeAndFlush(mapper.writeValueAsString(requestInfo)).addListener(future -> {
           log.info("send message success :{}",future.isSuccess());
        });
        return rpcFuture;
    }
}
