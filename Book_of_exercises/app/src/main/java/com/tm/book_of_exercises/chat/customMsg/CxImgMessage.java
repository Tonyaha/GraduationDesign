package com.tm.book_of_exercises.chat.customMsg;

import android.os.Parcel;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.common.ParcelUtils;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;

/**
 * 作者：T M on 2018/4/13 23:06
 * 邮箱：xxx@163.com
 */

/*
 * 注解名：MessageTag ；属性：value ，flag； value 即 ObjectName 是消息的唯一标识不可以重复，
 * 开发者命名时不能以 RC 开头，避免和融云内置消息冲突；flag 是用来定义消息的可操作状态。
 *如下面代码段，自定义消息名称 CustomizeMessage ，vaule 是 app:custom ，
 * flag 是 MessageTag.ISCOUNTED | MessageTag.ISPERSISTED 表示消息计数且存库。
 * app:RedPkgMsg: 这是自定义消息类型的名称，测试的时候用"app:RedPkgMsg"；
 * */
@MessageTag(value = "app:CImgMsg",flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED)
public class CxImgMessage extends MessageContent{
    //自定义属性
    private String title;
    private static String storeName;
    private static String desc1;
    private String desc2;

    public CxImgMessage(byte[] data) {
        String jsonStr = null;
        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            if (jsonObj.has("title"))
                setTitle(jsonObj.optString("title"));

            if (jsonObj.has("storeName"))
                setStoreName(jsonObj.optString("storeName"));

            if (jsonObj.has("desc1"))
                setDesc1(jsonObj.optString("desc1"));

            if (jsonObj.has("desc2"))
                setDesc2(jsonObj.optString("desc2"));

        } catch (JSONException e) {
            Log.d("JSONException", e.getMessage());
        }
    }

    /*
     * 实现 encode() 方法，该方法的功能是将消息属性封装成 json 串，
     * 再将 json 串转成 byte 数组，该方法会在发消息时调用，如下面示例代码：
     * */
    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("title", this.getTitle());
            jsonObj.put("storeName",this.getStoreName());
            jsonObj.put("desc1",this.getDesc1());
            jsonObj.put("desc2",this.getDesc2());

        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 给消息赋值
    public CxImgMessage(Parcel in){
        setTitle(ParcelUtils.readFromParcel(in));//该类为工具类，消息属性
        //这里可继续增加你消息的属性
        setStoreName(ParcelUtils.readFromParcel(in));//该类为工具类，消息属性
        setDesc1(ParcelUtils.readFromParcel(in));//该类为工具类，消息属性
        setDesc2(ParcelUtils.readFromParcel(in));//该类为工具类，消息属性
    }

    // 读取接口，目的是要从Parcel中构造一个实现了Parcelable的类的实例处理
    public static final Creator<CxImgMessage> CREATOR = new Creator<CxImgMessage>() {
        @Override
        public CxImgMessage createFromParcel(Parcel source) {
            return new CxImgMessage(source);
        }

        @Override
        public CxImgMessage[] newArray(int size) {
            return new CxImgMessage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * 将类的数据写入外部提供的 Parcel 中。
     * @param dest  对象被写入的 Parcel。
     * @param flags 对象如何被写入的附加标志。
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, getTitle());
        ParcelUtils.writeToParcel(dest, getStoreName());
        ParcelUtils.writeToParcel(dest, getDesc1());
        ParcelUtils.writeToParcel(dest, getDesc2());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public static String getDesc1() {
        return desc1;
    }

    public void setDesc1(String desc1) {
        this.desc1 = desc1;
    }

    public String getDesc2() {
        return desc2;
    }

    public void setDesc2(String desc2) {
        this.desc2 = desc2;
    }
}
