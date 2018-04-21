package com.tm.book_of_exercises.chat.customMsg;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tm.book_of_exercises.R;

import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;

/**
 * 作者：T M on 2018/4/13 23:23
 * 邮箱：xxx@163.com
 * desc新建一个消息类继承 IContainerItemProvider.MessageProvider 类，实现对应接口方法，
 * 1.注意开头的注解！
 * 2.注意泛型！
 */

@ProviderTag(
        messageContent = CxImgMessage.class,showReadState = true  //这里是你自定义的消息实体
)

public class CxImgItemProvider extends IContainerItemProvider.MessageProvider<CxImgMessage>{
    public CxImgItemProvider(){}

    @Override
    public void bindView(View view, int i, CxImgMessage cxImgMessage, UIMessage message) {
        //根据需求，适配数据
        ViewHolder holder = (ViewHolder) view.getTag();

        if (message.getMessageDirection() == Message.MessageDirection.SEND) {//消息方向，自己发送的
            //holder.message.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_right);
        } else {
            //holder.message.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_left);
        }
        //AndroidEmoji.ensure((Spannable) holder.message.getText());//显示消息中的 Emoji 表情。
        //holder.tvTitle.setText(redPackageMessage.getTitle());
        holder.tvStoreName.setText(CxImgMessage.getStoreName());
        //holder.tvDesc1.setText(redPackageMessage.getDesc1());
        //holder.tvDesc2.setText(redPackageMessage.getDesc2());
    }

    @Override
    public Spannable getContentSummary(CxImgMessage cxImgMessage) {
        return new SpannableString(CxImgMessage.getDesc1());
    }

    @Override
    public void onItemClick(View view, int i, CxImgMessage cxImgMessage, UIMessage uiMessage) {

    }

//    @Override
//    public void onItemLongClick(View view, int i, CxImgMessage cxImgMessage, UIMessage message) {
//        //实现长按删除等功能，咱们直接复制融云其他provider的实现
//        String[] items1;//复制，删除
//        items1 = new String[]{view.getContext().getResources().getString(io.rong.imkit.R.string.rc_dialog_item_message_copy), view.getContext().getResources().getString(io.rong.imkit.R.string.rc_dialog_item_message_delete)};
//
//        OptionsPopupDialog.newInstance(view.getContext(), items1).setOptionsPopupDialogListener(new OptionsPopupDialog.OnOptionsItemClickedListener() {
//            public void onOptionsItemClicked(int which) {
//                if (which == 0) {
//                    ClipboardManager clipboard = (ClipboardManager) view.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
//                    clipboard.setText(content.getContent());//这里是自定义消息的消息属性
//                } else if (which == 1) {
//                    RongIM.getInstance().deleteMessages(new int[]{message.getMessageId()}, (RongIMClient.ResultCallback) null);
//                }
//            }
//        }).show();
//
//    }

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        //这就是展示在会话界面的自定义的消息的布局
        View view = LayoutInflater.from(context).inflate(R.layout.custom_for_chat, null);
        ViewHolder holder = new ViewHolder();
        holder.tvTitle = (TextView) view.findViewById(R.id.tv_title);
        holder.tvStoreName = (TextView) view.findViewById(R.id.tv_store_name);
        holder.tvDesc1 = (TextView) view.findViewById(R.id.tv_desc1);
        holder.tvDesc2 = (TextView) view.findViewById(R.id.tv_desc2);
        view.setTag(holder);
        return view;
    }

    private static class ViewHolder {
        TextView tvTitle, tvStoreName, tvDesc1, tvDesc2;
    }
}
