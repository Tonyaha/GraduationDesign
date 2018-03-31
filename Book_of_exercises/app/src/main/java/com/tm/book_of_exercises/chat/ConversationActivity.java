package com.tm.book_of_exercises.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.TextView;

import com.tm.book_of_exercises.MainActivity;
import com.tm.book_of_exercises.R;
import com.tm.book_of_exercises.tools.AnnotateUtils;
import com.tm.book_of_exercises.tools.ViewInject;

/**
 * Created by T M on 2018/3/24.
 */

public class ConversationActivity extends AppCompatActivity {

    @ViewInject(R.id.tv_friend)
    private TextView tv_bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation);
        AnnotateUtils.injectViews(this);

        /**
         * 启动单聊
         * context - 应用上下文。
         * targetUserId - 要与之聊天的用户 Id。
         * title - 聊天的标题，如果传入空值，则默认显示与之聊天的用户名称。
         */
        String sName = getIntent().getData().getQueryParameter("targetUserId");//获取昵称
        String title = getIntent().getData().getQueryParameter("title");
        tv_bar.setText(title);
    }
}
