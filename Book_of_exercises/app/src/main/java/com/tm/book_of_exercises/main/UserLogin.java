package com.tm.book_of_exercises.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.tm.book_of_exercises.MainActivity;
import com.tm.book_of_exercises.R;
import com.tm.book_of_exercises.constant.Constant;
import com.tm.book_of_exercises.http.RetrofitBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by T M on 2018/2/8.
 */

public class UserLogin extends AppCompatActivity {
    private EditText et_user;
    private EditText et_pwd;
    private Button bt_login;
    private Button bt_register;
    private String user;
    private String pwd;
    private Map<String,Object> map = new HashMap<>();
    String resultCode = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login);
        et_user = findViewById(R.id.et_username);
        et_pwd = findViewById(R.id.et_password);
        bt_login = findViewById(R.id.btn_login);
        bt_register = findViewById(R.id.btn_register);

        Intent intent = getIntent();
        user = intent.getStringExtra("username");
        pwd = intent.getStringExtra("password");
        et_user.setText(user);
        et_pwd.setText(pwd);

        MyOnClickListener myOnClickListener = new MyOnClickListener();
        bt_register.setOnClickListener(myOnClickListener);
        bt_login.setOnClickListener(myOnClickListener);
    }

    public class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.btn_login:
                    if (et_user.getText().toString().trim().isEmpty() || et_pwd.getText().toString().trim().isEmpty()) {
                        Toast.makeText(UserLogin.this, getBaseContext().getResources().getText(R.string.login_user_not_empty), Toast.LENGTH_SHORT).show();
                    } else {
                        request();
                    }
                    break;
                case R.id.btn_register:
                    Intent intent1 = new Intent(UserLogin.this, UserRegister.class);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_in);
                    break;
                default:
                    break;
            }
        }
    }

    private void request() {
        Constant constant = new Constant();
        map.put("username", et_user.getText().toString());
        map.put("password", et_pwd.getText().toString());

        RetrofitBuilder retrofitBuilder = new RetrofitBuilder(constant.BaseUrl + "/login/");
        retrofitBuilder.isConnected(UserLogin.this);
        retrofitBuilder.params(map);
        retrofitBuilder.post();
        Call<ResponseBody> call = retrofitBuilder.getCall();
        //步骤6:发送网络请求(异步)
        call.enqueue(new Callback<ResponseBody>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // 请求处理,输出结果
                // 输出翻译的内容
                try {
                    //System.out.println("///////"+response.body().string());
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    resultCode = jsonObject.getString("code");
                    if ("200".equals(resultCode)) {
                        Toast.makeText(UserLogin.this, getBaseContext().getResources().getText(R.string.login_success), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(UserLogin.this, MainActivity.class);
                        intent.putExtra("username", et_user.getText().toString());
                        startActivity(intent);
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_in);
                        UserLogin.this.finish();
                    } else if ("103".equals(resultCode)) {
                        Toast.makeText(UserLogin.this, getBaseContext().getResources().getText(R.string.login_account_err), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(UserLogin.this, getBaseContext().getResources().getText(R.string.login_fail), Toast.LENGTH_LONG).show();
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
                Toast.makeText(UserLogin.this, getBaseContext().getResources().getText(R.string.connect_to_server), Toast.LENGTH_LONG).show();
                System.out.println(throwable.getMessage());
            }
        });
    }
}
