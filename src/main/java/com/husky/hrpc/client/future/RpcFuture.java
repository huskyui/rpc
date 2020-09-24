package com.husky.hrpc.client.future;

import java.util.concurrent.*;

/**
 * 此方法用于获取异步消息使用，主要使用是使用一个map : uuid future,future里面用countdownLatch来限制
 * @author huskyui
 */

public class RpcFuture implements Future {

    private Object result ;
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        countDownLatch.await();
        return result;
    }

    public void success(Object result){
        this.result = result;
        countDownLatch.countDown();
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }
}
