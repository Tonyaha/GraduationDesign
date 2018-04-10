package com.tm.book_of_exercises;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.tm.book_of_exercises.main.UserLogin;



/**
 * Created by T M on 2018/2/8.
 */

public class StartAppActivity extends AppCompatActivity {
    public static final String EXIST = "exist";
    private String username;
    private String password;
    private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        //setContentView(R.layout.start_app);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getAccount();
                if("".equals(username) || username.equals(null) || "null".equals(username)){
                    Intent intent = new Intent(StartAppActivity.this,GuideActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
                }else {
                    new UserLogin().connectRongServer(token);
                    Intent intent = new Intent(StartAppActivity.this,MainActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
                }

                //StartAppActivity.this.finish();
            }
        },3000);

    }

    /**
     * 获取存入SharedPreference中的账户
     *
     * @return
     * */
    public void getAccount(){
        SharedPreferences preferences = getSharedPreferences("loginInfo", Activity.MODE_PRIVATE);
        username = preferences.getString("username","");
        password = preferences.getString("password","");
        token = preferences.getString("token","");
    }
    /*
    * 如果设置一个Activity的启动模式为singleTask，那么每次启动此Activity时系统都会检查当前任务栈中是否已经存在此Activity的实例。
    * 如果已经存在，则不再创建此Activity新的实例，而是调用已存在的Activity的onNewIntent()方法，
    * 并把intent作为此方法的参数传递给它。然后它就会被置于栈顶，并把它之前的所有的Activity移除掉。
    */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent != null){ //判断其他Activity启动本Activity时传递来的intent是否为空
            //获取intent中对应Tag的布尔值
            boolean isExist = intent.getBooleanExtra(EXIST,false);

            //如果为真则退出本Activity
            if(isExist){
                StartAppActivity.this.onDestroy(); //在Activity的生命周期中，onDestory()方法是他生命的最后一步，资源空间等就被回收了。当重新进入此Activity的时候，必须重新创建，执行onCreate()方法。
                //this.finish();  //系统只是将最上面的Activity移出了栈，并没有及时的调用onDestory（）方法，其占用的资源也没有被及时释放。因为移出了栈，所以当你点击手机上面的“back”按键的时候，也不会再找到这个Activity。
                //System.exit(0);
            }
        }
    }
}
