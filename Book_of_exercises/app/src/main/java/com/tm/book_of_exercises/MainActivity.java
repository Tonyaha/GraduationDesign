package com.tm.book_of_exercises;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.tm.book_of_exercises.chat.Friend;
import com.tm.book_of_exercises.constant.Constant;
import com.tm.book_of_exercises.http.RetrofitBuilder;
import com.tm.book_of_exercises.main.mainPage.AddFragment;
import com.tm.book_of_exercises.main.mainPage.CollectFragment;
import com.tm.book_of_exercises.main.mainPage.ContactFragment;
import com.tm.book_of_exercises.main.mainPage.MeFragment;
import com.tm.book_of_exercises.main.otherPage.SearchActivity;
import com.tm.book_of_exercises.myDialog.AlertDiolog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tm.book_of_exercises.constant.Constant.username;

public class MainActivity extends FragmentActivity implements RongIM.UserInfoProvider {
    protected static final String TAG = "MainActivity";
    private long firstTime = 0;
    private boolean hasGotToken = false;
    private AlertDialog.Builder alertDialog;

    private HashMap<String, Object> map = new HashMap<>();
    public static JSONObject jsonObject;
    private List<Fragment> fragments;
    private RadioGroup radioGroup;
    private ViewPager viewPager;

    private ImageView imageView;

    private Fragment mConversationList;
    private Fragment mConversationFragment = null;
    private List<Friend> userIdList;
    public static ArrayList<String> data = new ArrayList<>();
    public static ArrayList<JSONObject> collectData = new ArrayList<>();
    public static ArrayList<JSONObject> allTasksData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        getFriendList(); //好友列表
        queryFriend();
        collectRequest(); //习题列表
        tasksRequest(); //全部习题


        fragments = new ArrayList<>();

        imageView = findViewById(R.id.main_activity_title).findViewById(R.id.user_search);
        imageView.setOnClickListener(view1 -> searchButton());

        mConversationList = initConversationList();//获取融云会话列表的对象
        //MsgFragment msgFragment = new MsgFragment();// 顺序对界面有影响
        ContactFragment contactFragment = new ContactFragment();
        AddFragment addFragment = new AddFragment();
        CollectFragment collectFragment = new CollectFragment();
        MeFragment meFragment = new MeFragment();

        fragments.add(mConversationList);
        fragments.add(contactFragment);
        fragments.add(addFragment);
        fragments.add(collectFragment);
        fragments.add(meFragment);

        viewPager = findViewById(R.id.viewPage);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
        initViewPage();
        initMenuItem();

