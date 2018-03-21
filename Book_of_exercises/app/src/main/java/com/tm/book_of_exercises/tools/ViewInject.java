package com.tm.book_of_exercises.tools;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by T M on 2018/3/19.
 * 注解，我们看到的最多的注解获取是@Override，当我们重写父类中的方法的时候，这个注解会被编译器自动生成出来，这个注解只是表明方法是重写了父类方法。
 * 那么我们究竟该如何自定义我们想要的注解呢？其实很简单，直接看代码：
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface  ViewInject{
    int value();
}
