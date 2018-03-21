package com.tm.book_of_exercises.main.http;
import com.tm.book_of_exercises.constant.Constant;
import com.tm.book_of_exercises.main.request_interface.RequestInterface;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.Map;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by T M on 2018/3/12.
 * 接收服务器返回数据 的类
 */

public class Reception {
    //    Map<String, Object> map = new HashMap<>();
    Constant constant = new Constant();

    private Reception(Map<String, Object> map, String str) {

    }

//    //步骤4:创建Retrofit对象
//    Retrofit retrofit = new Retrofit.Builder()
//            .baseUrl(constant.BaseUrl + urlPath) // 设置 网络请求 Url
//            .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析(记得加入依赖)
//            .build();
//
//    // 步骤5:创建 网络请求接口 的实例
//    RequestInterface request = retrofit.create(RequestInterface.class);
//
//    //对 发送请求 进行封装(设置需要翻译的内容)
//    Call<ResponseBody> call = request.getCall(map);
//
//
//
//    //步骤6:发送网络请求(异步)
//    call.enqueue(new Callback<ResponseBody>(){
//        @Override
//        public void onResponse (Call < ResponseBody > call, Response < ResponseBody > response){
//        // 请求处理,输出结果
//        // 输出翻译的内容
//        try {
//            //System.out.println("///////"+response.body().string());
//            String resultCode = "";
//            String msg = "";
//            JSONObject jsonObject = null;
//            try {
//                jsonObject = new JSONObject(response.body().string());
//                resultCode = jsonObject.getString("code");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//        //请求失败时回调
//        @Override
//        public void onFailure (Call < ResponseBody > call, Throwable throwable){
//        //Toast.makeText(, getBaseContext().getResources().getText(R.string.connection_timeout), Toast.LENGTH_LONG).show();
//        System.out.println(throwable.getMessage());
//    }
//    });

}
