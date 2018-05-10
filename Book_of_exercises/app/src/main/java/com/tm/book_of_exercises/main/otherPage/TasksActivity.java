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

import com.tm.book_of_exercises.MainActivity;
import com.tm.book_of_exercises.R;
import com.tm.book_of_exercises.adapter.AllTasksAdapter;
import com.tm.book_of_exercises.constant.Constant;
import com.tm.book_of_exercises.http.RetrofitBuilder;
import com.tm.book_of_exercises.tools.AnnotateUtils;
import com.tm.book_of_exercises.tools.ViewInject;

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

/**
 * Created by T M on 2018/3/28.
 */

public class TasksActivity extends AppCompatActivity {
    @ViewInject(R.id.tasksRecyclerView)
    private RecyclerView mRecyclerView;
    public AllTasksAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public static int pos = 888888888;
    private boolean flag = false;
    private ArrayList<JSONObject> allTasksData = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        initData();  // 初始化在前面，不然提示 E/RecyclerView: No adapter attached; skipping layout
        if (MainActivity.updateFlag){
            tasksRequest();
        }

        //tasksRequest();
//        Constant constant = new Constant(TasksActivity.this);
//        constant.tasksRequest();
//        data = constant.getData();
        //Log.e("调试", "//////////============" + constant.getData());
        //tasksRequest();


        AnnotateUtils.injectViews(this);
        //mRecyclerView = findViewById(R.id.tasksRecyclerView);
        mRecyclerView.setLayoutManager(mLayoutManager); // 设置布局管理器
        mRecyclerView.setAdapter(mAdapter); // 设置adapter
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());  // 设置Item添加和移除的动画
    }

    private void initData() {;
        mLayoutManager = new LinearLayoutManager(TasksActivity.this, LinearLayoutManager.VERTICAL, false);
        mAdapter = new AllTasksAdapter(TasksActivity.this, MainActivity.allTasksData);
        //tasksRequest();
        //mAdapter.updateData(allTasksData);
//        if("".equals(MainActivity.allTasksData) || MainActivity.allTasksData == null) {
//            mAdapter = new AllTasksAdapter(TasksActivity.this, MainActivity.allTasksData);
//        }else {
//            mAdapter.updateData(Constant.kongData);
//        }
        mAdapter.mySetOnClickListener(new AllTasksAdapter.MyOnClickListener() {
            @Override
            public void onViewClickListener(View view, int position) {
                switch (view.getId()) {
                    case R.id.all_tasks_follow:
                        //Toast.makeText(TasksActivity.this, "收藏按钮  +  " + position, Toast.LENGTH_SHORT).show();
                        JSONObject jb = MainActivity.allTasksData.get(position);
                        TextView tv = view.findViewById(R.id.all_tasks_follow);
                        try {
                            Constant constant = new Constant(TasksActivity.this);
                            constant.follow(jb.getInt("taskId"),tv.getText().toString(),"","");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.all_tasks_see_answer:
                        if(!flag){
                            pos = position;
                            Constant.showAnswerFlag = true;
                            mAdapter.updateData(MainActivity.allTasksData);
                            flag = false;
                        }else {
                            pos = position;
                            Constant.showAnswerFlag = false;
                            mAdapter.updateData(MainActivity.allTasksData);
                            flag = true;
                        }
                        break;
                }
            }
        });
    }

    public void tasksRequest() {
        HashMap<String, Object> map = new HashMap<>();
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
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String code = jsonObject.getString("code");
                    if ("200".equals(code)) {
                        try {
                            JSONArray jsonArray = jsonObject.getJSONArray("list");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jb = jsonArray.getJSONObject(i);
                                allTasksData.add(jb);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //mAdapter = new AllTasksAdapter(TasksActivity.this, allTasksData);
                        mAdapter.updateData(allTasksData);
                        //Log.e("调试", String.valueOf(data_1));
                        //Toast.makeText(TasksActivity.this,"数据请求成功", Toast.LENGTH_LONG).show();
                    } else if ("404".equals(code)) {
                        Log.e("CollectFragment", jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                MainActivity.updateCollectFlag = true;
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
