package com.tm.book_of_exercises.main.otherPage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tm.book_of_exercises.MainActivity;
import com.tm.book_of_exercises.R;
import com.tm.book_of_exercises.constant.Constant;
import com.tm.book_of_exercises.http.RetrofitBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tm.book_of_exercises.constant.Constant.username;

/**
 * Created by T M on 2018/3/18.
 */

public class SearchActivity extends AppCompatActivity {
    private ImageView backImageView,searchImageView;
    private EditText editText;
    private LinearLayout linearLayout_search;

    private ImageView imageView;
    private EditText et_remark;
    private TextView tv_remark,tv_username,tv_nickname,tv_class,tv_grade,tv_school,tv_occupation,tv_phone;
    private LinearLayout friendGradeAndClassLinearLayout,friendSetRemarkLinearLayout;

    private Button btn_send,btn_add;

    private String resultCode;

    private HashMap<String,Object> map = new HashMap<>();

    private String name = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        backImageView = findViewById(R.id.goBack);
        searchImageView = findViewById(R.id.userSearch);
        editText = findViewById(R.id.et_content);

        linearLayout_search = findViewById(R.id.searchLinear);

        Intent intent = getIntent();
        name = intent.getStringExtra("searchName");
        if(!"".equals(name)){
            editText.setText(name);
            searchFriend();
        }

        imageView = findViewById(R.id.search_include).findViewById(R.id.friendLogo);
        et_remark = findViewById(R.id.search_include).findViewById(R.id.setFriendRemark);
        tv_remark = findViewById(R.id.search_include).findViewById(R.id.friendRemark);
        tv_username = findViewById(R.id.search_include).findViewById(R.id.friendAccount);
        tv_nickname = findViewById(R.id.search_include).findViewById(R.id.friendNickname);
        tv_class = findViewById(R.id.search_include).findViewById(R.id.friendClass);
        tv_grade = findViewById(R.id.search_include).findViewById(R.id.friendGrade);
        tv_school = findViewById(R.id.search_include).findViewById(R.id.friendSchool);
        tv_occupation = findViewById(R.id.search_include).findViewById(R.id.friendOccupation);
        tv_phone = findViewById(R.id.search_include).findViewById(R.id.friendPhone);
        friendGradeAndClassLinearLayout = findViewById(R.id.search_include).findViewById(R.id.friend_GradeAndClass_LinearLayout);
        btn_send = findViewById(R.id.search_include).findViewById(R.id.friendSend);
        btn_add = findViewById(R.id.search_include).findViewById(R.id.friendAdd);
        friendSetRemarkLinearLayout = findViewById(R.id.search_include).findViewById(R.id.friendSetRemark);

        // 监听输入框变化
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchFriend();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_remark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                changeRemark();
            }
        });


        // 监听键盘回车
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    searchFriend();
                }
                return false;
            }
        });

        MyOnClickListener myOnClickListener = new MyOnClickListener();
        backImageView.setOnClickListener(myOnClickListener);
        searchImageView.setOnClickListener(myOnClickListener);
        btn_send.setOnClickListener(myOnClickListener);
        btn_add.setOnClickListener(myOnClickListener);
    }

    private void searchFriend() {
        Constant constant = new Constant();
        map.put("username", username);
        map.put("searchName",editText.getText().toString());
//        if(!"".equals(name)){
//            map.put("searchName",name);
//        }else {
//            map.put("searchName",editText.getText().toString());
//        }

        RetrofitBuilder retrofitBuilder = new RetrofitBuilder(constant.BaseUrl + "/api/search/");
        retrofitBuilder.isConnected(this);
        retrofitBuilder.params(map);
        retrofitBuilder.get();
        Call<ResponseBody> call = retrofitBuilder.getCall();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    resultCode = jsonObject.getString("code");
                    if("200".equals(resultCode)){
                        linearLayout_search.setVisibility(View.VISIBLE);
                        //imageView;
                        tv_remark.setText(jsonObject.getString("remark"));
                        tv_username.setText("账号：" + jsonObject.getString("username"));
                        tv_nickname.setText("昵称：" + jsonObject.getString("nickname"));
                        tv_school.setText(jsonObject.getString("school"));
                        tv_occupation.setText(jsonObject.getString("occupation"));
                        tv_phone.setText(jsonObject.getString("phone"));
                        if("学生".equals(jsonObject.getString("occupation"))){
                            friendGradeAndClassLinearLayout.setVisibility(View.VISIBLE);
                            tv_class.setText(jsonObject.getString("class_"));
                            tv_grade.setText(jsonObject.getString("grade"));
                        }else {
                            friendGradeAndClassLinearLayout.setVisibility(View.INVISIBLE);
                        }

                        if("True".equals(jsonObject.getString("isFriend")) | username.equals(jsonObject.getString("username"))){
                            btn_add.setVisibility(View.INVISIBLE);
                        }else {
                            btn_add.setVisibility(View.VISIBLE);
                        }

                    }else{
                        linearLayout_search.setVisibility(View.INVISIBLE);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //请求失败时回调
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Toast.makeText(SearchActivity.this, getBaseContext().getResources().getText(R.string.connect_to_server), Toast.LENGTH_LONG).show();
                System.out.println(throwable.getMessage());
            }
        });
    }

    public class MyOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.goBack:
                    finish();
                    break;
                case R.id.userSearch:
                    searchFriend();
                    break;
                case R.id.friendSend:
                    break;
                case R.id.friendAdd:
                    break;
            }
        }
    }

    private void changeRemark() {
        Constant constant = new Constant();
        map.put("username", username);
        map.put("searchName",editText.getText().toString());
        map.put("remark",et_remark.getText().toString());
        RetrofitBuilder retrofitBuilder = new RetrofitBuilder(constant.BaseUrl + "/api/search/");
        retrofitBuilder.isConnected(this);
        retrofitBuilder.params(map);
        retrofitBuilder.post();
        Call<ResponseBody> call = retrofitBuilder.getCall();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    System.out.println("////////" + response.body().string());
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    resultCode = jsonObject.getString("code");
                    if("200".equals(resultCode)) {
                        System.out.println("备注修改成功");
                    }else {
                        Toast.makeText(SearchActivity.this, "修改失败", Toast.LENGTH_LONG).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //请求失败时回调
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Toast.makeText(SearchActivity.this, getBaseContext().getResources().getText(R.string.connect_to_server), Toast.LENGTH_LONG).show();
                System.out.println(throwable.getMessage());
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            System.out.println("按下了back键   onKeyDown()");
            //new MeFragment().query(accountTV.getText().toString());
            finish();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
