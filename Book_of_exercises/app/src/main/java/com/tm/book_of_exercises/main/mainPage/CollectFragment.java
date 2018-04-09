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
import android.widget.TextView;

import com.tm.book_of_exercises.MainActivity;
import com.tm.book_of_exercises.R;
import com.tm.book_of_exercises.adapter.CollectAdapter;
import com.tm.book_of_exercises.constant.Constant;
import com.tm.book_of_exercises.myDialog.InputDialog;
import com.tm.book_of_exercises.myDialog.InputDialogInterface;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by T M on 2018/3/14.
 */

public class CollectFragment extends Fragment {
    private RecyclerView mRecyclerView;
    public CollectAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    int taskID = 0;
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
        mAdapter = new CollectAdapter(getActivity(),MainActivity.collectData,R.layout.item_collect_tasks,R.id.tv_task,R.id.task_img,R.id.tasks_collect,R.id.tasks_follow,R.id.tv_msg,R.id.tasks__label,"collect");
        mAdapter.mySetOnClickListener(new CollectAdapter.MyOnClickListener() {
            @Override
            public void onViewClickListener(View view, int position) {
                switch (view.getId()){
                    case R.id.img_msg:
                        String msg = null;
                        JSONObject jsonObject = MainActivity.collectData.get(position);
                        try {
                            msg = jsonObject.getString("msg");
                            taskID = jsonObject.getInt("taskId");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        InputDialog addFriendDialog = new InputDialog();
                        addFriendDialog.init("设置留言", "输入留言",msg, new InputDialogInterface() {
                            @Override
                            public void onClick() {
                                //Log.e("click!!!" , addFriendDialog.getInput());
                                Constant constant = new Constant(getActivity());
                                constant.follow(taskID,"已关注",addFriendDialog.getInput());
                            }

//                            @Override
//                            public FilterResult filter(String inputText) {
//                                return isInputValid(inputText);
//                            }
                        });
                        addFriendDialog.show(getFragmentManager(),"");
                        break;
                    case R.id.tasks_follow:
                        //Toast.makeText(getActivity(),"收藏按钮  +  "+position,Toast.LENGTH_SHORT).show();
                        JSONObject jb = MainActivity.collectData.get(position);
                        TextView tv = view.findViewById(R.id.tasks_follow);
                        try {
                            Constant constant = new Constant(getActivity());
                            constant.follow(jb.getInt("taskId"),tv.getText().toString(),"null");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });
    }


//    private InputDialogInterface.FilterResult isInputValid(String inputText) {
//        InputDialogInterface.FilterResult filterResult = new InputDialogInterface.FilterResult();
//        if (TextUtils.isEmpty(inputText)) {
//            filterResult.result = false;
//            filterResult.errorHint = "请输入用户号";
//        } else {
//            filterResult.result = true;
//        }
//        return filterResult;
//    }
}
