package com.tm.book_of_exercises.tools;

import android.app.Activity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by T M on 2018/3/19.
 * 在工具类中，我们获取到了activity的Class，接着获得类中的所有字段，遍历字段，如果有ViewInject注解，
 * 则获取注解的中的值，通过获取并执行类中的方法（findViewById）来获得对应View的实例，最后把实例赋值给当前的字段，整个过程就完成了。
 * 当我们需要使用注解的时候，我们只需要在定义View的时候，在View的字段上添加对应的注解，把Id传入，然后在onCreate方法中调用上面这个工具类的方法即可。
 * 如UserInfo.class
 */

public class AnnotateUtils {
    public static void injectViews(Activity activity) {
        Class<? extends Activity> object = activity.getClass(); // 获取activity的Class
        Field[] fields = object.getDeclaredFields(); // 通过Class获取activity的所有字段
        for (Field field : fields) { // 遍历所有字段
            // 获取字段的注解，如果没有ViewInject注解，则返回null
            ViewInject viewInject = field.getAnnotation(ViewInject.class);
            if (viewInject != null) {
                int viewId = viewInject.value(); // 获取字段注解的参数，这就是我们传进去控件Id
                if (viewId != -1) {
                    try {
                        // 获取类中的findViewById方法，参数为int
                        Method method = object.getMethod("findViewById", int.class);
                        // 执行该方法，返回一个Object类型的View实例
                        Object resView = method.invoke(activity, viewId);
                        field.setAccessible(true);
                        // 把字段的值设置为该View的实例
                        field.set(activity, resView);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
