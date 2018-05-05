package com.tm.book_of_exercises.chat.plugin;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;

import io.rong.imkit.RongExtension;
import io.rong.imkit.plugin.IPluginModule;

/**
 * 作者：T M on 2018/4/22 11:55
 * 邮箱：xxx@163.com
 * 自定义 Plugin 并实现 IPluginModule
 */
public class CxImgMessagePlugin implements IPluginModule {
    @Override
    public Drawable obtainDrawable(Context context) {
        return null;
    }

    @Override
    public String obtainTitle(Context context) {
        return null;
    }

    @Override
    public void onClick(Fragment fragment, RongExtension rongExtension) {

    }

    @Override
    public void onActivityResult(int i, int i1, Intent intent) {

    }
}
