package com.tm.book_of_exercises.main.otherPage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tm.book_of_exercises.R;
import com.tm.book_of_exercises.adapter.CollectAdapter;
import com.tm.book_of_exercises.constant.Constant;
import com.tm.book_of_exercises.http.RetrofitBuilder;
import com.tm.book_of_exercises.tools.AnnotateUtils;
import com.tm.book_of_exercises.tools.ViewInject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by T M on 2018/3/28.
 */

public class TasksActivity extends AppCompatActivity {
    @ViewInject(R.id.tasksRecyclerView)
    private RecyclerView mRecyclerView;
    public CollectAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<JSONObject> data = new ArrayList<>();
    private HashMap<String, Object> map = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tasksRequest();
        initData();  // 初始化在前面，不然提示 E/RecyclerView: No adapter attached; skipping layout
        setContentView(R.layout.activity_tasks);

        AnnotateUtils.injectViews(this);
        //mRecyclerView = findViewById(R.id.tasksRecyclerView);
        mRecyclerView.setLayoutManager(mLayoutManager); // 设置布局管理器
        mRecyclerView.setAdapter(mAdapter); // 设置adapter
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());  // 设置Item添加和移除的动画
    }

    private void tasksRequest() {
        Constant constant = new Constant();
        RetrofitBuilder builder = new RetrofitBuilder(constant.BaseUrl + "/api/userCollect/");
        builder.isConnected(this);
        map.put("username", Constant.username);
        map.put("action", "allTasks");
        builder.params(map);
        builder.get();
        Call<ResponseBody> call = builder.getCall();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    //System.out.println("////////////" + response.body().string());
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String code = jsonObject.getString("code");
                    if ("200".equals(code)) {
                        try {
                            JSONArray jsonArray = jsonObject.getJSONArray("list");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jb = jsonArray.getJSONObject(i);
                                data.add(jb);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if ("404".equals(code)) {
                        Log.e("CollectFragment", jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void initData() {
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAdapter = new CollectAdapter(data, R.layout.item_all_tasks, R.id.all_task_tv, R.id.all_tasks_img, R.id.all_tasks_follow, R.id.all_tasks_label, "all");
        mAdapter.mySetOnClickListener(new CollectAdapter.MyOnClickListener() {
            @Override
            public void onViewClickListener(View view, int position) {
                switch (view.getId()) {
                    case R.id.all_tasks_follow:
                        Toast.makeText(TasksActivity.this, "收藏按钮  +  " + position, Toast.LENGTH_SHORT).show();
                        JSONObject jb = data.get(position);
                        TextView tv = view.findViewById(R.id.all_tasks_follow);
                        String follow = "";
//                        if ("已关注".equals(tv.getText().toString())){
//                            follow = "true";
//                        }else {
//                            follow = "false";
//                        }
//                        try {
//                            follow(jb.getInt("taskId"),follow,"null");
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                        break;
                }
            }
        });
    }

    public void follow(int id,String follow,String msg) {
        Constant constant = new Constant();
        map.put("username", Constant.username);
        map.put("action", "modifyCollect");
        map.put("isFollow",follow);
        map.put("taskId",id);
        map.put("msg",msg);
        RetrofitBuilder retrofitBuilder = new RetrofitBuilder(constant.BaseUrl + "/api/userCollect/");
        retrofitBuilder.isConnected(this);
        retrofitBuilder.params(map);
        retrofitBuilder.post();
        Call<ResponseBody> call = retrofitBuilder.getCall();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    System.out.println("///////////////" + response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @Override
    public void finish() {
        super.finish();
    }


}
