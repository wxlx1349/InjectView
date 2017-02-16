package com.example.wang.injectview.annotation;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by wangxi on 2017/2/15.
 * <p>
 * description :
 * version code: 1
 * create time : 2017/2/15
 * update time : 2017/2/15
 * last modify : wangxi
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@EventBase(listenerType = View.OnLongClickListener.class,
        listenerSetter = "setOnLongClickListener", methodName = "onLongClick")
public @interface OnLongClick {
    int[] value();
}
