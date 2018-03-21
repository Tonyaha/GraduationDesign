package com.tm.book_of_exercises;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;


/**
 * Created by T M on 2018/2/8.
 */

public class StartAppActivity extends AppCompatActivity {
    public static final String EXIST = "exist";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.start_app);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(StartAppActivity.this,MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
                //StartAppActivity.this.finish();
            }
        },3000);

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
                this.onDestroy(); //在Activity的生命周期中，onDestory()方法是他生命的最后一步，资源空间等就被回收了。当重新进入此Activity的时候，必须重新创建，执行onCreate()方法。
                //this.finish();  //系统只是将最上面的Activity移出了栈，并没有及时的调用onDestory（）方法，其占用的资源也没有被及时释放。因为移出了栈，所以当你点击手机上面的“back”按键的时候，也不会再找到这个Activity。
                //System.exit(0);
            }
        }
    }
}
