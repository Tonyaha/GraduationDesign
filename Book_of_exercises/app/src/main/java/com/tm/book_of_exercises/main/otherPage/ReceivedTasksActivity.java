package com.tm.book_of_exercises.main.otherPage;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tm.book_of_exercises.MainActivity;
import com.tm.book_of_exercises.R;
import com.tm.book_of_exercises.adapter.ReceivedAdapter;
import com.tm.book_of_exercises.constant.Constant;
import com.tm.book_of_exercises.http.RetrofitBuilder;
import com.tm.book_of_exercises.main.bean.ImageHandle;
import com.tm.book_of_exercises.tools.AnnotateUtils;
import com.tm.book_of_exercises.tools.ViewInject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tm.book_of_exercises.main.bean.ImageHandle.bitmapToString;

public class ReceivedTasksActivity extends AppCompatActivity {
    @ViewInject(R.id.receiveRecyclerView)
    private RecyclerView mRecyclerView;
    public ReceivedAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    @ViewInject(R.id.received_label)
    private TextView label;
    @ViewInject(R.id.received_send)
    private TextView send;
    private boolean selectFlag = false;
    JSONArray data = null;
    ArrayList<HashMap> list = new ArrayList<>();
    private String mFilePath;
    private Bitmap prodImgA;
    public static String strAnswer = "";
    private String taskId = "0";
    public static int pos = 999999998;
    private String correctAnswer = "";
    private String myAnswer = "";
    private String occupation = "";
    private boolean flag = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();  // 初始化在前面，不然提示 E/RecyclerView: No adapter attached; skipping layout
        setContentView(R.layout.show_tasks_activity);

        AnnotateUtils.injectViews(this);

