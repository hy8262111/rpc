package com.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class RpcProviderProxy {
    private URL url;

    public RpcProviderProxy(URL url) {
        this.url = url;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("可以做一些执行方法的前置处理");
                Invocation invocation = new Invocation();
                invocation.setClassName(method.getDeclaringClass().getName());
                System.out.println(method.getDeclaringClass().getName());
                invocation.setMethodName(method.getName());
                invocation.setParamTypes(method.getParameterTypes());
                invocation.setParams(args);
                Object result = new RpcConsumer().execute(invocation, url.getHostname(), url.getPort());
                //功能增强，比如记录流水信息
                System.out.println("反射中的方法执行完毕，返回结果为：" + result);
                System.out.println("可以做一些执行方法的后置处理");
                return result;
            }
        });
    }

}
