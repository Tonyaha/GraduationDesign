package com.tm.book_of_exercises.chat;

import android.widget.Toast;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;

/**
 * 作者：T M on 2018/4/21 17:19
 * 邮箱：xxx@163.com
 */
public class MyReceiveMessageListener implements RongIMClient.OnReceiveMessageListener {
    /**
     * 收到消息的处理。
     *
     * @param message 收到的消息实体。
     * @param left    剩余未拉取消息数目。
     * @return 收到消息是否处理完成，true 表示自己处理铃声和后台通知，false 走融云默认处理方式。
     */
    @Override
    public boolean onReceived(Message message, int left) {
        Toast.makeText(new ConversationActivity().getContext(),"好友请求",Toast.LENGTH_LONG).show();
        return false;
    }
}
