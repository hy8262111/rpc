package com.proxy;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;

public class RpcProviderHandler implements Runnable {
    private Socket clientSocket;
    private Map<String, Object> serviceMap;

    public RpcProviderHandler(Socket client, Map<String, Object> services) {
        this.clientSocket = client;
        this.serviceMap = services;
    }

    @Override
    public void run() {
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        RpcResponse response = new RpcResponse();
        try {
            ois = new ObjectInputStream(clientSocket.getInputStream());
            oos = new ObjectOutputStream(clientSocket.getOutputStream());

            //反序列化
            Object object = ois.readObject();
            Invocation invocation = invocation = (Invocation) object;


            //查找并执行服务
            Class<?> clazz = (Class<?>) serviceMap.get(invocation.getClassName());
            Method method = clazz.getMethod(invocation.getMethodName(), invocation.getParamTypes());
            Object result = method.invoke(clazz.newInstance(), invocation.getParams());

            response.setResult(result);
            oos.writeObject(response);
            oos.flush();

        } catch (Exception e) {
            if (oos != null) {
                response.setError(e);
                try {
                    oos.writeObject(response);
                    oos.flush();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        } finally {
            try {
                if (ois != null) ois.close();
                if (oos != null) oos.close();
                if (clientSocket != null) clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
