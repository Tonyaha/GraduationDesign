package com.tm.book_of_exercises.main.otherPage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tm.book_of_exercises.MainActivity;
import com.tm.book_of_exercises.R;
import com.tm.book_of_exercises.adapter.CollectAdapter;
import com.tm.book_of_exercises.constant.Constant;
import com.tm.book_of_exercises.tools.AnnotateUtils;
import com.tm.book_of_exercises.tools.ViewInject;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by T M on 2018/3/28.
 */

public class TasksActivity extends AppCompatActivity {
    @ViewInject(R.id.tasksRecyclerView)
    private RecyclerView mRecyclerView;
    public CollectAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //tasksRequest();
        initData();  // 初始化在前面，不然提示 E/RecyclerView: No adapter attached; skipping layout
        setContentView(R.layout.activity_tasks);

        AnnotateUtils.injectViews(this);
        //mRecyclerView = findViewById(R.id.tasksRecyclerView);
        mRecyclerView.setLayoutManager(mLayoutManager); // 设置布局管理器
        mRecyclerView.setAdapter(mAdapter); // 设置adapter
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());  // 设置Item添加和移除的动画
    }

    private void initData() {
        //Log.e("调试", "//////////============" + String.valueOf(MainActivity.allTasksData));
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAdapter = new CollectAdapter(TasksActivity.this,MainActivity.allTasksData, R.layout.item_all_tasks, R.id.all_task_tv, R.id.all_tasks_img, R.id.all_tasks_follow, R.id.all_tasks_label, "all");
        mAdapter.mySetOnClickListener(new CollectAdapter.MyOnClickListener() {
            @Override
            public void onViewClickListener(View view, int position) {
                switch (view.getId()) {
                    case R.id.all_tasks_follow:
                        //Toast.makeText(TasksActivity.this, "收藏按钮  +  " + position, Toast.LENGTH_SHORT).show();
                        JSONObject jb = MainActivity.allTasksData.get(position);
                        TextView tv = view.findViewById(R.id.all_tasks_follow);
                        try {
                            Constant constant = new Constant(TasksActivity.this);
                            constant.follow(jb.getInt("taskId"),tv.getText().toString(),"null");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
    }


}