        initAccessToken();; //百度云orc识别初始化
    }

    private void initAccessToken() {
        OCR.getInstance().initAccessToken(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken accessToken) {
                String token = accessToken.getAccessToken();
                //System.out.println("////////"+token);
                hasGotToken = true;
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                AlertDiolog diolog = new AlertDiolog(MainActivity.this,"licence方式获取token失败",error.getMessage());
                diolog.alertText();
            }
        }, getApplicationContext());
    }

    private boolean checkTokenStatus() {
        if (!hasGotToken) {
            Toast.makeText(getApplicationContext(), "token还未成功获取", Toast.LENGTH_LONG).show();
        }
        return hasGotToken;
    }

    // 融云信息提供...
    private void queryFriend() {
        RetrofitBuilder retrofitBuilder = new RetrofitBuilder(new Constant().BaseUrl + "/userInfo/");
        retrofitBuilder.isConnected(this);
        retrofitBuilder.params("action", "friendship");
        retrofitBuilder.get();
        Call<ResponseBody> call = retrofitBuilder.getCall();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject1 = new JSONObject(response.body().string());
                    String resultCode = jsonObject1.getString("code");
                    if ("200".equals(resultCode)) {
                        userIdList = new ArrayList<Friend>();
                        JSONArray jsonArray = jsonObject1.getJSONArray("list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jb = jsonArray.getJSONObject(i);
                            userIdList.add(new Friend(jb.getString("username"), jb.getString("nickname"), Uri.parse(jb.getString("icon"))));
                        }
                        RongIM.setUserInfoProvider(MainActivity.this, true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
//
//                userIdList.add(new Friend("tm","TM",Constant.uri_tm));
//                userIdList.add(new Friend("tsm","TSM",Constant.uri_tsm));
//                userIdList.add(new Friend("admin","ADMIN",Constant.uri_admin));

            }

            //请求失败时回调
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Toast.makeText(MainActivity.this, getBaseContext().getResources().getText(R.string.connect_to_server), Toast.LENGTH_LONG).show();
                System.out.println(throwable.getMessage());
            }
        });
    }

    @Override
    public UserInfo getUserInfo(String s) {
        for (Friend i : userIdList) {
            if (i.getUserId().equals(s)) {
                Log.e(TAG, String.valueOf(i.getPortraitUri()));
                return new UserInfo(i.getUserId(), i.getName(), Uri.parse(String.valueOf(i.getPortraitUri())));
            }
        }
        Log.e(TAG, "UserId is : " + s);
        return null;
    }


    private Fragment initConversationList() {
        /**
         * appendQueryParameter对具体的会话列表做展示
         */
        if (mConversationFragment == null) {
            ConversationListFragment listFragment = new ConversationListFragment();//ConversationListFragment.getInstance();
            Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversationList")
                    .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false")//设置私聊会话是否聚合显示
                    .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")
                    .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//设置私聊会话是否聚合显示
                    .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//设置私聊会是否聚合显示
                    .build();
            listFragment.setUri(uri);
            return listFragment;
        } else {
            return mConversationFragment;
        }
    }


    private void searchButton() {
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(intent);
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
//            System.out.println("按下了back键   onKeyDown()");
//            final AlertDialog.Builder alterDialog = new AlertDialog.Builder(this);
//            alterDialog.setMessage("确定退出应用？");
//            alterDialog.setCancelable(true);
//            alterDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    if(RongIM.getInstance() != null){
//                        RongIM.getInstance().disconnect();
//
//                        Intent intent = new Intent(MainActivity.this, StartAppActivity.class);
//                        intent.putExtra(StartAppActivity.EXIST, true);
//                        startActivity(intent);
//                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//                    }
//                }
//            });
//            alterDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    dialogInterface.cancel();
//                }
//            });
//            alterDialog.show();
//
//            return false;
//        } else {
//            return super.onKeyDown(keyCode, event);
//        }
//    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                Toast.makeText(MainActivity.this, "再按一次返回键退出", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;
                return true;
            } else {
                if (RongIM.getInstance() != null) {
                    RongIM.getInstance().disconnect();
                    Intent intent = new Intent(MainActivity.this, StartAppActivity.class);
                    intent.putExtra(StartAppActivity.EXIST, true);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }

            }
        }

        return super.onKeyUp(keyCode, event);
    }

    private void initMenuItem() {
        radioGroup = findViewById(R.id.mymain_radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int index = group.getCheckedRadioButtonId(); //通过 group 找对应按钮 id
                RadioButton rb = findViewById(index);
                rb.setChecked(true);

                for (int i = 0; i < group.getChildCount(); i++) {
                    RadioButton rb_01 = (RadioButton) group.getChildAt(i);
                    if (rb_01.isChecked()) {
                        viewPager.setCurrentItem(i);
                        //Toast.makeText(getActivity(),"第 "+(i+1)+"个界面",Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }
        });
    }

    private void initViewPage() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                RadioButton radioButton = (RadioButton) radioGroup.getChildAt(position);
                radioButton.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    // 好友列表
    public void getFriendList() {
        Constant constant = new Constant();
        map.put("username", username);
        map.put("action", "listOfFriend");
        RetrofitBuilder retrofitBuilder = new RetrofitBuilder(constant.BaseUrl + "/userInfo/");
        retrofitBuilder.isConnected(this);
        retrofitBuilder.params(map);
        retrofitBuilder.get();
        Call<ResponseBody> call = retrofitBuilder.getCall();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    //System.out.println("///////////" + response.body().string());
                    jsonObject = new JSONObject(response.body().string());
                    if ("200".equals(jsonObject.getString("code"))) {
                        JSONArray jsonArray = jsonObject.getJSONArray("list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject job = jsonArray.getJSONObject(i);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                            //System.out.println(job.get("username")+"=//////////") ;  // 得到 每个对象中的属性值
                            String remark = job.getString("remark");
                            String nickname = job.getString("nickname");

                            //System.out.println("///////////" + remark + "     " + nickname);
                            //data.add(job.getString("remark"));
                            if ("null".equals(remark)) {  //换了很多种写法.......
                                data.add(nickname);
                            } else {
                                data.add(remark);
                            }
                        }
                    }
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
                Toast.makeText(MainActivity.this, getBaseContext().getResources().getText(R.string.connect_to_server), Toast.LENGTH_LONG).show();
                System.out.println(throwable.getMessage());
            }
        });
    }

    //收藏列表
    private void collectRequest() {
        Constant constant = new Constant();
        RetrofitBuilder builder = new RetrofitBuilder(constant.BaseUrl + "/api/userCollect/");
        builder.isConnected(MainActivity.this);
        map.put("username", Constant.username);
        map.put("action", "collectTasks");
        builder.params(map);
        builder.get();
        Call<ResponseBody> call = builder.getCall();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    //System.out.println("////////////" + response.body().string());
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String code = jsonObject.getString("code");
                    if ("200".equals(code)) {
                        try {
                            JSONArray jsonArray = jsonObject.getJSONArray("list");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jb = jsonArray.getJSONObject(i);
                                collectData.add(jb);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if ("404".equals(code)) {
                        Log.e("CollectFragment", jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //请求失败时回调
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Toast.makeText(MainActivity.this, getBaseContext().getResources().getText(R.string.connect_to_server), Toast.LENGTH_LONG).show();
                System.out.println(throwable.getMessage());
            }
        });
    }

    //全部习题
    private void tasksRequest() {
        Constant constant = new Constant();
        RetrofitBuilder builder = new RetrofitBuilder(constant.BaseUrl + "/api/userCollect/");
        builder.isConnected(this);
        map.put("username", Constant.username);
        map.put("action", "allTasks");
        builder.params(map);
        builder.get();
        Call<ResponseBody> call = builder.getCall();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    //System.out.println("////////////" + response.body().string());
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String code = jsonObject.getString("code");
                    if ("200".equals(code)) {
                        try {
                            JSONArray jsonArray = jsonObject.getJSONArray("list");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jb = jsonArray.getJSONObject(i);
                                allTasksData.add(jb);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if ("404".equals(code)) {
                        Log.e("CollectFragment", jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
