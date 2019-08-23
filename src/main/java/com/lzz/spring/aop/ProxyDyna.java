package com.lzz.spring.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyDyna implements InvocationHandler {

    Method before;
    Method after;
    Object target;
    Object aspect;
    String targetMethod;

    public Object createProxybean(Method before, Method after, Object target,
                                  Object aspect, String targetMethod) throws IllegalAccessException, InstantiationException {
        this.before = before;
        this.after = after;
        this.target = target;
        this.targetMethod = targetMethod;
        this.aspect = aspect;
        return Proxy.newProxyInstance(this.getClass().getClassLoader(),
                target.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object res = null;
        if (!method.getName().equals(targetMethod)){
            return method.invoke(target, args);
        }
        if (before != null)
            before.invoke(aspect);
        res = method.invoke(this.target, args);
        if (after != null){
            after.invoke(aspect);
        }
        return res;
    }
}
