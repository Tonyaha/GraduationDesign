package com.tm.book_of_exercises.chat;


import android.content.Context;

import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

/**
 * 使用融云推送功能前必须在“融云开发者后台 - 应用标识”中设置了 Android 应用包名，否则无法接收推送消息。
 * 注册到应用的 AndroidManifest.xml 里面
 */
/**
 * 作者：T M on 2018/4/13 22:56
 * 邮箱：xxx@163.com
 */
public class BroadcastReceiver extends PushMessageReceiver {
    @Override
    public boolean onNotificationMessageArrived(Context context, PushNotificationMessage pushNotificationMessage) {
        return false;
    }

    @Override
    public boolean onNotificationMessageClicked(Context context, PushNotificationMessage pushNotificationMessage) {
        return false;
    }
}
