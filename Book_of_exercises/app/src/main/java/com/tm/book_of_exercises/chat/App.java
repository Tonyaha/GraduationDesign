package com.tm.book_of_exercises.chat;  //包名

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.tm.book_of_exercises.chat.customMsg.CxImgItemProvider;
import com.tm.book_of_exercises.chat.customMsg.CxImgMessage;
import com.tm.book_of_exercises.chat.customMsg.MyConversationClickListener;
import com.tm.book_of_exercises.chat.plugin.ExtensionModule;

import java.util.List;

import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.IExtensionModule;
import io.rong.imkit.RongExtensionManager;
import io.rong.imkit.RongIM;

/**
 * Created by T M on 2018/3/23.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIM 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) || "com.tm.book_of_exercises".equals(getCurProcessName(getApplicationContext()))) {

            /**
             * IMKit SDK调用第一步 初始化
             */
            RongIM.init(this);
            //注意，要在初始化之后注册
            RongIM.registerMessageType(CxImgMessage.class);
            RongIM.registerMessageTemplate(new CxImgItemProvider());

            /**
             * 设置会话界面操作的监听器。
             */
            RongIM.setConversationBehaviorListener(new MyConversationClickListener());

            // 发送消息监听
            RongIM.getInstance().setSendMessageListener(new MySendMessageListener());


            //在初始化之后，取消 SDK 默认的 ExtensionModule，注册自定义的 ExtensionModule，
            //setMyExtensionModule();

        }
    }

    public void setMyExtensionModule() {
        List<IExtensionModule> moduleList = RongExtensionManager.getInstance().getExtensionModules();
        IExtensionModule defaultModule = null;
        if (moduleList != null) {
            for (IExtensionModule module : moduleList) {
                if (module instanceof DefaultExtensionModule) {
                    defaultModule = module;
                    break;
                }
            }
            if (defaultModule != null) {
                RongExtensionManager.getInstance().unregisterExtensionModule(defaultModule);
                RongExtensionManager.getInstance().registerExtensionModule(new ExtensionModule());
            }
        }
    }

    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return 进程号
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }
}
