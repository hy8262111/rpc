package com.proxy;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class RpcConsumer {
    public Object execute(Invocation invocation, String host, int port) throws Throwable {
        Socket server = new Socket(host, port);
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        try{
            //将请求写到连接服务端socket的输出流
            oos = new ObjectOutputStream(server.getOutputStream());
            oos.writeObject(invocation);
            oos.flush();

            //读取输入流的内容
            ois = new ObjectInputStream(server.getInputStream());
            Object res = ois.readObject();
            RpcResponse response = (RpcResponse) res;

            return response.getResult();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (ois!=null)ois.close();
            if (oos != null) oos.close();
            if (server != null) server.close();
        }
    }
}
