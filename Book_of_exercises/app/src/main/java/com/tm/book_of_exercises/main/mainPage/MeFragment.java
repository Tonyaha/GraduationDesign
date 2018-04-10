package com.tm.book_of_exercises.main.mainPage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tm.book_of_exercises.R;
import com.tm.book_of_exercises.adapter.CollectAdapter;
import com.tm.book_of_exercises.constant.Constant;
import com.tm.book_of_exercises.http.RetrofitBuilder;
import com.tm.book_of_exercises.main.UserLogin;
import com.tm.book_of_exercises.main.bean.ImageHandle;
import com.tm.book_of_exercises.main.otherPage.TasksActivity;
import com.tm.book_of_exercises.main.otherPage.UserInfo;

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
 * Created by T M on 2018/3/14.
 */

public class MeFragment extends Fragment {
    private LinearLayout infoLinearLayout;
    private ImageView imageView;
    private TextView nickname;
    private TextView account;
    private LinearLayout exitLinearLayout, allTaskLinearLayout;
    private LinearLayout linearLayout_;
    private String userGrade, userNickname, userPhone, userSchool, userOccupation, userClass, userLogo;
    HashMap<String, Object> map = new HashMap<>();
    private String userStrBitmap = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);

        infoLinearLayout = view.findViewById(R.id.user_info);
        imageView = view.findViewById(R.id.user_logo);
        nickname = view.findViewById(R.id.user_nickname);
        account = view.findViewById(R.id.user_account);
        exitLinearLayout = view.findViewById(R.id.exit_account);
        linearLayout_ = view.findViewById(R.id.add_user_info);
        allTaskLinearLayout = view.findViewById(R.id.all_tasks);

        account.setText("账号：" + username);
        query(username);

        MyOnClickListener myOnClickListener = new MyOnClickListener();
        infoLinearLayout.setOnClickListener(myOnClickListener);
        imageView.setOnClickListener(myOnClickListener);
        nickname.setOnClickListener(myOnClickListener);
        account.setOnClickListener(myOnClickListener);
        exitLinearLayout.setOnClickListener(myOnClickListener);
        linearLayout_.setOnClickListener(myOnClickListener);
        allTaskLinearLayout.setOnClickListener(myOnClickListener);

        return view;//super.onCreateView(inflater, container, savedInstanceState);
    }

    public void query(String user) {
        Constant constant = new Constant();
        map.put("username", user);
        map.put("action", "userInfo");
        RetrofitBuilder retrofitBuilder = new RetrofitBuilder(constant.BaseUrl + "/userInfo/");
        retrofitBuilder.isConnected(getActivity());
        retrofitBuilder.params(map);
        retrofitBuilder.get();
        Call<ResponseBody> call = retrofitBuilder.getCall();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    //System.out.println("////////"+response.body().string());
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    userNickname = jsonObject.getString("nickname");
                    userLogo = jsonObject.getString("icon");

                    //主线程处理消息队列中的消息，并刷新相应UI控件
                    Handler handler = new Handler() {
                        public void handleMessage(android.os.Message msg) {
                            imageView.setImageBitmap((Bitmap) msg.obj);
                            if("".equals(msg.obj) | msg.obj == null){
                                userStrBitmap = null;
                            }else {
                                userStrBitmap = ImageHandle.bitmapToString((Bitmap) msg.obj);
                            }

                        }
                    };
                    //同时要注意网络操作需在子线程操作，以免引起主线程阻塞，影响用途体验，同时采用handler消息机制进行参数处理，刷新UI控件
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            //String urlpath = "http://pic39.nipic.com/20140226/18071023_164300608000_2.jpg";
                            Bitmap bm = CollectAdapter.getInternetPicture(userLogo);
                            Message msg = new Message();
                            // 把bm存入消息中,发送到主线程
                            msg.obj = bm;
                            handler.sendMessage(msg);
                        }
                    }).start();

                    //imageView.setImageBitmap(ImageHandle.stringToImage(userLogo));
                    userPhone = jsonObject.getString("phone");
                    userSchool = jsonObject.getString("school");
                    userOccupation = jsonObject.getString("occupation");
                    userClass = jsonObject.getString("class_");
                    userGrade = jsonObject.getString("grade");
                    nickname.setText(userNickname);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //请求失败时回调
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Toast.makeText(getActivity(), getActivity().getResources().getText(R.string.connect_to_server), Toast.LENGTH_LONG).show();
                System.out.println(throwable.getMessage());
            }
        });
    }

    public class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.user_info:
                    Intent intent = new Intent(getActivity(), UserInfo.class);
                    putExtr(intent);
                    startActivity(intent);
                    break;
                case R.id.add_user_info:
                    Intent intent_ = new Intent(getActivity(), UserInfo.class);
                    putExtr(intent_);
                    startActivity(intent_);
                    break;
                case R.id.user_logo:
                    Intent intent__ = new Intent(getActivity(), UserInfo.class);
                    putExtr(intent__);
                    startActivity(intent__);
                    break;
                case R.id.user_nickname:
                    Intent intent__1 = new Intent(getActivity(), UserInfo.class);
                    putExtr(intent__1);
                    startActivity(intent__1);
                    break;
                case R.id.user_account:
                    Intent intent_1 = new Intent(getActivity(), UserInfo.class);
                    putExtr(intent_1);
                    startActivity(intent_1);
                    break;
                case R.id.exit_account:
                    Intent intent1 = new Intent(getActivity(), UserLogin.class);
                    startActivity(intent1);
                    break;
                case R.id.all_tasks:
                    Intent intent2 = new Intent(getActivity(), TasksActivity.class);
                    startActivity(intent2);
                    //overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    break;
            }
        }
    }

    void putExtr(Intent intent) {
        intent.putExtra("userLogo", userStrBitmap);
        intent.putExtra("userNickname", userNickname);
        intent.putExtra("userPhone", userPhone);
        intent.putExtra("userSchool", userSchool);
        intent.putExtra("userOccupation", userOccupation);
        intent.putExtra("userClass", userClass);
        intent.putExtra("userGrade", userGrade);
    }


}
