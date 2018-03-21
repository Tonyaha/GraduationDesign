package com.tm.book_of_exercises;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.tm.book_of_exercises.constant.Constant;
import com.tm.book_of_exercises.http.RetrofitBuilder;
import com.tm.book_of_exercises.main.MyMainActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tm.book_of_exercises.constant.Constant.*;

public class MainActivity extends FragmentActivity{
    private FrameLayout frameLayout;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private MyMainActivity myMainActivity;
    private HashMap<String,Object> map = new HashMap<>();
    public static JSONObject jsonObject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frameLayout = findViewById(R.id.framLayout);

        Intent intent = getIntent();
        username = "tm";//intent.getStringExtra("username");

        myfragmentmanager();
        getFriendList();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            System.out.println("按下了back键   onKeyDown()");
            Intent intent = new Intent(this,StartAppActivity.class);
            intent.putExtra(StartAppActivity.EXIST,true);
            startActivity(intent);
            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }

    // 好友列表
    public void getFriendList(){
        Constant constant = new Constant();
        map.put("username", username);
        map.put("action","listOfFriend");
        RetrofitBuilder retrofitBuilder = new RetrofitBuilder(constant.BaseUrl + "/userInfo/");
        retrofitBuilder.isConnected(this);
        retrofitBuilder.params(map);
        retrofitBuilder.get();
        Call<ResponseBody> call = retrofitBuilder.getCall();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    //System.out.println("///////////" + response.body().string());
                    jsonObject = new JSONObject(response.body().string());
                    if("200".equals(jsonObject.getString("code"))){
                        JSONArray jsonArray = jsonObject.getJSONArray("list");
                        for(int i = 0; i<jsonArray.length();i++){
                            JSONObject job = jsonArray.getJSONObject(i);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                            //System.out.println(job.get("username")+"=//////////") ;  // 得到 每个对象中的属性值
                            String remark = job.getString("remark");
                            String nickname = job.getString("nickname");

                            //System.out.println("///////////" + remark + "     " + nickname);
                            //data.add(job.getString("remark"));
                            if("null".equals(remark)){  //换了很多种写法.......
                                data.add(nickname);
                            }else {
                                data.add(remark);
                            }
                        }
                    }
                    //System.out.println("///////////" + data);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //请求失败时回调
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Toast.makeText(MainActivity.this, getBaseContext().getResources().getText(R.string.connect_to_server), Toast.LENGTH_LONG).show();
                System.out.println(throwable.getMessage());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("执行 onDestroy()");
    }

    private void myfragmentmanager() {
        fragmentManager = this.getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();//获取一个事务， 该事务可以管理 fragment对象的添加、删除等操作

        /* 默认布局 */
        myMainActivity = new MyMainActivity();
        fragmentTransaction.add(R.id.framLayout,myMainActivity);
        fragmentTransaction.commit();
    }
}
