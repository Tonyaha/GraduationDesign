package com.tm.book_of_exercises.main.mainPage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.tm.book_of_exercises.MainActivity;
import com.tm.book_of_exercises.R;
import com.tm.book_of_exercises.adapter.MyAdapter;
import com.tm.book_of_exercises.constant.Constant;
import com.tm.book_of_exercises.main.otherPage.SearchActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by T M on 2018/3/14.
 */

public class ContactFragment extends Fragment {
    //@ViewInject(R.id.contact_list)
    private RecyclerView mRecyclerView;
    public MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    //@ViewInject(R.id.contact_add_friend)
    private LinearLayout addFriendLinearLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact,container,false);

        initData();  // 初始化在前面，不然提示 E/RecyclerView: No adapter attached; skipping layout
        //AnnotateUtils.injectViews(getActivity());
        addFriendLinearLayout = view.findViewById(R.id.contact_add_friend);
        mRecyclerView = view.findViewById(R.id.contactRecyclerView);
        mRecyclerView.setLayoutManager(mLayoutManager); // 设置布局管理器
        mRecyclerView.setAdapter(mAdapter); // 设置adapter
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());  // 设置Item添加和移除的动画
        //mRecyclerView.addItemDecoration(mDividerItemDecoration); // 设置Item之间间隔样式

        //initView();
        addFriendLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });
        return view;//super.onCreateView(inflater, container, savedInstanceState);
    }

//    private void initView() {
//        mRecyclerView =
//    }

    private void initData() {
        mLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        mAdapter = new MyAdapter(MainActivity.data,R.layout.item_contact,R.id.oneContact,R.id.oneContactImg);

        mAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                try {
                    JSONArray jsonArray = MainActivity.jsonObject.getJSONArray("list");
                    JSONObject jb = jsonArray.getJSONObject(position);

                    Intent intent = new Intent(getActivity(),SearchActivity.class);
                    intent.putExtra("searchName",jb.getString("username"));
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(getActivity(),"click " + position + " item", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(getActivity(),"long click " + position + " item", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
