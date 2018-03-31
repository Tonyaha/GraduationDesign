package com.tm.book_of_exercises.main.mainPage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tm.book_of_exercises.MainActivity;
import com.tm.book_of_exercises.R;
import com.tm.book_of_exercises.adapter.CollectAdapter;

/**
 * Created by T M on 2018/3/14.
 */

public class CollectFragment extends Fragment {
    private RecyclerView mRecyclerView;
    public CollectAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collect,container,false);
        initData();  // 初始化在前面，不然提示 E/RecyclerView: No adapter attached; skipping layout
        //AnnotateUtils.injectViews(getActivity());
        mRecyclerView = view.findViewById(R.id.collectRecyclerView);
        mRecyclerView.setLayoutManager(mLayoutManager); // 设置布局管理器
        mRecyclerView.setAdapter(mAdapter); // 设置adapter
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());  // 设置Item添加和移除的动画
        //mRecyclerView.addItemDecoration(mDividerItemDecoration); // 设置Item之间间隔样式
        return view;//super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initData() {
        mLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        mAdapter = new CollectAdapter(MainActivity.collectData,R.layout.item_collect_tasks,R.id.tv_task,R.id.task_img,R.id.tasks_collect,R.id.tasks_follow,R.id.tv_msg,R.id.tasks__label,"collect");
        mAdapter.mySetOnClickListener(new CollectAdapter.MyOnClickListener() {
            @Override
            public void onViewClickListener(View view, int position) {
//                JSONArray jsonArray = MainActivity.jsonObject.getJSONArray("list");
//                JSONObject jb = jsonArray.getJSONObject(position);
                switch (view.getId()){
//                    case R.id.tasks_collect:
//                        Toast.makeText(getActivity(),"收藏按钮  +  "+position,Toast.LENGTH_SHORT).show();
//                        break;
                    case R.id.img_msg:
                        Toast.makeText(getActivity(),"留言按钮  +  " + position,Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.tasks_follow:
                        Toast.makeText(getActivity(),"收藏按钮  +  "+position,Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }


}
