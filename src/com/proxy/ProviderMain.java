package com.proxy;

import java.util.HashMap;
import java.util.Map;

public class ProviderMain {
    public static void main(String[] args) {
        Map<String,Object> services = new HashMap<>();
        services.put(DemoService.class.getName(), DemoServiceImpl.class);

        RpcProvider server= new RpcProvider();
        server.start(7777,services);
    }
}

