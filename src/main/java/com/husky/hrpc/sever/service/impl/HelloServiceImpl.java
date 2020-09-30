package com.husky.hrpc.sever.service.impl;

import com.husky.hrpc.common.service.HelloService;

import java.util.concurrent.TimeUnit;

/**
 * @author huskyui
 */

public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String s) {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "hi," + s + "! this is hrpc";
    }
}
