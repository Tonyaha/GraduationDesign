package com.tm.book_of_exercises.chat.plugin;

import java.util.List;

import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.model.Conversation;

/**
 * 作者：T M on 2018/4/22 11:56
 * 邮箱：xxx@163.com
 * 自定义一个 ExtensionModule 继承自 DefaultExtensionModule，复写其中的 getPluginModules() 方法，返回需要展示的 plugin 列表。
 */
public class ExtensionModule extends DefaultExtensionModule {
    private CxImgMessagePlugin cxImgMessagePlugin;
    @Override
    public List<IPluginModule> getPluginModules(Conversation.ConversationType conversationType) {
        List<IPluginModule> pluginModules =  super.getPluginModules(conversationType);
        pluginModules.add(cxImgMessagePlugin);
        return pluginModules;
    }
}
