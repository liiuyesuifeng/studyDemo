package com.demo.domain.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 代理对象
 *
 * @author lhr
 */
public class DynamicProxyHandle implements InvocationHandler {
    private Object proxyid;

    public DynamicProxyHandle(Object proxyid) {
        this.proxyid = proxyid;
    }

    /**
     * @param proxy  代理对象
     * @param method 方法
     * @param args   参数
     * @return
     * @throws Throwable
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("********** proxy:" + proxy.getClass() + " ,method:" + method + ", args:" + args);
        return method.invoke(proxyid, args);
    }

    public Object getProxyid() {
        return proxyid;
    }

    public void setProxyid(Object proxyid) {
        this.proxyid = proxyid;
    }
}
