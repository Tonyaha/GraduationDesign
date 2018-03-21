package com.tm.book_of_exercises.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


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
 * Created by T M on 2018/2/4.
 */

public class UserRegister extends AppCompatActivity {
    public final int CODE_SELECT_IMAGE = 2;//相册RequestCode
    public final int TYPE_TAKE_PHOTO = 1;//Uri获取类型判断
    public final int CODE_TAKE_PHOTO = 1;//相机RequestCode

    private Bitmap bp;
    private String string_bitmap;


    private Button btn_register, btn_login;
    //public ImageView imageView;
    private EditText et_user, et_pwd, et_school, et_class, et_phone;
    private RadioButton rb_teacher;
    private RadioButton rb_student;
    private LinearLayout class_linearLayout;
    private RadioGroup radioGroup;

    String username = "";
    String pwd = "";
    String school = "";
    String class_ = "";
    String phone = "";
    //String user_logo = "";
    String teacher = "";
    String occupation = "学生";

    Map<String, Object> map = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_register);

        //imageView = findViewById(R.id.img_logo);
        et_user = findViewById(R.id.register_username);
        et_pwd = findViewById(R.id.register_password);
        et_school = findViewById(R.id.register_user_school);
        et_class = findViewById(R.id.register_user_class);
        et_phone = findViewById(R.id.register_user_phone);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        rb_teacher = findViewById(R.id.rb_teacher);
        rb_student = findViewById(R.id.rb_student);
        class_linearLayout = findViewById(R.id.class_LinearLayout);
        radioGroup = findViewById(R.id.rg_student_teacher);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.rb_student:
                        class_linearLayout.setVisibility(View.VISIBLE);
                        occupation = (rb_student.getText().toString()).trim();
                        break;
                    case R.id.rb_teacher:
                        class_linearLayout.setVisibility(View.INVISIBLE);
                        occupation = (rb_teacher.getText().toString()).trim();
                        break;
                }
            }
        });

        MyOnClickListener myOnClickListener = new MyOnClickListener();
        btn_login.setOnClickListener(myOnClickListener);
        btn_register.setOnClickListener(myOnClickListener);
        //imageView.setOnClickListener(myOnClickListener);
    }

    public class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_login:
                    Intent intent = new Intent(UserRegister.this, UserLogin.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_in);
                    break;
                case R.id.btn_register:
                    inputHandle();
                    break;
//                case R.id.img_logo:
//                    Intent albumIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    albumIntent.setType("image/*");//相片类型
//                    startActivityForResult(albumIntent, CODE_SELECT_IMAGE);
                default:
                    break;
            }
        }
    }

    public void inputHandle() {
        username = (et_user.getText().toString()).trim();
        pwd = (et_pwd.getText().toString()).trim();
        school = (et_school.getText().toString()).trim();
        class_ = (et_class.getText().toString()).trim();
        phone = (et_phone.getText().toString()).trim();
        //user_logo = string_bitmap;
        teacher = (String) getBaseContext().getResources().getText(R.string.teacher);

        if (occupation.equals(teacher)) {
            if (!username.isEmpty() && !pwd.isEmpty() && !school.isEmpty()) {
                map.put("username", username);
                map.put("password", pwd);
                map.put("school", school);
                map.put("phone", phone);
                map.put("occupation", occupation);
                request();
            } else {
                String prompt = (String) getBaseContext().getResources().getText(R.string.not_empty);
                Toast.makeText(UserRegister.this, prompt, Toast.LENGTH_LONG).show();
            }
        } else {
            if (!username.isEmpty() && !pwd.isEmpty() && !school.isEmpty() && !class_.isEmpty()) {
                map.put("username", username);
                map.put("password", pwd);
                map.put("school", school);
                map.put("class_", class_);
                map.put("phone", phone);
                map.put("occupation", occupation);
                request();
            } else {
                String prompt = (String) getBaseContext().getResources().getText(R.string.not_empty);
                Toast.makeText(UserRegister.this, prompt, Toast.LENGTH_LONG).show();
            }
        }
    }
    public void request() {
        Constant constant = new Constant();
        RetrofitBuilder retrofitBuilder = new RetrofitBuilder(constant.BaseUrl + "/register/");
        retrofitBuilder.isConnected(UserRegister.this);
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
                    String resultCode = "";
                    String msg = "";
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    resultCode = jsonObject.getString("code");
                    if("200".equals(resultCode)){
                        Toast.makeText(UserRegister.this,getBaseContext().getResources().getText(R.string.register_success),Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(UserRegister.this, UserLogin.class);
                        intent.putExtra("username", username);
                        intent.putExtra("password", pwd);
                        startActivity(intent);
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_in);
                        UserRegister.this.finish();
                    }else if("100".equals(resultCode)){
                        Toast.makeText(UserRegister.this,getBaseContext().getResources().getText(R.string.register_exist),Toast.LENGTH_LONG).show();
                    }else if("101".equals(resultCode)){
                        Toast.makeText(UserRegister.this,getBaseContext().getResources().getText(R.string.register_user_err),Toast.LENGTH_LONG).show();
                    }else if("102".equals(resultCode)){
                        Toast.makeText(UserRegister.this,getBaseContext().getResources().getText(R.string.register_user_pwd_err),Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(UserRegister.this,getBaseContext().getResources().getText(R.string.register_fail),Toast.LENGTH_LONG).show();
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
                Toast.makeText(UserRegister.this,getBaseContext().getResources().getText(R.string.connect_to_server),Toast.LENGTH_LONG).show();
                System.out.println(throwable.getMessage());
            }
        });
    }
}
