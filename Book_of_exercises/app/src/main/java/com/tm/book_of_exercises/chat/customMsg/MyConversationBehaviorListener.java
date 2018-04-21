package com.tm.book_of_exercises.chat.customMsg;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.tm.book_of_exercises.constant.Constant;
import com.tm.book_of_exercises.main.otherPage.SearchActivity;

import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.RichContentMessage;
import io.rong.message.TextMessage;

import static com.tm.book_of_exercises.constant.Constant.username;

/**
 * 作者：T M on 2018/4/21 14:45
 * 邮箱：xxx@163.com
 * 实现会话界面操作的监听接口 ConversationBehaviorListener 。会话界面中点击用户头像、长按用户头像、点击消息、长按消息的操作都在此处理。
 */
public class MyConversationBehaviorListener implements RongIM.ConversationBehaviorListener{

    /**
     * 当点击用户头像后执行。
     *
     * @param context           上下文。
     * @param conversationType  会话类型。
     * @param userInfo          被点击的用户的信息。
     * @return 如果用户自己处理了点击后的逻辑，则返回 true，否则返回 false，false 走融云默认处理方式。
     */
    @Override
    public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        Intent intent = new Intent(context,SearchActivity.class);
        intent.putExtra("searchName",userInfo.getUserId());
        context.startActivity(intent);
        return false;
    }

    /**
     * 当长按用户头像后执行。
     *
     * @param context          上下文。
     * @param conversationType 会话类型。
     * @param userInfo         被点击的用户的信息。
     * @return 如果用户自己处理了点击后的逻辑，则返回 true，否则返回 false，false 走融云默认处理方式。
     */
    @Override
    public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        return false;
    }

    /**
     * 当点击消息时执行。
     *
     * @param context 上下文。
     * @param view    触发点击的 View。
     * @param message 被点击的消息的实体信息。
     * @return 如果用户自己处理了点击后的逻辑，则返回 true， 否则返回 false, false 走融云默认处理方式。
     */
    @Override
    public boolean onMessageClick(Context context, View view, Message message) {
        MessageContent messageContent = message.getContent();
        //if(!"SEND".equals(message.getMessageDirection().toString()) && "RC:ImgTextMsg".equals(message.getObjectName())){
        if(messageContent instanceof RichContentMessage){ //图文消息
            RichContentMessage richContentMessage = (RichContentMessage) messageContent;
            //Log.e("aaaaaaaaa",richContentMessage.getTitle());
            if(!"SEND".equals(message.getMessageDirection().toString()) && "*  添加好友 *".equals(richContentMessage.getTitle())) {
                newFriend(context, message);
            }
            return true;
        }else if("SEND".equals(message.getMessageDirection().toString()) && "RC:ImgTextMsg".equals(message.getObjectName())){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 当长按消息时执行。
     *
     * @param context 上下文。
     * @param view    触发点击的 View。
     * @param message 被长按的消息的实体信息。
     * @return 如果用户自己处理了长按后的逻辑，则返回 true，否则返回 false，false 走融云默认处理方式。
     */
    @Override
    public boolean onMessageLongClick(Context context, View view, Message message) {
        return false;
    }
    /**
     * 当点击链接消息时执行。
     *
     * @param context 上下文。
     * @param link    被点击的链接。
     * @return 如果用户自己处理了点击后的逻辑处理，则返回 true， 否则返回 false, false 走融云默认处理方式。
     */
    @Override
    public boolean onMessageLinkClick(Context context, String link) {
        return false;
    }

    private void newFriend(Context context,Message message){
//        Log.e("tttttt","1:" + message.getExtra());
//        Log.e("tttttt","2:" + message.getReceivedStatus().isRetrieved());
//        Log.e("tttttt","3:" + message.getConversationType());
//        Log.e("tttttt","4:" + message.getObjectName());
//        Log.e("tttttt","5:" + message.getSenderUserId());
//        Log.e("tttttt","6:" + message.getTargetId());
//        Log.e("tttttt","7:" + message.getUId());
//        Log.e("tttttt","8:" + message.getContent().toString());
//        Log.e("tttttt","9:" + message.getMessageDirection());
//        Log.e("tttttt","10:" + message.getMessageId());
//        Log.e("tttttt","11:" + message.getSentStatus());
//        Log.e("tttttt","12:" + message.getReadReceiptInfo().toJSON());
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("是否同意加为好友？")
                .setCancelable(true);
        alertDialog.setPositiveButton("同意", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Constant constant = new Constant();
                constant.addFriend(context,username,message.getSenderUserId());
                dialogInterface.cancel();
            }
        });
        alertDialog.setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                TextMessage myTextMessage = TextMessage.obtain(username + ":拒绝添加你为好友");
                Message myMessage = Message.obtain(message.getSenderUserId(),Conversation.ConversationType.PRIVATE,myTextMessage);
                RongIM.getInstance().sendMessage(myMessage,"请求被拒绝",null, new IRongCallback.ISendMessageCallback() {
                    @Override
                    public void onAttached(Message message) {

                    }

                    @Override
                    public void onSuccess(Message message) {

                    }

                    @Override
                    public void onError(Message message, RongIMClient.ErrorCode errorCode) {

                    }
                });
                dialogInterface.cancel();
            }
        });
        alertDialog.show();
    }
}
