package com.husky.hrpc.client;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.husky.hrpc.client.future.RpcFuture;
import com.husky.hrpc.client.handler.ClientHandler;
import com.husky.hrpc.common.RequestInfo;
import com.husky.hrpc.common.config.ZkConfig;
import com.husky.hrpc.common.container.ServerContainer;
import com.husky.hrpc.util.RandomUtil;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

/**
 * @author huskyui
 */
@Slf4j
public class InvokerHandlerImpl implements InvocationHandler {
    private Class clazz;
    private ZkConfig zkConfig;

    private List<String> serverPathList;

    private ClientHandler handler;

    private NettyClient nettyClient;

    public static ConcurrentHashMap<String, RpcFuture> resultAsyncMap = new ConcurrentHashMap<>(64);

    public InvokerHandlerImpl(Class clazz, ZkConfig zkConfig) {
        this.zkConfig = zkConfig;
        // 此处会修改
        this.handler = new ClientHandler();
        log.info("invoker handler init success");
        this.clazz = clazz;
        initNettyClient(this.handler);
    }


    private void initNettyClient(ClientHandler clientHandler) {
        NettyClient nettyClient = new NettyClient(clientHandler);
        nettyClient.start();
        this.nettyClient = nettyClient;
        ZkClient zkClient = new ZkClient(zkConfig.getHostAddress(), zkConfig.getPort());
        this.serverPathList = zkClient.subscribeChildChanges("/rpc", new IZkChildListener() {
            @Override
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                log.info("发送变化parentPath {} changedList {}", parentPath, currentChilds);
                serverPathList = currentChilds;
            }
        });
        log.info("初始化serverPathList111 {}", serverPathList);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcFuture rpcFuture = sendMsg(clazz.getName(), method.getName(), args, method.getParameterTypes(), method.getReturnType());
        return rpcFuture.get();
    }

    /**
     * 这个方法可能会有问题，由于zookeeper的心跳有时延
     * fixme 测试zookeeper的时延
     *
     * @return channel
     */
    public Channel getRandomChannel() throws ExecutionException, InterruptedException {
        String serverInfo = (String) RandomUtil.getRandomOne(serverPathList);
        Channel channel = ServerContainer.getChannel(serverInfo);

        String hostAddress = serverInfo.split("_")[0];
        Integer port = Integer.parseInt(serverInfo.split("_")[1]);
        // 如果container获取到的数据不存在，就连接
        if (channel == null) {
            // 设置默认值
            channel = nettyClient.connectServer(hostAddress, port);
            ServerContainer.add(serverInfo, channel);
        }
        // if the channel is inactive ,so connected
        if (!channel.isActive()) {
            ServerContainer.remove(serverInfo);
            channel = nettyClient.connectServer(hostAddress, port);
            ServerContainer.add(serverInfo, channel);
        }
        // if the channel is active,so connected.
        if (channel.isActive()) {
            return channel;
        }
        throw new RuntimeException("channel 出现异常");
    }

    /**
     * 返回requestId
     */
    public RpcFuture sendMsg(String className, String methodName, Object[] parameters, Class[] parameterTypes, Class returnType) throws JsonProcessingException, ExecutionException, InterruptedException {
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
        Channel channel = getRandomChannel();
//        channel.writeAndFlush(mapper.writeValueAsString(requestInfo)).addListener(future -> {
//            log.info("send message success :{}", future.isSuccess());
//        });
        channel.eventLoop().execute(() -> {
            try {
                String message = mapper.writeValueAsString(requestInfo);
                channel.writeAndFlush(message).addListener(future -> {
                    log.info("send message {} success :{}", message, future.isSuccess());
                });
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        return rpcFuture;
    }
}
