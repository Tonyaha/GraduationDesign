package com.tm.book_of_exercises.main.mainPage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tm.book_of_exercises.MainActivity;
import com.tm.book_of_exercises.R;
import com.tm.book_of_exercises.adapter.MyAdapter;
import com.tm.book_of_exercises.constant.Constant;
import com.tm.book_of_exercises.main.otherPage.SearchActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by T M on 2018/3/14.
 */

public class ContactFragment extends Fragment implements View.OnClickListener{
    //@ViewInject(R.id.contact_list)
    private RecyclerView mRecyclerView;
    public MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    //@ViewInject(R.id.contact_add_friend)
    private LinearLayout addFriendLinearLayout;
    private LinearLayout linearLayout_select_menu,contact_select_linearLayout;
    private TextView imgCancel,imgDel,imgSend;
    private Boolean selectFlag = false;
    //private View view;
    private ArrayList<String> isSelectList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact,container,false);
        initData();  // 初始化在前面，不然提示 E/RecyclerView: No adapter attached; skipping layout
        initView(view);
        initClick();
        //AnnotateUtils.injectViews(getActivity());
        Log.e("iiii", "constant   "+String.valueOf(MainActivity.selectContactFlag));
        if(MainActivity.selectContactFlag == 1){
            contact_select_linearLayout.setVisibility(View.VISIBLE);
            addFriendLinearLayout.setVisibility(View.GONE);
            Log.e("iiii", "0000    "+String.valueOf(MainActivity.selectContactFlag));
        }


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

    private void initView(View view) {
        contact_select_linearLayout = view.findViewById(R.id.contact_select_linearLayout);
        linearLayout_select_menu = view.findViewById(R.id.linearLayout_contact);
        addFriendLinearLayout = view.findViewById(R.id.contact_add_friend);
        mRecyclerView = view.findViewById(R.id.contactRecyclerView);
        imgCancel = view.findViewById(R.id.cancel_contact);
        imgDel = view.findViewById(R.id.del_contact);
        imgSend = view.findViewById(R.id.send_contact);
    }

    private void initClick() {
        imgDel.setOnClickListener(this);
        imgCancel.setOnClickListener(this);
        imgSend.setOnClickListener(this);
        contact_select_linearLayout.setOnClickListener(this);
    }

    private void initData() {
        mLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        mAdapter = new MyAdapter(getActivity(),MainActivity.contactData);

        mAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(view.getId() == R.id.select_contact){
                    JSONObject jb = MainActivity.contactData.get(position);
                    ImageView select = view.findViewById(R.id.select_contact);
                    try {
                        if(!selectFlag){
                            select.setImageResource(R.mipmap.select_true);
                            isSelectList.add(jb.getString("username"));
                            selectFlag = true;
                        }else {
                            select.setImageResource(R.mipmap.select_false);
                            isSelectList.remove(jb.getString("username"));
                            selectFlag = false;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    try {
                        JSONArray jsonArray = MainActivity.jsonObject.getJSONArray("list");
                        JSONObject jb = jsonArray.getJSONObject(position);

                        Intent intent = new Intent(getActivity(),SearchActivity.class);
                        intent.putExtra("searchName",jb.getString("username"));
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //Toast.makeText(getActivity(),"click " + position + " item", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, ArrayList<JSONObject> mData) {
                linearLayout_select_menu.setVisibility(View.VISIBLE);
                addFriendLinearLayout.setVisibility(View.GONE);
                contact_select_linearLayout.setVisibility(View.VISIBLE);
                Constant.showSelectFlag = true;
                mAdapter.updateData(mData);
                //Toast.makeText(getActivity(),"long click " + position + " item", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel_contact:
                linearLayout_select_menu.setVisibility(View.GONE);
                Constant.showSelectFlag = false;
                mAdapter.updateData(MainActivity.contactData);
                break;
            case R.id.del_contact:
                break;
            case R.id.send_contact:
                if("".equals(Constant.sendData)){
                    Toast.makeText(getActivity(),"没有选择要发送的习题",Toast.LENGTH_LONG).show();
                }else {
                    for(int i=0;i<isSelectList.size();i++){
                        Constant constant = new Constant(getActivity());
                        constant.sendMsg(isSelectList.get(i),Constant.sendData);
                    }
                }
                break;
            case R.id.contact_select_linearLayout:
                linearLayout_select_menu.setVisibility(View.VISIBLE);
                Constant.showSelectFlag = true;
                mAdapter.updateData(MainActivity.contactData);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        linearLayout_select_menu.setVisibility(View.GONE);
        addFriendLinearLayout.setVisibility(View.VISIBLE);
        contact_select_linearLayout.setVisibility(View.GONE);
        Constant.showSelectFlag = false;
        MainActivity.selectContactFlag = 0;
        mAdapter.updateData(MainActivity.contactData);
    }
}
