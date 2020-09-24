package com.husky.hrpc.client;


import com.husky.hrpc.client.future.RpcFuture;
import com.husky.hrpc.client.handler.ClientHandler;
import com.husky.hrpc.common.config.ZkConfig;
import com.husky.hrpc.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author huskyui
 */
@Slf4j
public class InvokerHandlerImpl implements InvocationHandler {
    private Class clazz;
    private ZkConfig zkConfig;

    private ClientHandler handler;

    public InvokerHandlerImpl(Class clazz, ZkConfig zkConfig) {
        this.zkConfig = zkConfig;
        // 此处实现一个 zookeeper,服务治理
        ZkClient zkClient = new ZkClient(zkConfig.getHostAddress() + ":" + zkConfig.getPort());
        List<String> serverList = zkClient.getChildren("/rpc");
        if (serverList == null || serverList.isEmpty()) {
            throw new RuntimeException("服务器端无端点可访问，请联系管理员");
        }
        // 获取
        String serverInfo = (String) RandomUtil.getRandomOne(serverList);

        log.info("serverList {} randomPort {}", serverList, serverInfo);

        String hostAddress = serverInfo.split("_")[0];
        Integer port = Integer.parseInt(serverInfo.split("_")[1]);

        // 此处会修改
        this.handler = new ClientHandler();
        log.info("invoker handler init success");
        this.clazz = clazz;
        initNettyClient(hostAddress, port, this.handler);
    }


    private void initNettyClient(String hostAddress, Integer port, ClientHandler clientHandler) {
        NettyClient nettyClient = new NettyClient(hostAddress, port, clientHandler);
        nettyClient.start();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcFuture rpcFuture = handler.sendMsg(clazz.getName(), method.getName(), args, method.getParameterTypes(), method.getReturnType());
        return rpcFuture.get();
    }
}
