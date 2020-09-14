package com.husky.hrpc.sever.service.impl;

import com.husky.hrpc.common.service.HelloService;

/**
 * @author huskyui
 */

public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String s) {
        return "hi," + s + "! this is hrpc";
    }
}
