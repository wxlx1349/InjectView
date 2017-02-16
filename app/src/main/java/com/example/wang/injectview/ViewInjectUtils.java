package com.example.wang.injectview;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.example.wang.injectview.annotation.ContentView;
import com.example.wang.injectview.annotation.DynamicHandler;
import com.example.wang.injectview.annotation.EventBase;
import com.example.wang.injectview.annotation.ViewInject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by wangxi on 2017/2/15.
 * <p>
 * description :
 * version code: 1
 * create time : 2017/2/15
 * update time : 2017/2/15
 * last modify : wangxi
 */

public class ViewInjectUtils {
    private static final String METHOD_SET_CONTENTVIEW = "setContentView";
    private static final String METHOD_FIND_VIEW_BY_ID = "findViewById";

    public static void injectContentView(Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();
        //查询类上是否存在注解
        ContentView contentView = clazz.getAnnotation(ContentView.class);
        if (contentView != null) {
            int contentViewLayoutId = contentView.value();
            try {
                activity.setContentView(contentViewLayoutId);
//                Method method = clazz.getMethod(METHOD_SET_CONTENTVIEW, int.class);
//                method.setAccessible(true);
//                method.invoke(activity, contentViewLayoutId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        injectViews(activity);
        injectEvents(activity);
    }

    private static void injectViews(Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            ViewInject viewInjectAnnotation = field.getAnnotation(ViewInject.class);
            if (viewInjectAnnotation != null) {
                int viewId = viewInjectAnnotation.value();
                if (viewId != -1) {
                    try {
                        Method method = clazz.getMethod(METHOD_FIND_VIEW_BY_ID, int.class);
                        method.setAccessible(true);
                        Object resView = method.invoke(activity, viewId);
                        field.setAccessible(true);
                        field.set(activity, resView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static void injectEvents(Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                Class<? extends Annotation> annotationType = annotation.annotationType();
                //拿到注解上的注解
                EventBase eventBaseAnnotation = annotationType.getAnnotation(EventBase.class);
                if (eventBaseAnnotation != null) {
                    //拿到参数
                    String listenerSetter = eventBaseAnnotation.listenerSetter();
                    String methodName = eventBaseAnnotation.methodName();
                    Class<?> listenerType = eventBaseAnnotation.listenerType();
                    //通过动态代理，设置监听方法
                    DynamicHandler dynamicHandler = new DynamicHandler(activity);
                    dynamicHandler.addMethod(methodName, method);
                    Object listener = Proxy.newProxyInstance(listenerType.getClassLoader(), new Class[]{listenerType}, dynamicHandler);
                    Log.e("tag2", "setListener 成功0");
                    try {
                        //拿到view的id
                        Method aMethod = annotationType.getDeclaredMethod("value");
                        int[] viewIds = (int[]) aMethod.invoke(annotation);

                        for (int viewId : viewIds) {
                            View view = activity.findViewById(viewId);
                            if (view != null) {
                                Method setEventListenerMethod = view.getClass().getMethod(listenerSetter, listenerType);
                                setEventListenerMethod.invoke(view, listener);
                                Log.e("tag2", "setListener 成功");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

}
