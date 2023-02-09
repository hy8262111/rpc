package com.proxy;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RpcProvider {
    public void start(int port, Map<String, Object> services){
        ServerSocket server = null;
        try {
            Executor executor = new ThreadPoolExecutor(5, 10, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>() {
            });
            while(true){
                //4、获取客户端连接
                Socket client = server.accept();
                //5、将服务端被调用的服务放到线程池中异步执行
                RpcProviderHandler service = new RpcProviderHandler(client,services);
                executor.execute(service);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (server!=null){
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
