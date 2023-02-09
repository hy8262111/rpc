package com.proxy;

public class ConsumerMain {
    public static void main(String[] args) {
        URL url = new URL("127.0.0.1", 7777);
        RpcProviderProxy clientProxy = new RpcProviderProxy(url);

        //代理类
        DemoService proxy = clientProxy.getProxy(DemoService.class);
        System.out.println(proxy.sendAndGetMessage("测试发送信息"));
    }
}
