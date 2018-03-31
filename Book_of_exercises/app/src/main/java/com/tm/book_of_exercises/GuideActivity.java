package com.tm.book_of_exercises;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.tm.book_of_exercises.main.UserLogin;
import com.tm.book_of_exercises.main.UserRegister;

/**
 * Created by T M on 2018/2/8.
 */

public class GuideActivity extends AppCompatActivity {
    private Button register;
    private Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        register = findViewById(R.id.img_register);
        login = findViewById(R.id.img_login);

        MyOnClickListener myOnClickListener = new MyOnClickListener();
        register.setOnClickListener(myOnClickListener);
        login.setOnClickListener(myOnClickListener);

    }
    public class MyOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {

            switch (view.getId()){
                case R.id.img_register:
                    Intent intent = new Intent(GuideActivity.this,UserRegister.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_left_in,R.anim.push_left_in);
                    break;
                case R.id.img_login:
                    Intent intent1 = new Intent(GuideActivity.this,UserLogin.class);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.push_left_in,R.anim.push_left_in);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            System.out.println("按下了back键   onKeyDown()");
            Intent intent = new Intent(this,StartAppActivity.class);
            intent.putExtra(StartAppActivity.EXIST,true);
            startActivity(intent);
            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }

}
