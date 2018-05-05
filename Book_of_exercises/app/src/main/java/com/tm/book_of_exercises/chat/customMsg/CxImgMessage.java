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
    private String strData;

    public CxImgMessage(){}

    public static CxImgMessage obtain(String str){
        CxImgMessage cxImgMessage = new CxImgMessage();
        cxImgMessage.strData = str;
        return cxImgMessage;
    }

    //覆盖父类的 MessageContent(byte[] data) 构造方法，该方法将对收到的消息进行解析，先由 byte 转成 json 字符串，再将 json 中内容取出赋值给消息属性
    public CxImgMessage(byte[] data) {
        String jsonStr = null;
        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            if (jsonObj.has("content")){  //content 是encode（）里面的
                setStrData(jsonObj.optString("content"));
            }

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
            jsonObj.put("content", this.getStrData());

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
        setStrData(ParcelUtils.readFromParcel(in));//该类为工具类，消息属性
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
        ParcelUtils.writeToParcel(dest, getStrData());
    }

    public String getStrData() {
        return strData;
    }

    public void setStrData(String strData) {
        this.strData = strData;
    }

}
