package com.proxy;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

public class DemoServiceImpl implements DemoService {
    Logger log = LoggerFactory.getLogger(DemoServiceImpl.class);

    @Override
    public String sendAndGetMessage(String message) {
        System.out.println("执行了客户端的方法，参数为：" + message);
        return message;
    }

}
