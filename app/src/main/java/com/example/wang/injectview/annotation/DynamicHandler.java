package com.example.wang.injectview.annotation;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by wangxi on 2017/2/16.
 * <p>
 * description :
 * version code: 1
 * create time : 2017/2/16
 * update time : 2017/2/16
 * last modify : wangxi
 */

public class DynamicHandler implements InvocationHandler {
    private WeakReference<Object> handlerRef;
    private final HashMap<String, Method> methodMap = new HashMap<>();

    public DynamicHandler(Object handler) {
        this.handlerRef = new WeakReference<Object>(handler);
    }

    public void addMethod(String name, Method method) {
        methodMap.put(name, method);
    }

    public Object getHandler() {
        return handlerRef.get();
    }

    public void setHandler(Object handler) {
        this.handlerRef = new WeakReference<Object>(handler);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object handler = handlerRef.get();
        if (handler != null) {
            String methodName = method.getName();
            method = methodMap.get(methodName);
            if (method != null) {
                return method.invoke(handler, args);
            }
        }
        return null;
    }
}
