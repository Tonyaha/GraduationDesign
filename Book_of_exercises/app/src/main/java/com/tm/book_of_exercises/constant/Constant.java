package com.tm.book_of_exercises.constant;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.tm.book_of_exercises.http.RetrofitBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by T M on 2018/2/9.
 */
public class Constant{
    public static final int CODE_SELECT_IMAGE = 2;//相册RequestCode
    public static final int TYPE_TAKE_PHOTO = 1;//Uri获取类型判断
    public static final int CODE_TAKE_PHOTO = 1;//相机RequestCode
    public final String BaseUrl = "http://192.168.137.1:8000";//""http://140.143.95.232:8000";//http://192.168.137.1:8081";
    public static String username= "";
    public static Uri uri_admin = Uri.parse("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2619911597,2242133565&fm=27&gp=0.jpg");


    private HashMap<String, Object> map = new HashMap<>();
    private Context context;
    public Constant(){}
    public Constant(Context context){
        this.context = context;
    }
    // 关注按钮
    public void follow(int id,String follow,String msg) {
        Constant constant = new Constant();
        map.put("username", Constant.username);
        map.put("action", "modifyCollect");
        map.put("isFollow",follow);
        map.put("taskId",id);
        map.put("msg",msg);
        RetrofitBuilder retrofitBuilder = new RetrofitBuilder(constant.BaseUrl + "/api/userCollect/");
        retrofitBuilder.isConnected(context);
        retrofitBuilder.params(map);
        retrofitBuilder.post();
        Call<ResponseBody> call = retrofitBuilder.getCall();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    //System.out.println("//////"+response.body().string());
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    if("200".equals(jsonObject.getString("code"))){
                        Toast.makeText(context,"change success ",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(context,"change fail ",Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void addFriend(Context context ,String userId,String targetId){
        map.put("username",userId);
        map.put("targetname",targetId);
        RetrofitBuilder builder = new RetrofitBuilder(BaseUrl + "/api/newFriend/");
        builder.isConnected(context);
        builder.params(map);
        builder.post();
        Call<ResponseBody> call = builder.getCall();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}