        //mRecyclerView = findViewById(R.id.tasksRecyclerView);
        mRecyclerView.setLayoutManager(mLayoutManager); // 设置布局管理器
        mRecyclerView.setAdapter(mAdapter); // 设置adapter
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());  // 设置Item添加和移除的动画

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("选择".equals(send.getText().toString().trim())) {
                    Constant.showSelectFlag = true;
                    mAdapter.updateData(data);
                    send.setText("发送");
                } else {
                    Constant.showSelectFlag = false;
                    mAdapter.updateData(data);
                    send.setText("选择");
                    Intent intent = new Intent(ReceivedTasksActivity.this, SelectContactActivity.class);
                    intent.putExtra("data", new Gson().toJson(list));
                    startActivity(intent);
                    finish();
                }
            }
        });
    }


    private void initData() {
        mFilePath = Environment.getExternalStorageDirectory().getPath();// 获取SD卡路径
        mFilePath = mFilePath + "/" + "myAnswer.png";// 指定路径
        Intent intent = getIntent();
        try {
            data = new JSONArray(intent.getStringExtra("tasks"));
            mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            mAdapter = new ReceivedAdapter(ReceivedTasksActivity.this, data);
            //mAdapter.updateData(data);
//            if("".equals(data) || data == null){
//                mAdapter = new ReceivedAdapter(ReceivedTasksActivity.this, data);
//            }else {
//                mAdapter.updateData(data);
//            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mAdapter.setOnClickListener(new ReceivedAdapter.OnClickListener() {
            @Override
            public void onClickListener(View view, int position) {
                switch (view.getId()) {
                    case R.id.received_write_answer:
                        pos = position;
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ReceivedTasksActivity.this);
                        alertDialog.setTitle("选择图片")
                                .setIcon(R.mipmap.icon)
                                .setMessage("选择一种获取答案的方式")
                                .setCancelable(true);

                        alertDialog.setPositiveButton("照相", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //启动相机程序
                                Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                Uri photoUri = Uri.fromFile(new File(mFilePath)); // 传递路径
                                takePhoto.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);  //设置自定义拍照保存的临时路径 ,//指定图片存放位置，指定后，在onActivityResult里得到的Data将为null
                                startActivityForResult(takePhoto, Constant.CODE_TAKE_PHOTO);
                                dialogInterface.cancel();
                            }
                        });
                        alertDialog.setNegativeButton("相册", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent selectFromPhotos = new Intent(Intent.ACTION_PICK);
                                //takePhoto.putExtra(MediaStore.EXTRA_OUTPUT, imgUriSelect);  //设置自定义拍照保存的临时路径 ,//指定图片存放位置，指定后，在onActivityResult里得到的Data将为null
                                selectFromPhotos.setType("image/*");
                                startActivityForResult(selectFromPhotos, Constant.CODE_SELECT_IMAGE);
                                dialogInterface.cancel();
                            }
                        });
                        alertDialog.show();
                        try {
                            JSONObject jb3 = data.getJSONObject(position);
                            taskId = jb3.getString("taskId");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.received_collect:
                        JSONObject jb = null;
                        try {
                            jb = data.getJSONObject(position);
                            receivedSave(Integer.parseInt(jb.getString("taskId")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.see_received_answer:
                        if (!flag){
                            pos = position;
                            Constant.showAnswerFlag = true;
                            mAdapter.updateData(data);
                            flag = true;
                        }else {
                            pos = position;
                            Constant.showAnswerFlag = false;
                            mAdapter.updateData(data);
                            flag = false;
                        }
                        break;
                    case R.id.received_select:
                        ImageView imgSlect = findViewById(R.id.received_select);
                        JSONObject jb1 = null;
                        String context = "";
                        String contextImg = "";
                        String id = "";
                        try {
                            jb1 = data.getJSONObject(position);
                            context = jb1.getString("content");
                            contextImg = jb1.getString("imgUri");
                            id = jb1.getString("taskId");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (selectFlag) {
                            list.remove(getData(context,contextImg,id));
                            imgSlect.setImageResource(R.mipmap.select_false);
                            selectFlag = false;
                        } else {
                            list.add(getData(context,contextImg,id));
                            imgSlect.setImageResource(R.mipmap.select_true);
                            selectFlag = true;
                        }
                        break;
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intentData) {
        super.onActivityResult(requestCode, resultCode, intentData);
        switch (requestCode) {
            case Constant.CODE_SELECT_IMAGE:
                if (intentData != null) {
                    String imagePath = null;//String.valueOf(getActivity().getResources().getDrawable(R.mipmap.icon));
                    try {
                        Uri uri = intentData.getData();
                        String[] filePathColumns = {MediaStore.Images.Media.DATA};
                        Cursor c = this.getContentResolver().query(uri, filePathColumns, null, null, null);
                        Objects.requireNonNull(c).moveToFirst();
                        int columnIndex = c.getColumnIndex(filePathColumns[0]);
                        imagePath = c.getString(columnIndex);
                        c.close();
                    } catch (NullPointerException e) {
                        Log.e("选择图片出现异常", e.getMessage());
                    }
                    if (prodImgA != null)//如果不释放的话，不断取图片，将会内存不够
                        prodImgA.recycle();
                    prodImgA = ImageHandle.getBitmap(imagePath);
                    if (prodImgA != null) {
                        //imgAnswer.setImageBitmap(prodImgA);
                        strAnswer = bitmapToString(ImageHandle.compressScale(prodImgA));
                    }
                }
                break;
            case Constant.CODE_TAKE_PHOTO:
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(mFilePath); // 根据路径获取数据
                    Bitmap bitmap = ImageHandle.compressScale(BitmapFactory.decodeStream(fis)); //已经按质量压缩
                    //imgAnswer.setImageBitmap(bitmap);// 显示图片
                    strAnswer = ImageHandle.bitmapToString(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fis.close();// 关闭流
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
        receivedSave(Integer.parseInt(taskId));
        Constant.showWriteAnswerFlag = true;
        mAdapter.updateData(data);

    }

    private HashMap getData(String context,String imgUri,String id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("from", "notFromAddFragment");
        map.put("content", context);
        map.put("imgUri", imgUri);//strBitmap);
        map.put("answer", correctAnswer);//strBitmap);
        map.put("myAnswer",myAnswer);
        map.put("occupation",occupation);
        map.put("taskId",id);
        return map;
    }

    public void receivedSave(int id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("username", Constant.username);
        map.put("action", "ReceivedSave");
        map.put("taskId", id);
        map.put("answer", strAnswer);
        RetrofitBuilder builder = new RetrofitBuilder(new Constant().BaseUrl + "/api/save/");
        builder.isConnected(this);
        builder.params(map);
        builder.post();
        Call<ResponseBody> call = builder.getCall();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    correctAnswer = jsonObject.getString("answer");
                    myAnswer = jsonObject.getString("myAnswer");
                    occupation = jsonObject.getString("occupation");
                    Toast.makeText(ReceivedTasksActivity.this, jsonObject.getString("msg"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                MainActivity.updateCollectFlag = true;
            }

            //请求失败时回调
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Toast.makeText(ReceivedTasksActivity.this, getResources().getText(R.string.connect_to_server), Toast.LENGTH_LONG).show();
                System.out.println(throwable.getMessage());
            }
        });
    }

    @Override
    protected void onDestroy() {
        Constant.showWriteAnswerFlag = false;
        Constant.showSelectFlag = false;
        mAdapter.updateData(data);
        super.onDestroy();
    }
}