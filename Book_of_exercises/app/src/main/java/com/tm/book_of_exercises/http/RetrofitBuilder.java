package com.tm.book_of_exercises.http;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.tm.book_of_exercises.R;
import com.tm.book_of_exercises.http.utils.NetUtils;
import java.util.HashMap;
import java.util.Map;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by T M on 2018/3/16.
 */

public class RetrofitBuilder {
    Map<String, Object> map = new HashMap<>();
    String url;
    Context mContext;
    boolean checkNetConnected = false;
    Call<ResponseBody> call = null;

    public RetrofitBuilder(String url) {
        this.url = url;
    }

    public RetrofitBuilder params(@NonNull Map<String, Object> params) {
        this.map.putAll(params);
        return this;
    }

    public RetrofitBuilder params(@NonNull String key, String value) {
        this.map.put(key, value);
        return this;
    }

    /**
     * 检查网络是否连接，未连接跳转到网络设置界面
     */
    public RetrofitBuilder isConnected(@NonNull Context context) {
        checkNetConnected = true;
        mContext = context;
        return this;
    }

//    @CheckResult
//    private String checkUrl(String url) {
//        if (HttpUtil.checkNULL(url)) {
//            throw new NullPointerException("absolute url can not be empty");
//        }
//        return url;
//    }
//
//    @CheckResult
//    public String message(String mes) {
//        if (HttpUtil.checkNULL(mes)) {
//            mes = "服务器异常，请稍后再试";
//        }
//
//        if (mes.equals("timeout") || mes.equals("SSL handshake timed out")) {
//            return "网络请求超时";
//        } else {
//            return mes;
//        }
//
//    }

    /**
     * 请求前初始检查
     */
    boolean allready() {
        if (!checkNetConnected || mContext == null) {
            return true;
        }
        if (!NetUtils.isConnected(mContext)) {
            Toast.makeText(mContext, mContext.getResources().getString(R.string.internet_closed), Toast.LENGTH_SHORT).show();
            NetUtils.openSetting(mContext);//跳转到网络设置界面
            return false;
        }
        return true;
    }

    public void post(){
        if(!allready()){
            return;
        }
        //        //步骤4:创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url) // 设置 网络请求 Url
                .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析(记得加入依赖)
                .build();

        // 步骤5:创建 网络请求接口 的实例
        RetrofitHttpService request = retrofit.create(RetrofitHttpService.class);

        //对 发送请求 进行封装(设置需要翻译的内容)
        Call<ResponseBody> call = request.post(map);

        setCall(call);

    }

    public void get(){
        if(!allready()){
            return;
        }
        //        //步骤4:创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url) // 设置 网络请求 Url
                .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析(记得加入依赖)
                .build();

        // 步骤5:创建 网络请求接口 的实例
        RetrofitHttpService request = retrofit.create(RetrofitHttpService.class);

        //对 发送请求 进行封装(设置需要翻译的内容)
        Call<ResponseBody> call = request.get(map);

        setCall(call);
    }

    public Call<ResponseBody> getCall() {
        return call;
    }

    public void setCall(Call<ResponseBody> call) {
        this.call = call;
    }
}
