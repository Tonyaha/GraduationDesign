package com.tm.book_of_exercises.main.otherPage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tm.book_of_exercises.MainActivity;
import com.tm.book_of_exercises.R;
import com.tm.book_of_exercises.adapter.SelectContactAdapter;
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

import static com.tm.book_of_exercises.constant.Constant.username;

/**
 * 作者：T M on 2018/5/6 13:09
 * 邮箱：xxx@163.com
 */
public class SelectContactActivity extends AppCompatActivity implements View.OnClickListener {
    @ViewInject(R.id.selectContactRecyclerView)
    private RecyclerView mRecyclerView;
    @ViewInject(R.id.select_send)
    private TextView tv_send;
    @ViewInject(R.id.select_activity_linearLayout)
    private LinearLayout linearLayoutSelect;
    public SelectContactAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private boolean flag = false;
    private ArrayList<String> selectList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_contact_activity);
        AnnotateUtils.injectViews(this);
        initData();
        if (MainActivity.updateContactFlag){
            getFriendList();
        }
        mRecyclerView.setLayoutManager(mLayoutManager); // 设置布局管理器
        mRecyclerView.setAdapter(mAdapter); // 设置adapter
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());  // 设置Item添加和移除的动画

        tv_send.setOnClickListener(this);
        linearLayoutSelect.setOnClickListener(this);
    }

    private void initData() {
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAdapter = new SelectContactAdapter(SelectContactActivity.this, MainActivity.contactData);
        mAdapter.setOnItemClickListener(new SelectContactAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                if (selectList.size() > 1) { //加一层保障，防止数据重复
                    for (int i = 0; i < selectList.size(); i++) {
                        for (int j = 1; j < selectList.size(); j++) {
                            if (selectList.get(i).equals(selectList.get(j))) {
                                selectList.remove(j);
                            }
                        }
                    }
                }
                JSONObject jb = null;
                ImageView select = view.findViewById(R.id.select_contact_image);
                //Log.e("contact", "位置 " + String.valueOf(position));
                try {  // 这中写法会导致数据重复，暂未解决
                    if (!flag) {
                        jb = MainActivity.contactData.get(position); //得到同一位置的数据，不能写在外面
                        select.setImageResource(R.mipmap.select_true);
                        selectList.add(jb.getString("username"));
                        flag = true;
                        //Log.e("contact", "选中时" + String.valueOf(selectList));
                    } else {
                        jb = MainActivity.contactData.get(position);//得到同一位置的数据
                        select.setImageResource(R.mipmap.select_false);
                        selectList.remove(jb.getString("username"));
                        //Log.e("contact", String.valueOf(selectList));
                        flag = false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_send:
                Intent intent = getIntent();
                String data = intent.getStringExtra("data");
                if ("".equals(data)) {
                    Toast.makeText(SelectContactActivity.this, "习题不存在", Toast.LENGTH_LONG).show();
                } else {
                    for (int i = 0; i < selectList.size(); i++) {
                        Constant constant = new Constant(this);
                        constant.sendMsg(selectList.get(i), data);
                    }
                }
                finish();
                break;
            case R.id.select_activity_linearLayout:
                break;
        }
    }

    public void getFriendList() {
        ArrayList<JSONObject> data_ = new ArrayList<>();
        HashMap<String, Object> map = new HashMap<>();
        Constant constant = new Constant();
        map.put("username", username);
        map.put("action", "listOfFriend");
        RetrofitBuilder retrofitBuilder = new RetrofitBuilder(constant.BaseUrl + "/userInfo/");
        //retrofitBuilder.isConnected(this);
        retrofitBuilder.params(map);
        retrofitBuilder.get();
        Call<ResponseBody> call = retrofitBuilder.getCall();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    //System.out.println("///////////" + response.body().string());
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    if ("200".equals(jsonObject.getString("code"))) {
                        JSONArray jsonArray = jsonObject.getJSONArray("list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject job = jsonArray.getJSONObject(i);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                            //System.out.println(job.get("username")+"=//////////") ;  // 得到 每个对象中的属性值
                            data_.add(job);
//                            String remark = job.getString("remark");
//                            String nickname = job.getString("nickname");
//
//                            //System.out.println("///////////" + remark + "     " + nickname);
//                            //data.add(job.getString("remark"));
//                            if ("null".equals(remark)) {  //换了很多种写法.......
//                                data.add(nickname);
//                            } else {
//                                data.add(remark);
//                            }
                        }
                    }
                    mAdapter.updateData(data_);
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
                //Toast.makeText(MainActivity.this, getBaseContext().getResources().getText(R.string.connect_to_server), Toast.LENGTH_LONG).show();
                //System.out.println(throwable.getMessage());
            }
        });
    }
}
